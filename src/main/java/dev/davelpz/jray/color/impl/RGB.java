package dev.davelpz.jray.color.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;

public class RGB implements Color {
    private final double red;
    private final double green;
    private final double blue;
    private final ColorFactory factory;

    @Inject
    public RGB(@Assisted("red") double red, @Assisted("green") double green, @Assisted("blue") double blue, ColorFactory factory) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.factory = factory;
    }

    @Override
    public double red() {
        return red;
    }

    @Override
    public double green() {
        return green;
    }

    @Override
    public double blue() {
        return blue;
    }

    @Override
    public ColorFactory factory() {
        return factory;
    }
}
