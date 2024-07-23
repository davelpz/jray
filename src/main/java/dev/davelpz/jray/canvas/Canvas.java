package dev.davelpz.jray.canvas;

import dev.davelpz.jray.color.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Canvas interface models the surface where the image is drawn
 * and provides methods to write pixels and save the image to a file
 */
public interface Canvas {
    int width();

    int height();

    void writePixel(int x, int y, Color color);

    Color pixelAt(int x, int y);

    default String toPPM() {
        StringBuilder sb = new StringBuilder();
        sb.append("P3\n");
        sb.append(width()).append(" ").append(height()).append("\n");
        sb.append("255\n");
        for (int y = 0; y < height(); y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < width(); x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                Color color = pixelAt(x, y);
                if (color != null) {
                    r = clamp((int) (color.red() * 255));
                    g = clamp((int) (color.green() * 255));
                    b = clamp((int) (color.blue() * 255));
                }

                String rStr = String.valueOf(r);
                String gStr = String.valueOf(g);
                String bStr = String.valueOf(b);

                if ((line.length() + rStr.length()) >= 70) {
                    sb.append(line).append("\n");
                    line = new StringBuilder();
                }
                line.append(rStr).append(" ");

                if ((line.length() + gStr.length()) >= 70) {
                    sb.append(line).append("\n");
                    line = new StringBuilder();
                }
                line.append(gStr).append(" ");

                if ((line.length() + bStr.length()) >= 70) {
                    sb.append(line).append("\n");
                    line = new StringBuilder();
                }
                line.append(bStr).append(" ");
            }
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    default boolean saveToFile(String filename) {
        BufferedImage image = toBufferedImage();
        File file = new File(filename);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    default BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(width(), height(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height(); y++) {
            int[] row = new int[width()];
            for (int x = 0; x < width(); x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                Color color = pixelAt(x, y);
                if (color != null) {
                    r = clamp((int) (color.red() * 255));
                    g = clamp((int) (color.green() * 255));
                    b = clamp((int) (color.blue() * 255));
                }
                int rgb = (r << 16) | (g << 8) | b;
                row[x] = rgb;
            }
            image.setRGB(0, y, width(), 1, row, 0, width());
        }
        return image;
    }

    default int clamp(int value) {
        if (value < 0) {
            return 0;
        }
        return Math.min(value, 255);
    }

}
