package dev.davelpz.jray.world.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.light.LightSource;
import dev.davelpz.jray.light.operations.LightOperations;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.Computation;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WorldImpl implements World {
    private final List<LightSource> lights;
    private final List<Shape> objects;
    private final RayFactory rayFactory;
    private final RayOperations rayOperations;
    private final LightOperations lightOperations;
    private final Color black;

    @Inject
    public WorldImpl(RayFactory rayFactory, RayOperations rayOperations, LightOperations lightOperations, @Named("blackColor") Color black) {
        this.rayFactory = rayFactory;
        this.rayOperations = rayOperations;
        this.lightOperations = lightOperations;
        this.black = black;
        lights = new ArrayList<>();
        objects = new ArrayList<>();
    }

    @Override
    public List<LightSource> lights() {
        return lights;
    }

    @Override
    public List<Shape> objects() {
        return objects;
    }

    @Override
    public void addLight(LightSource light) {
        lights.add(light);
    }

    @Override
    public void addObject(Shape shape) {
        objects.add(shape);
    }

    @Override
    public List<Intersection> intersect(Ray ray) {
        List<Intersection> hits = new ArrayList<>();
        for (Shape object : objects) {
            List<Intersection> intersections = rayOperations.intersect(object, ray);
            hits.addAll(intersections);
        }
        //sort by t
        Collections.sort(hits);
        return hits;
    }

    @Override
    public Color shadeHit(Computation computation, int remaining) {
        Color surface = black;
        for (LightSource light : lights) {
            boolean inShadow = isShadowed(computation.overPoint());
            Color temp = lightOperations.lighting(computation.shape().material(), computation.shape(), light, computation.overPoint(), computation.eyeV(), computation.normalV(), inShadow);
            surface = surface.add(temp);
        }
        Color reflected = reflectedColor(computation, remaining);
        Color refracted = refractedColor(computation, remaining);

        Material material = computation.shape().material();
        if (material.reflectivity() > 0 && material.transparency() > 0) {
            double reflectance = computation.schlick();
            return surface.add(reflected.multiply(reflectance).add(refracted.multiply(1 - reflectance)));
        } else {
            return surface.add(reflected).add(refracted);
        }
    }

    @Override
    public Color colorAt(Ray ray, int remaining) {
        List<Intersection> intersections = intersect(ray);
        Optional<Intersection> hitOpt = rayOperations.hit(intersections);
        if (hitOpt.isPresent()) {
            Computation computation = rayOperations.prepareComputations(hitOpt.get(), ray, intersections);
            return shadeHit(computation, remaining);
        } else {
            return black;
        }
    }

    @Override
    public Color reflectedColor(Computation computation, int remaining) {
        if (remaining <= 0 || computation.shape().material().reflectivity() < Constants.EPSILON) {
            return black;
        }

        Ray reflectRay = rayFactory.create(computation.overPoint(), computation.reflectV());
        Color color = colorAt(reflectRay, remaining - 1);

        return color.multiply(computation.shape().material().reflectivity());
    }

    @Override
    public Color refractedColor(Computation computation, int remaining) {
        if (remaining <= 0 || computation.shape().material().transparency() < Constants.EPSILON) {
            return black;
        }
        // find the ratio of first index of refraction to the second.
        // (Yup, this is inverted from the definition of Snell's Law.)
        double nRatio = computation.n1() / computation.n2();

        // cos(theta_i) is the same as the dot product of the two vectors
        double cosI = computation.eyeV().dot(computation.normalV());

        // find sin(theta_t)^2 via trigonometric identity
        double sin2T = (nRatio * nRatio) * (1 - (cosI * cosI));
        if (sin2T > 1) {
            return black;
        }

        // find cos(theta_t) via trigonometric identity
        double cosT = Math.sqrt(1.0 - sin2T);

        // compute the direction of the refracted ray
        Tuple direction = computation.normalV().multiply(nRatio * cosI - cosT).subtract(computation.eyeV().multiply(nRatio));

        // create the refracted ray
        Ray refractRay = rayFactory.create(computation.underPoint(), direction);

        // Find the color of the refracted ray, making sure to multiply
        // by the transparency value to account for any opacity
        return colorAt(refractRay, remaining - 1).multiply(computation.shape().material().transparency());
    }

    @Override
    public boolean isShadowed(Tuple point) {
        Tuple v = lights.get(0).position().subtract(point);
        double distance = v.magnitude();
        Tuple direction = v.normalize();

        Ray ray = rayFactory.create(point, direction);
        List<Intersection> intersections = intersect(ray);

        Optional<Intersection> hitOpt = rayOperations.hit(intersections);
        if (hitOpt.isPresent()) {
            Intersection hit = hitOpt.get();
            return hit.t() < distance;
        }

        return false;
    }
}
