package dev.davelpz.jray.material.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.tuple.Tuple;

public class StripePattern extends TwoColorPatternBase {

    @Inject
    public StripePattern(@Named("identityMatrix") Matrix identityMatrix, @Assisted("a") Color a, @Assisted("b")  Color b) {
        super(a, b, identityMatrix);
    }

    @Override
    public Color patternAt(Tuple point) {
        if (Math.floor(point.x()) % 2 == 0) {
            return a;
        } else {
            return b;
        }
    }
}
