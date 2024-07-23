package dev.davelpz.jray.tuple.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.TupleFactoryBase;

public class Tup implements Tuple {

    private final TupleFactoryBase factory;
    private final double x;
    private final double y;
    private final double z;
    private double w;

    @Inject
    public Tup(@Assisted("x") double x, @Assisted("y") double y, @Assisted("z") double z, @Assisted("w") double w, TupleFactoryBase factory) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.factory = factory;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double w() {
        return w;
    }

    @Override
    public void setW(double w) {
        this.w = w;
    }

    @Override
    public TupleFactoryBase factory() {
        return factory;
    }
}
