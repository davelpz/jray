package dev.davelpz.jray.shape.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import dev.davelpz.jray.bvh.AabbFactory;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.shape.SmoothTriangle;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;

import java.util.List;

public class SmoothTriangleImpl extends TriangleImpl implements SmoothTriangle {

    private final Tuple n1;
    private final Tuple n2;
    private final Tuple n3;

    @Inject
    public SmoothTriangleImpl(@Named("identityMatrix") Matrix identityMatrix, @Named("defaultMaterial") Material material, TupleFactory tupleFactory,
            IntersectionFactory intersectionFactory, AabbFactory aabbFactory,
            @Assisted("p1") Tuple p1, @Assisted("p2") Tuple p2, @Assisted("p3") Tuple p3,
            @Assisted("n1") Tuple n1, @Assisted("n2") Tuple n2, @Assisted("n3") Tuple n3) {
        super(identityMatrix, material, tupleFactory, intersectionFactory, aabbFactory, p1, p2, p3);
        this.n1 = n1;
        this.n2 = n2;
        this.n3 = n3;
    }

    @Override
    public List<Intersection> intersect(Ray ray) {
        Tuple dirCrossE2 = ray.direction().cross(e2());
        double det = e1().dot(dirCrossE2);
        if (Math.abs(det) < 0.0001) {
            return List.of();
        }

        double f = 1.0 / det;
        Tuple p1ToOrigin = ray.origin().subtract(p1());
        double u = f * p1ToOrigin.dot(dirCrossE2);
        if (u < 0 || u > 1) {
            return List.of();
        }

        Tuple originCrossE1 = p1ToOrigin.cross(e1());
        double v = f * ray.direction().dot(originCrossE1);
        if (v < 0 || (u + v) > 1) {
            return List.of();
        }

        double t = f * e2().dot(originCrossE1);
        return List.of(intersectionFactory.create(t, this, u, v));
    }

    public Tuple n1() {
        return n1;
    }

    public Tuple n2() {
        return n2;
    }

    public Tuple n3() {
        return n3;
    }
}

