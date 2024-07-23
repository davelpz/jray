package dev.davelpz.jray.bvh.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.bvh.Aabb;
import dev.davelpz.jray.bvh.AabbFactory;
import dev.davelpz.jray.bvh.BvhOperations;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;


public class AabbImpl implements Aabb {
    private Tuple min;
    private Tuple max;
    private final BvhOperations bvhOperations;
    private final TupleFactory tupleFactory;
    private final AabbFactory aabbFactory;

    @Inject
    public AabbImpl(@Assisted("min") Tuple min, @Assisted("max")  Tuple max, BvhOperations bvhOperations, TupleFactory tupleFactory,
                    AabbFactory aabbFactory) {
        this.min = min;
        this.max = max;
        this.bvhOperations = bvhOperations;
        this.tupleFactory = tupleFactory;
        this.aabbFactory = aabbFactory;
    }

    @Override
    public Tuple min() {
        return min;
    }

    @Override
    public Tuple max() {
        return max;
    }

    @Override
    public boolean hit(Ray r, double tmin, double tmax) {
        double t0 = Math.min(
                min.x() - r.origin().x() / r.direction().x(),
                max.x() - r.origin().x() / r.direction().x());
        double t1 = Math.max(
                min.x() - r.origin().x() / r.direction().x(),
                max.x() - r.origin().x() / r.direction().x());
        tmin = Math.max(t0, tmin);
        tmax = Math.min(t1, tmax);
        if (tmax <= tmin) {
            return false;
        }

        t0 = Math.min(
                min.y() - r.origin().y() / r.direction().y(),
                max.y() - r.origin().y() / r.direction().y());
        t1 = Math.max(
                min.y() - r.origin().y() / r.direction().y(),
                max.y() - r.origin().y() / r.direction().y());
        tmin = Math.max(t0, tmin);
        tmax = Math.min(t1, tmax);
        if (tmax <= tmin) {
            return false;
        }

        t0 = Math.min(
                min.z() - r.origin().z() / r.direction().z(),
                max.z() - r.origin().z() / r.direction().z());
        t1 = Math.max(
                min.z() - r.origin().z() / r.direction().z(),
                max.z() - r.origin().z() / r.direction().z());
        tmin = Math.max(t0, tmin);
        tmax = Math.min(t1, tmax);
        return tmax > tmin;
    }

    @Override
    public void adjust(Tuple point) {
        min = tupleFactory.point(Math.min(min.x(), point.x()),
                Math.min(min.y(), point.y()),
                Math.min(min.z(), point.z())
        );
        max = tupleFactory.point(Math.max(max.x(), point.x()),
                Math.max(max.y(), point.y()),
                Math.max(max.z(), point.z())
        );
    }

    @Override
    public void adjust(Aabb aabb) {
        adjust(aabb.min());
        adjust(aabb.max());
    }

    @Override
    public Aabb applyTransform(Matrix transform) {
        Tuple[] corners = new Tuple[]{
                tupleFactory.point(min.x(), min.y(), min.z()),
                tupleFactory.point(min.x(), min.y(), max.z()),
                tupleFactory.point(min.x(), max.y(), min.z()),
                tupleFactory.point(min.x(), max.y(), max.z()),
                tupleFactory.point(max.x(), min.y(), min.z()),
                tupleFactory.point(max.x(), min.y(), max.z()),
                tupleFactory.point(max.x(), max.y(), min.z()),
                tupleFactory.point(max.x(), max.y(), max.z()),
        };

        Aabb box = aabbFactory.create(tupleFactory.point(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY),
                tupleFactory.point(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY));

        for (Tuple corner : corners) {
            box.adjust(transform.multiply(corner));
        }

        return box;
    }

    @Override
    public boolean intersect(Ray ray) {
        double[] xtminmax = checkAxis(ray.origin().x(), ray.direction().x(), min.x(), max.x());
        double[] ytminmax = checkAxis(ray.origin().y(), ray.direction().y(), min.y(), max.y());
        double[] ztminmax = checkAxis(ray.origin().z(), ray.direction().z(), min.z(), max.z());

        double tmin = Math.max(Math.max(xtminmax[0], ytminmax[0]), ztminmax[0]);
        double tmax = Math.min(Math.min(xtminmax[1], ytminmax[1]), ztminmax[1]);

        return tmin <= tmax;
    }


    private double[] checkAxis(double origin, double direction, double min, double max) {
        double tminNumerator = (min - origin);
        double tmaxNumerator = (max - origin);
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
            return new double[]{tmax,tmin};
        } else {
            return new double[] { tmin, tmax };
        }
    }

}
