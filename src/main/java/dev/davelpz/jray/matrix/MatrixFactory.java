package dev.davelpz.jray.matrix;

import com.google.inject.assistedinject.Assisted;

public interface MatrixFactory {
    Matrix create(@Assisted("width") int width, @Assisted("height") int height);
}
