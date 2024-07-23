package dev.davelpz.jray.material.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.material.Pattern;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.tuple.Tuple;

public class TestPattern implements Pattern {
    protected Matrix transform;
    private final ColorFactory colorFactory;

    @Inject
    public TestPattern(@Named("identityMatrix") Matrix identityMatrix, ColorFactory colorFactory) {
        this.transform = identityMatrix;
        this.colorFactory = colorFactory;
    }

    @Override
    public Matrix transform() {
        return transform;
    }

    @Override
    public void setTransform(Matrix transform) {
        this.transform = transform;
    }

    @Override
    public Color patternAt(Tuple point) {
        return colorFactory.create(point.x(), point.y(), point.z());
    }
}
