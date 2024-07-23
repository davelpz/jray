package dev.davelpz.jray.shape;

import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.tuple.Tuple;

public interface SmoothTriangleFactory {
    SmoothTriangle create(@Assisted("p1") Tuple p1, @Assisted("p2") Tuple p2, @Assisted("p3") Tuple p3,
            @Assisted("n1") Tuple n1, @Assisted("n2") Tuple n2, @Assisted("n3") Tuple n3);
}
