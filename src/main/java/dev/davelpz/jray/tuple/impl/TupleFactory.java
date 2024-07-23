package dev.davelpz.jray.tuple.impl;

import com.google.inject.Inject;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.TupleFactoryBase;

public class TupleFactory implements TupleFactoryBase {
    TupleFactoryBase factory;

    @Inject
    public TupleFactory(TupleFactoryBase factory) {
        this.factory = factory;
    }

    @Override
    public Tuple create(double x, double y, double z, double w) {
        return factory.create(x, y, z, w);
    }

    public Tuple point(double x, double y, double z) {
        return factory.create(x, y, z, 1.0f);
    }

    public Tuple vector(double x, double y, double z) {
        return factory.create(x, y, z, 0.0f);
    }
}
