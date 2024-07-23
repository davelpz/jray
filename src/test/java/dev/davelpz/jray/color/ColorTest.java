package dev.davelpz.jray.color;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.config.RayTracerModule;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ColorTest {
    Injector injector;
    ColorFactory factory;

    @Before
    public void setUp() {
        injector =  Guice.createInjector(new RayTracerModule());
        factory = injector.getInstance(ColorFactory.class);
    }

    @Test
    public void red() {
        Color color = factory.create(1.0f, 0.0f, 0.0f);
        assertEquals(1.0f, color.red(), Constants.EPSILON);
    }

    @Test
    public void green() {
        Color color = factory.create(0.0f, 1.0f, 0.0f);
        assertEquals(1.0f, color.green(), Constants.EPSILON);
    }

    @Test
    public void blue() {
        Color color = factory.create(0.0f, 0.0f, 1.0f);
        assertEquals(1.0f, color.blue(), Constants.EPSILON);
    }

    @Test
    public void add() {
        Color color1 = factory.create(0.9f, 0.6f, 0.75f);
        Color color2 = factory.create(0.7f, 0.1f, 0.25f);
        Color expected = factory.create(1.6f, 0.7f, 1.0f);
        Color result = color1.add(color2);
        assertTrue(expected.equals(result));
    }

    @Test
    public void subtract() {
        Color color1 = factory.create(0.9f, 0.6f, 0.75f);
        Color color2 = factory.create(0.7f, 0.1f, 0.25f);
        Color expected = factory.create(0.2f, 0.5f, 0.5f);
        assertTrue(expected.equals(color1.subtract(color2)));
    }

    @Test
    public void multiply() {
        Color color = factory.create(0.2f, 0.3f, 0.4f);
        Color expected = factory.create(0.4f, 0.6f, 0.8f);
        assertTrue(expected.equals(color.multiply(2.0f)));
    }

    @Test
    public void testMultiply() {
        Color color1 = factory.create(1.0f, 0.2f, 0.4f);
        Color color2 = factory.create(0.9f, 1.0f, 0.1f);
        Color expected = factory.create(0.9f, 0.2f, 0.04f);
        assertTrue(expected.equals(color1.multiply(color2)));
    }
}