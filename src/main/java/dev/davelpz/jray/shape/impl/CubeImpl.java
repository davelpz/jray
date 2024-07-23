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
import dev.davelpz.jray.shape.Cube;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;

import java.util.List;

public class CubeImpl extends ShapeBase implements Cube {

    @Inject
    public CubeImpl(@Named("defaultMaterial") Material material,@Named("identityMatrix") Matrix transform, TupleFactory tupleFactory, IntersectionFactory intersectionFactory,
            AabbFactory aabbFactory) {
        super(transform, material, tupleFactory, intersectionFactory, aabbFactory);
    }

    @Override
    public List<Intersection> intersect(Ray ray) {
        double[] xtminmax = checkAxis(ray.origin().x(), ray.direction().x());
        double[] ytminmax = checkAxis(ray.origin().y(), ray.direction().y());
        double[] ztminmax = checkAxis(ray.origin().z(), ray.direction().z());

        double tmin = Math.max(Math.max(xtminmax[0], ytminmax[0]), ztminmax[0]);
        double tmax = Math.min(Math.min(xtminmax[1], ytminmax[1]), ztminmax[1]);

        if (tmin > tmax) {
            return List.of();
        }

        return List.of(intersectionFactory.create(tmin, this), intersectionFactory.create(tmax, this));
    }

    private double[] checkAxis(double origin, double direction) {
        double tminNumerator = (-1 - origin);
        double tmaxNumerator = (1 - origin);
        double tmin;
        double tmax;
        if (Math.abs(direction) >= Constants.EPSILON) {
            tmin = tminNumerator / direction;
            tmax = tmaxNumerator / direction;
        } else {
            tmin = tminNumerator * Double.POSITIVE_INFINITY;
            tmax = tmaxNumerator * Double.POSITIVE_INFINITY;
        }
        if (tmin > tmax) {
            double temp = tmin;
            tmin = tmax;
            tmax = temp;
        }
        return new double[]{tmin,tmax};
    }

    @Override
    public Tuple localNormalAt(Tuple worldPoint) {
        double maxc = Math.max(Math.max(Math.abs(worldPoint.x()), Math.abs(worldPoint.y())), Math.abs(worldPoint.z()));
        if (maxc == Math.abs(worldPoint.x())) {
            return tupleFactory.vector(worldPoint.x(), 0, 0);
        } else if (maxc == Math.abs(worldPoint.y())) {
            return tupleFactory.vector(0, worldPoint.y(), 0);
        }
        return tupleFactory.vector(0, 0, worldPoint.z());
    }

    @Override
    public Aabb buildBoundingBox() {
        return aabbFactory.create(tupleFactory.point(-1, -1, -1), tupleFactory.point(1, 1, 1));
    }
}
