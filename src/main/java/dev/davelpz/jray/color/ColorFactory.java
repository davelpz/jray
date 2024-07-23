package dev.davelpz.jray.color;

import com.google.inject.assistedinject.Assisted;

public interface ColorFactory {
    Color create(@Assisted("red") double red, @Assisted("green") double green, @Assisted("blue") double blue);
}
