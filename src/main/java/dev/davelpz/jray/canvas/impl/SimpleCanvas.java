package dev.davelpz.jray.canvas.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.canvas.Canvas;
import dev.davelpz.jray.color.Color;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SimpleCanvas implements Canvas {
    private final int width;
    private final int height;
    Color[][] pixels;

    @Inject
    public SimpleCanvas(@Assisted("width") int width, @Assisted("height") int height) {
        this.width = width;
        this.height = height;
        pixels = new Color[width][height];
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
        pixels[x][y] = color;
    }

    @Override
    public Color pixelAt(int x, int y) {
        return pixels[x][y];
    }

    //@Override
    private boolean saveToFilePPM(String filename) {
        String contents = toPPM();
        File file = new File(filename);
        try (FileWriter writer = new FileWriter(file)){
            writer.write(contents);
            writer.flush();
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
