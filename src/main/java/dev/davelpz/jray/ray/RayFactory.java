package dev.davelpz.jray.ray;

import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.tuple.Tuple;

public interface RayFactory {
    Ray create(@Assisted("origin") Tuple origin, @Assisted("direction") Tuple direction);
}
