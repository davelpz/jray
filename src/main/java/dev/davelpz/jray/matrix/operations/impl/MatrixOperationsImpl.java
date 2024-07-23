package dev.davelpz.jray.matrix.operations.impl;

import com.google.inject.Inject;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.matrix.MatrixFactory;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.tuple.Tuple;

public class MatrixOperationsImpl implements MatrixOperations {
    private final MatrixFactory factory;

    @Inject
    public MatrixOperationsImpl(MatrixFactory factory) {
        this.factory = factory;
    }

    @Override
    public Matrix translation(double x, double y, double z) {
        Matrix result = factory.create(4, 4);
        result.set(0, 0, 1);
        result.set(1, 1, 1);
        result.set(2, 2, 1);
        result.set(3, 3, 1);
        result.set(0, 3, x);
        result.set(1, 3, y);
        result.set(2, 3, z);
        return result;
    }

    @Override
    public Matrix scaling(double x, double y, double z) {
        Matrix result = factory.create(4, 4);
        result.set(0, 0, x);
        result.set(1, 1, y);
        result.set(2, 2, z);
        result.set(3, 3, 1);
        return result;
    }

    @Override
    public Matrix rotationX(double radians) {
        Matrix result = factory.create(4, 4);
        result.set(0, 0, 1);
        result.set(1, 1, Math.cos(radians));
        result.set(1, 2, -Math.sin(radians));
        result.set(2, 1, Math.sin(radians));
        result.set(2, 2, Math.cos(radians));
        result.set(3, 3, 1);
        return result;
    }

    @Override
    public Matrix rotationXDeg(double degrees) {
        return rotationX(Math.toRadians(degrees));
    }

    @Override
    public Matrix rotationY(double radians) {
        Matrix result = factory.create(4, 4);
        result.set(0, 0, Math.cos(radians));
        result.set(0, 2, Math.sin(radians));
        result.set(1, 1, 1);
        result.set(2, 0, -Math.sin(radians));
        result.set(2, 2, Math.cos(radians));
        result.set(3, 3, 1);
        return result;
    }

    @Override
    public Matrix rotationYDeg(double degrees) {
        return rotationY(Math.toRadians(degrees));
    }

    @Override
    public Matrix rotationZ(double radians) {
        Matrix result = factory.create(4, 4);
        result.set(0, 0, Math.cos(radians));
        result.set(0, 1, -Math.sin(radians));
        result.set(1, 0, Math.sin(radians));
        result.set(1, 1, Math.cos(radians));
        result.set(2, 2, 1);
        result.set(3, 3, 1);
        return result;
    }

    @Override
    public Matrix rotationZDeg(double degrees) {
        return rotationZ(Math.toRadians(degrees));
    }

    @Override
    public Matrix shearing(double xy, double xz, double yx, double yz, double zx, double zy) {
        Matrix result = factory.create(4, 4);
        result.set(0, 0, 1);
        result.set(0, 1, xy);
        result.set(0, 2, xz);
        result.set(1, 0, yx);
        result.set(1, 1, 1);
        result.set(1, 2, yz);
        result.set(2, 0, zx);
        result.set(2, 1, zy);
        result.set(2, 2, 1);
        result.set(3, 3, 1);
        return result;
    }

    @Override
    public Matrix viewTransform(Tuple from, Tuple to, Tuple up) {
        Tuple forward = to.subtract(from).normalize();
        Tuple upn = up.normalize();
        Tuple left = forward.cross(upn);
        Tuple trueUp = left.cross(forward);

        Matrix orientation = factory.create(4, 4);
        orientation.set(0, 0, left.x());
        orientation.set(0, 1, left.y());
        orientation.set(0, 2, left.z());
        orientation.set(1, 0, trueUp.x());
        orientation.set(1, 1, trueUp.y());
        orientation.set(1, 2, trueUp.z());
        orientation.set(2, 0, -forward.x());
        orientation.set(2, 1, -forward.y());
        orientation.set(2, 2, -forward.z());
        orientation.set(3, 3, 1);

        return orientation.multiply(translation(-from.x(), -from.y(), -from.z()));
    }
}
