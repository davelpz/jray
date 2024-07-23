package dev.davelpz.jray.bvh;

import com.google.inject.Inject;
import dev.davelpz.jray.bvh.impl.AabbImpl;
import dev.davelpz.jray.tuple.impl.TupleFactory;

public class BvhOperations {

    private final TupleFactory tupleFactory;

    @Inject
    public BvhOperations(TupleFactory tupleFactory) {
        this.tupleFactory = tupleFactory;
    }

    public AabbImpl surroundingBox(AabbImpl box0, AabbImpl box1) {
        /*
        Tuple small = tupleFactory.vector(Math.min(box0.min().x(), box1.min().x()), Math.min(box0.min().y(), box1.min().y()),
                Math.min(box0.min().z(), box1.min().z()));
        Tuple big = tupleFactory.vector(Math.max(box0.max().x(), box1.max().x()), Math.max(box0.max().y(), box1.max().y()),
                Math.max(box0.max().z(), box1.max().z()));
        return new AabbImpl(small, big, this, tupleFactory);
        */
         return null;
    }

}
