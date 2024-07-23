package dev.davelpz.jray.ray.operations;

import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;

public interface Computation {
    double t();
    Shape shape();
    Tuple point();
    Tuple eyeV();
    Tuple normalV();
    Tuple reflectV();
    boolean inside();
    Tuple overPoint();
    Tuple underPoint();
    double n1();
    double n2();
    double schlick();
}
