package dev.davelpz.jray.canvas.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.canvas.Canvas;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;

import java.util.ArrayList;
import java.util.List;

public class AntiAliasCanvas implements Canvas {
    private final int width;
    private final int height;
    List<Color>[][] pixels;
    private final ColorFactory colorFactory;

    @Inject
    public AntiAliasCanvas(@Assisted("width") int width, @Assisted("height") int height, ColorFactory colorFactory) {
        this.width = width;
        this.height = height;
        this.colorFactory = colorFactory;
        pixels = new ArrayList[width][height];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                pixels[x][y] = new ArrayList<>();
            }
        }
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public void writePixel(int x, int y, Color color) {
        pixels[x][y].add(color);
    }

    @Override
    public Color pixelAt(int x, int y) {
        Color result = colorFactory.create(0, 0, 0);
        for (Color color : pixels[x][y]) {
            result = result.add(color);
        }
        return (pixels[x][y].isEmpty()) ? result : result.divide(pixels[x][y].size());
    }
}
