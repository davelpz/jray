package dev.davelpz.jray.material.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.tuple.Tuple;

public class CheckersPattern extends TwoColorPatternBase {

    @Inject
    public CheckersPattern(@Named("identityMatrix") Matrix identityMatrix, @Assisted("a") Color a, @Assisted("b")  Color b) {
        super(a, b, identityMatrix);
    }

    @Override
    public Color patternAt(Tuple point) {
        int x = (int)(Math.floor(point.x()));
        int y = (int)(Math.floor(point.y()));
        int z = (int)(Math.floor(point.z()));
        int sum = x + y + z;
        if (sum % 2 == 0) {
            return a;
        } else {
            return b;
        }
    }
}
