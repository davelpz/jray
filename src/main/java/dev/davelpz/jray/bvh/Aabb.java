package dev.davelpz.jray.bvh;

import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.tuple.Tuple;


public interface Aabb {
    Tuple min();

    Tuple max();

    boolean hit(Ray r, double tmin, double tmax);

    void adjust(Tuple point);
    void adjust(Aabb aabb);

    Aabb applyTransform(Matrix transform);

    boolean intersect(Ray ray);
}
