package dev.davelpz.jray.camera.impl;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class PixelStream {
    private int x;
    private int y;

    public PixelStream(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Stream<PixelStream> genStream(final int width, final int height) {

        Predicate<PixelStream> hasNext = p -> (p.x != 0 || p.y != height);

        UnaryOperator<PixelStream> next = p -> {
            int tx = p.x;
            int ty = p.y;

            if (tx < (width - 1)) {
                tx++;
            } else {
                tx = 0;
                ty++;
            }

            return new PixelStream(tx, ty);
        };

        return Stream.iterate(new PixelStream(0, 0), hasNext, next);
    }

    public String toString() {
        return "PixelStream(" + x + "," + y + ")";
    }
}