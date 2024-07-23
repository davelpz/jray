package dev.davelpz.jray.shape.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.davelpz.jray.bvh.Aabb;
import dev.davelpz.jray.bvh.AabbFactory;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.shape.Plane;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;

import java.util.List;

public class PlaneImpl extends ShapeBase implements Plane {

    @Inject
    public PlaneImpl(@Named("defaultMaterial") Material material,@Named("identityMatrix") Matrix transform, TupleFactory tupleFactory, IntersectionFactory intersectionFactory,
            AabbFactory aabbFactory) {
        super(transform, material, tupleFactory, intersectionFactory, aabbFactory);
    }

    @Override
    public List<Intersection> intersect(Ray ray) {
        if (Math.abs(ray.direction().y()) < Constants.EPSILON) {
            return List.of();
        }

        double t = -ray.origin().y() / ray.direction().y();
        return List.of(intersectionFactory.create(t, this));
    }

    @Override
    public Tuple localNormalAt(Tuple worldPoint) {
        return tupleFactory.vector(0, 1, 0);
    }

    @Override
    public Aabb buildBoundingBox() {
        return aabbFactory.create(tupleFactory.point(Double.NEGATIVE_INFINITY, 0, Double.NEGATIVE_INFINITY),
                tupleFactory.point(Double.POSITIVE_INFINITY, 0, Double.POSITIVE_INFINITY));
    }
}
