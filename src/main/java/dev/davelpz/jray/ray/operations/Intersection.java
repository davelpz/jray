package dev.davelpz.jray.ray.operations;

import dev.davelpz.jray.shape.Shape;

public interface Intersection extends Comparable<Intersection> {
    double t();
    Shape shape();
    double u();
    double v();

    @Override
    default int compareTo(Intersection o) {
        return Double.compare(t(), o.t());
    }
}
