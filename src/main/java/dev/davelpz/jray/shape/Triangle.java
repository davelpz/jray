package dev.davelpz.jray.shape;

import dev.davelpz.jray.tuple.Tuple;

public interface Triangle extends Shape {
    Tuple p1();
    Tuple p2();
    Tuple p3();
    Tuple e1();
    Tuple e2();
    Tuple normal();
}
