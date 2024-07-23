package dev.davelpz.jray.ray.operations.impl;

import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.shape.Shape;

public class IntersectionFactoryImpl implements IntersectionFactory {
    @Override
    public Intersection create(double t, Shape shape) {
        return new IntersectionImpl(t, shape);
    }

    @Override
    public Intersection create(double t, Shape shape, double u, double v) {
        return new IntersectionImpl(t, shape, u, v);
    }
}
