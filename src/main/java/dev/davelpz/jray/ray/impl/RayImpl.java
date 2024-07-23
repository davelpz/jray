package dev.davelpz.jray.ray.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.tuple.Tuple;

public class RayImpl implements Ray {
    private final Tuple origin;
    private final Tuple direction;

    @Inject
    public RayImpl(@Assisted("origin")  Tuple origin, @Assisted("direction")  Tuple direction) {
        this.origin = origin;
        this.direction = direction;
    }

    @Override
    public Tuple origin() {
        return origin;
    }

    @Override
    public Tuple direction() {
        return direction;
    }

    @Override
    public Tuple position(double t) {
        return origin.add(direction.multiply(t));
    }
}
