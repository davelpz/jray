package dev.davelpz.jray.light;

import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.tuple.Tuple;

/**
 * A light source in the scene that can be used to illuminate objects
 */
public interface LightSource {
    Color intensity();
    Tuple position();

    default boolean equals(LightSource other) {
        return intensity().equals(other.intensity()) && position().equals(other.position());
    }
}
