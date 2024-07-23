package dev.davelpz.jray.material;

import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;

public interface Pattern {
    Matrix transform();
    void setTransform(Matrix transform);
    Color patternAt(Tuple point);

    default Color patternAtShape(Shape shape, Tuple worldPoint) {
        Tuple objectPoint = shape.worldToObject(worldPoint);
        //Tuple objectPoint = shape.transform().inverse().multiply(worldPoint);
        Tuple patternPoint = transform().inverse().multiply(objectPoint);
        return patternAt(patternPoint);
    }
}
