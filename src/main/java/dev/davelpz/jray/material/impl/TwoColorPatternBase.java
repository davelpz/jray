package dev.davelpz.jray.material.impl;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.material.Pattern;
import dev.davelpz.jray.matrix.Matrix;

public abstract class TwoColorPatternBase implements Pattern {
    protected final Color a;
    protected final Color b;
    protected Matrix transform;

    protected TwoColorPatternBase(@Assisted("a") Color a, @Assisted("b") Color b, @Named("identityMatrix") Matrix identityMatrix) {
        this.a = a;
        this.b = b;
        this.transform = identityMatrix;
    }

    public Color a() {
        return a;
    }

    public Color b() {
        return b;
    }

    @Override
    public Matrix transform() {
        return transform;
    }

    @Override
    public void setTransform(Matrix transform) {
        this.transform = transform;
    }
}
