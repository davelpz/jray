package dev.davelpz.jray.tuple;

import com.google.inject.assistedinject.Assisted;

public interface TupleFactoryBase {

    Tuple create(@Assisted("x") double x, @Assisted("y") double y, @Assisted("z") double z, @Assisted("w") double w);
}
