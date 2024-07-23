package dev.davelpz.jray.ray.operations.impl;

import com.google.inject.Inject;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.operations.Computation;
import dev.davelpz.jray.ray.operations.ComputationFactory;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RayOperationsImpl implements RayOperations {
    private final RayFactory rayFactory;
    private final IntersectionFactory intersectionFactory;
    private final ComputationFactory computationFactory;

    @Inject
    public RayOperationsImpl(RayFactory rayFactory, IntersectionFactory intersectionFactory,
            ComputationFactory computationFactory) {
        this.rayFactory = rayFactory;
        this.intersectionFactory = intersectionFactory;
        this.computationFactory = computationFactory;
    }

    @Override
    public List<Intersection> intersect(Shape shape, Ray ray) {
        ray = transform(ray, shape.transform().inverse());
        return shape.intersect(ray);
    }

    @Override
    public Optional<Intersection> hit(List<Intersection> intersections) {
        Intersection target = intersectionFactory.create(Float.MAX_VALUE, null);

        for (Intersection intersection: intersections) {
            double t = intersection.t();
            if (t >= 0 && t < target.t()) {
                target = intersection;
            }
        }

        if (target.t() == Float.MAX_VALUE) {
            return Optional.empty();
        } else {
            return Optional.of(target);
        }
    }

    @Override
    public Computation prepareComputations(Intersection intersection, Ray ray, List<Intersection> intersections) {
        Tuple point = ray.position(intersection.t());
        Tuple eyeVector = ray.direction().negate();
        Tuple normalVector = intersection.shape().normalAt(point);
        boolean inside = false;
        if (normalVector.dot(eyeVector) < 0) {
            inside = true;
            normalVector = normalVector.negate();
        }
        //Tuple overPoint = point.add(normalVector.multiply(Constants.EPSILON));
        Tuple reflectVector = ray.direction().reflect(normalVector);

        List<Shape> containers = new ArrayList<>();
        double n1 = 0.0;
        double n2 = 0.0;
        for (Intersection i: intersections) {
            if (i.equals(intersection)) {
                if (containers.isEmpty()) {
                    n1 = 1.0;
                } else {
                    n1 = containers.get(containers.size() - 1).material().refractiveIndex();
                }
            }
            if (containers.contains(i.shape())) {
                containers.remove(i.shape());
            } else {
                containers.add(i.shape());
            }

            if (i.equals(intersection)) {
                if (containers.isEmpty()) {
                    n2 = 1.0;
                } else {
                    n2 = containers.get(containers.size() - 1).material().refractiveIndex();
                }
                break;
            }
        }

        return computationFactory.create(intersection.t(), intersection.shape(), point, eyeVector, normalVector, reflectVector,
                inside, n1, n2);
    }

    @Override
    public Ray transform(Ray ray, Matrix m) {
        Tuple origin = m.multiply(ray.origin());
        Tuple direction = m.multiply(ray.direction());
        return rayFactory.create(origin, direction);
    }
}
