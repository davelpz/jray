package dev.davelpz.jray.ray.operations.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.shape.Shape;

import javax.annotation.Nullable;

public class IntersectionImpl implements Intersection {
    private final double t;
    private final Shape shape;
    private final double u;
    private final double v;

    @Inject
    public IntersectionImpl(@Assisted("t") double t, @Assisted @Nullable Shape shape) {
        this(t, shape, -1, -1);
    }

    public IntersectionImpl(@Assisted("t") double t, @Assisted @Nullable Shape shape,
            @Assisted("u") double u, @Assisted("v") double v) {
        this.t = t;
        this.shape = shape;
        this.u = u;
        this.v = v;
    }

    @Override
    public double t() {
        return t;
    }

    @Override
    public Shape shape() {
        return shape;
    }

    @Override
    public double u() {
        return u;
    }

    @Override
    public double v() {
        return v;
    }
}
