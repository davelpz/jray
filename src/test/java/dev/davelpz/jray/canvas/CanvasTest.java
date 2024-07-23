package dev.davelpz.jray.canvas;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.config.RayTracerModule;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CanvasTest {
    Injector injector;
    CanvasFactory factory;

    @Before
    public void setUp() {
        injector =  Guice.createInjector(new RayTracerModule());
        factory = injector.getInstance(CanvasFactory.class);
    }

    @Test
    public void width() {
        Canvas canvas = factory.create(10, 20);
        assertEquals(10, canvas.width());
    }

    @Test
    public void height() {
        Canvas canvas = factory.create(10, 20);
        assertEquals(20, canvas.height());
    }

    @Test
    public void writePixel() {
        Canvas canvas = factory.create(10, 20);
        Color color = injector.getInstance(ColorFactory.class).create(1.0f, 0.0f, 0.0f);
        canvas.writePixel(2, 3, color);
        assertTrue(color.equals(canvas.pixelAt(2, 3)));
    }

    @Test
    public void pixelAt() {
        Canvas canvas = factory.create(10, 20);
        Color color = injector.getInstance(ColorFactory.class).create(1.0f, 0.0f, 0.0f);
        canvas.writePixel(2, 3, color);
        assertTrue(color.equals(canvas.pixelAt(2, 3)));
    }

    @Test
    public void toPPM() {
        Canvas canvas = factory.create(5, 3);
        Color c1 = injector.getInstance(ColorFactory.class).create(1.5f, 0.0f, 0.0f);
        Color c2 = injector.getInstance(ColorFactory.class).create(0.0f, 0.5f, 0.0f);
        Color c3 = injector.getInstance(ColorFactory.class).create(-0.5f, 0.0f, 1.0f);
        canvas.writePixel(0, 0, c1);
        canvas.writePixel(2, 1, c2);
        canvas.writePixel(4, 2, c3);
        String ppm = canvas.toPPM();
        assertNotNull(ppm);
        assertEquals("P3\n5 3\n255\n", ppm.substring(0, 11));
        assertEquals("255 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ", ppm.substring(11, 43));
        assertEquals("0 0 0 0 0 0 0 127 0 0 0 0 0 0 0 ", ppm.substring(44, 76));
        assertEquals("0 0 0 0 0 0 0 0 0 0 0 0 0 0 255 ", ppm.substring(77, 109));

        canvas = factory.create(10, 2);
        Color darkRed = injector.getInstance(ColorFactory.class).create(1.0f, 0.8f, 0.6f);
        //set every pixel to darkRed
        for (int y = 0; y < canvas.height(); y++) {
            for (int x = 0; x < canvas.width(); x++) {
                canvas.writePixel(x, y, darkRed);
            }
        }

        ppm = canvas.toPPM();
        assertNotNull(ppm);
        assertEquals("153 255 204 153 255 204 153 255 204 153 255 204 153 ", ppm.substring(81, 133));

    }
}