package dev.davelpz.jray.tuple;

import dev.davelpz.jray.config.Constants;

/**
 * Tuple interface models a 4D tuple. It is used to represent points and vectors.
 */
public interface Tuple {
    double x();

    double y();

    double z();

    double w();

    void setW(double w);

    TupleFactoryBase factory();

    default boolean equals(Tuple other) {
        return (Math.abs(x() - other.x()) < Constants.EPSILON)
                && (Math.abs(y() - other.y()) < Constants.EPSILON)
                && (Math.abs(z() - other.z()) < Constants.EPSILON)
                && (Math.abs(w() - other.w()) < Constants.EPSILON);
    }

    default boolean isPoint() {
        return w() == 1.0f;
    }

    default boolean isVector() {
        return w() == 0.0f;
    }

    default Tuple add(Tuple other) {
        return factory().create(x() + other.x(), y() + other.y(), z() + other.z(), w() + other.w());
    }

    default Tuple subtract(Tuple other) {
        return factory().create(x() - other.x(), y() - other.y(), z() - other.z(), w() - other.w());
    }

    default Tuple negate() {
        return factory().create(-x(), -y(), -z(), -w());
    }

    default Tuple multiply(double scalar) {
        return factory().create(x() * scalar, y() * scalar, z() * scalar, w() * scalar);
    }

    default Tuple divide(double scalar) {
        return factory().create(x() / scalar, y() / scalar, z() / scalar, w() / scalar);
    }

    default double magnitude() {
        return Math.sqrt(x() * x() + y() * y() + z() * z() + w() * w());
    }

    default Tuple normalize() {
        double mag = magnitude();
        // if this is a zero vector, return a zero vector
        // otherwise, divide by the magnitude
        if (mag == 0.0f) {
            return factory().create(0.0f, 0.0f, 0.0f, 0.0f);
        }
        return divide(mag);
    }

    default double dot(Tuple other) {
        return x() * other.x() + y() * other.y() + z() * other.z() + w() * other.w();
    }

    default Tuple cross(Tuple other) {
        return factory().create(y() * other.z() - z() * other.y(), z() * other.x() - x() * other.z(), x() * other.y() - y() * other.x(), 0.0f);
    }

    default Tuple reflect(Tuple normal) {
        return subtract(normal.multiply(2.0 * dot(normal)));
    }
}
