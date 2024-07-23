package dev.davelpz.jray.shape;

import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.tuple.Tuple;

public interface TriangleFactory {
    Triangle create(@Assisted("p1") Tuple p1, @Assisted("p2") Tuple p2, @Assisted("p3") Tuple p3);
}
