package dev.davelpz.jray.matrix.operations;

import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.tuple.Tuple;

public interface MatrixOperations {

    Matrix translation(double x, double y, double z);

    Matrix scaling(double x, double y, double z);

    Matrix rotationX(double radians);

    Matrix rotationY(double radians);

    Matrix rotationZ(double radians);

    Matrix rotationXDeg(double degrees);

    Matrix rotationYDeg(double degrees);

    Matrix rotationZDeg(double degrees);

    Matrix shearing(double xy, double xz, double yx, double yz, double zx, double zy);

    Matrix viewTransform(Tuple from, Tuple to, Tuple up);
}
