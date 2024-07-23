package dev.davelpz.jray.shape.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import dev.davelpz.jray.bvh.Aabb;
import dev.davelpz.jray.bvh.AabbFactory;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.shape.Triangle;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;

import java.util.List;

public class TriangleImpl extends ShapeBase implements Triangle {

    private final Tuple p1;
    private final Tuple p2;
    private final Tuple p3;
    private final Tuple e1;
    private final Tuple e2;
    private final Tuple normal;


    @Inject
    public TriangleImpl(@Named("identityMatrix") Matrix identityMatrix, @Named("defaultMaterial") Material material, TupleFactory tupleFactory,
            IntersectionFactory intersectionFactory, AabbFactory aabbFactory,
            @Assisted("p1") Tuple p1, @Assisted("p2") Tuple p2, @Assisted("p3") Tuple p3) {
        super(identityMatrix, material, tupleFactory, intersectionFactory, aabbFactory);
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.e1 = p2.subtract(p1);
        this.e2 = p3.subtract(p1);
        this.normal = e2.cross(e1).normalize();
    }

    @Override
    public List<Intersection> intersect(Ray ray) {
        Tuple dirCrossE2 = ray.direction().cross(e2);
        double det = e1().dot(dirCrossE2);
        if (Math.abs(det) < 0.0001) {
            return List.of();
        }

        double f = 1.0 / det;
        Tuple p1ToOrigin = ray.origin().subtract(p1);
        double u = f * p1ToOrigin.dot(dirCrossE2);
        if (u < 0 || u > 1) {
            return List.of();
        }

        Tuple originCrossE1 = p1ToOrigin.cross(e1);
        double v = f * ray.direction().dot(originCrossE1);
        if (v < 0 || (u + v) > 1) {
            return List.of();
        }

        double t = f * e2.dot(originCrossE1);
        return List.of(intersectionFactory.create(t, this));
    }

    @Override
    public Tuple localNormalAt(Tuple objectPoint) {
        return normal;
    }

    @Override
    public Aabb buildBoundingBox() {
        Tuple min = tupleFactory.point(Math.min(p1.x(), Math.min(p2.x(), p3.x())), Math.min(p1.y(), Math.min(p2.y(), p3.y())),
                Math.min(p1.z(), Math.min(p2.z(), p3.z())));
        Tuple max = tupleFactory.point(Math.max(p1.x(), Math.max(p2.x(), p3.x())), Math.max(p1.y(), Math.max(p2.y(), p3.y())),
                Math.max(p1.z(), Math.max(p2.z(), p3.z())));

        return aabbFactory.create(min, max);
    }

    public Tuple p1() {
        return p1;
    }

    public Tuple p2() {
        return p2;
    }

    public Tuple p3() {
        return p3;
    }

    public Tuple e1() {
        return e1;
    }

    public Tuple e2() {
        return e2;
    }

    public Tuple normal() {
        return normal;
    }
}
