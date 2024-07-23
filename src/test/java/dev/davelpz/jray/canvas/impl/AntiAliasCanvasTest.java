package dev.davelpz.jray.canvas.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.davelpz.jray.canvas.Canvas;
import dev.davelpz.jray.canvas.CanvasFactory;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.config.RayTracerModule;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AntiAliasCanvasTest {
    Injector injector;
    ColorFactory colorFactory;
    CanvasFactory canvasFactory;
    @Before
    public void setUp() {
        injector =  Guice.createInjector(new RayTracerModule());
        colorFactory = injector.getInstance(ColorFactory.class);
        canvasFactory = injector.getInstance(CanvasFactory.class);
    }

    @Test
    public void testCanvas() {
        int hSize = 100;
        int vSize = 100;
        Canvas canvas = canvasFactory.create(hSize, vSize);
        Color black = colorFactory.create(0, 0, 0);
        assertEquals(hSize, canvas.height());
        assertEquals(vSize, canvas.width());
        for(int i = 0; i < hSize; i++) {
            for(int j = 0; j < vSize; j++) {
                assertTrue(black.equals(canvas.pixelAt(i, j)));
            }
        }
        canvas.writePixel(0, 0, colorFactory.create(1, 0, 0));
        assertTrue(colorFactory.create(1, 0, 0).equals(canvas.pixelAt(0, 0)));
        canvas.writePixel(0, 0, colorFactory.create(1, 1, 0));
        assertTrue(colorFactory.create(1, 0.5, 0).equals(canvas.pixelAt(0, 0)));
        canvas.writePixel(0, 0, colorFactory.create(1, 1, 1));
        assertTrue(colorFactory.create(1, 2.0/3.0, 1.0/3.0).equals(canvas.pixelAt(0, 0)));
    }

}