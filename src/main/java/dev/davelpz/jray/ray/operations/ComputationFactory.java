package dev.davelpz.jray.ray.operations;

import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;

public interface ComputationFactory {
    Computation create(@Assisted("t") double t, @Assisted("object") Shape object, @Assisted("point") Tuple point, @Assisted("eyeV") Tuple eyeV,
            @Assisted("normalV") Tuple normalV, @Assisted("reflectV") Tuple reflectV, @Assisted("inside") boolean inside,
            @Assisted("n1") double n1, @Assisted("n2") double n2);
}
