package dev.davelpz.jray.color;

import dev.davelpz.jray.config.Constants;

/**
 * Represents a color in RGB space.
 * <p>
 *     This interface is used to represent colors in RGB space. It provides methods for adding, subtracting, and multiplying colors.
 *     It also provides a factory method for creating new colors.
 *     <br>
 *     The default implementation of this interface is {@link dev.davelpz.jray.color.impl.RGB}.
 *     <br>
 *     This interface is immutable.
 */
public interface Color {

    double red();

    double green();

    double blue();

    ColorFactory factory();

    default Color add(Color other) {
        return factory().create(red() + other.red(), green() + other.green(), blue() + other.blue());
    }

    default Color subtract(Color other) {
        return factory().create(red() - other.red(), green() - other.green(), blue() - other.blue());
    }

    default Color multiply(double scalar) {
        return factory().create(red() * scalar, green() * scalar, blue() * scalar);
    }

    default Color multiply(Color other) {
        return factory().create(red() * other.red(), green() * other.green(), blue() * other.blue());
    }

    default Color divide(double scalar) {
        return factory().create(red() / scalar, green() / scalar, blue() / scalar);
    }

    default boolean equals(Color other) {
        return (Math.abs(red() - other.red()) < Constants.EPSILON) && (Math.abs(green() - other.green()) < Constants.EPSILON) && (Math.abs(blue() - other.blue())
                < Constants.EPSILON);
    }
}
