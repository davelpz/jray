package dev.davelpz.jray.light.operations;

import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.light.LightSource;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;

public interface LightOperations {
    Color lighting(Material material, Shape shape, LightSource light, Tuple position, Tuple eyev, Tuple normalv, boolean inShadow);
}
