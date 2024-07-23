package dev.davelpz.jray.shape.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.davelpz.jray.bvh.Aabb;
import dev.davelpz.jray.bvh.AabbFactory;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.shape.Sphere;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;

import java.util.List;

public class SphereImpl extends ShapeBase implements Sphere {

    @Inject
    public SphereImpl(@Named("identityMatrix") Matrix identityMatrix, @Named("defaultMaterial") Material material, TupleFactory tupleFactory,
            IntersectionFactory intersectionFactory, AabbFactory aabbFactory) {
        super(identityMatrix, material, tupleFactory, intersectionFactory, aabbFactory);
    }

    @Override
    public List<Intersection> intersect(Ray ray) {
        Tuple sphereToRay = ray.origin().subtract(tupleFactory.point(0, 0, 0));
        double a = ray.direction().dot(ray.direction());
        double b = 2 * ray.direction().dot(sphereToRay);
        double c = sphereToRay.dot(sphereToRay) - 1;
        double discriminant = Math.pow(b, 2) - 4 * a * c;
        if (discriminant < 0) {
            return List.of();
        }
        double t1 = (-b - Math.sqrt(discriminant)) / (2 * a);
        double t2 = (-b + Math.sqrt(discriminant)) / (2 * a);
        return List.of(intersectionFactory.create(t1, this), intersectionFactory.create(t2, this));
    }

    @Override
    public Tuple localNormalAt(Tuple objectPoint) {
        return objectPoint.subtract(tupleFactory.point(0, 0, 0));
    }

    @Override
    public Aabb buildBoundingBox() {
        return aabbFactory.create(tupleFactory.point(-1, -1, -1), tupleFactory.point(1, 1, 1));
    }
}
