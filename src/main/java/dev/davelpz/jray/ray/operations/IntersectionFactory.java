package dev.davelpz.jray.ray.operations;

import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.shape.Shape;

public interface IntersectionFactory {
    Intersection create(@Assisted("t") double t, @Assisted Shape shape);
    Intersection create(@Assisted("t") double t, @Assisted Shape shape, @Assisted("u") double u, @Assisted("v") double v);
}
