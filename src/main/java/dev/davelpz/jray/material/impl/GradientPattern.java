package dev.davelpz.jray.material.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.tuple.Tuple;

public class GradientPattern extends TwoColorPatternBase {

    @Inject
    public GradientPattern(@Named("identityMatrix") Matrix identityMatrix, @Assisted("a") Color a, @Assisted("b")  Color b) {
        super(a, b, identityMatrix);
    }

    @Override
    public Color patternAt(Tuple point) {
        Color distance = b.subtract(a);
        double fraction = point.x() - Math.floor(point.x());
        return a.add(distance.multiply(fraction));
    }

}
