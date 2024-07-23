package dev.davelpz.jray.canvas;

import com.google.inject.assistedinject.Assisted;

public interface CanvasFactory {
    Canvas create(@Assisted("width") int width, @Assisted("height") int height);
}
