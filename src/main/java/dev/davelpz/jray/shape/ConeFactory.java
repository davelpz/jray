package dev.davelpz.jray.shape;

import com.google.inject.assistedinject.Assisted;

public interface ConeFactory {
    Cone create(@Assisted("minimum") double minimum, @Assisted("maximum") double maximum, @Assisted("closed") boolean closed);
}
