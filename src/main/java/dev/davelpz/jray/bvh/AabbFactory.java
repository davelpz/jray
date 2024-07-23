package dev.davelpz.jray.bvh;

import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.tuple.Tuple;

public interface AabbFactory {
    Aabb create(@Assisted("min") Tuple min, @Assisted("max")  Tuple max);
}
