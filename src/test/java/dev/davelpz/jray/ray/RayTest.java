package dev.davelpz.jray.ray;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class RayTest {

    Injector injector;
    RayFactory rayFactory;
    TupleFactory tupleFactory;

    @Before
    public void setUp() {
        injector =  Guice.createInjector(new RayTracerModule());
        rayFactory = injector.getInstance(RayFactory.class);
        tupleFactory = injector.getInstance(TupleFactory.class);
    }

    @Test
    public void testOriginAndDirection() {
        Tuple origin = tupleFactory.point(1.0f, 2.0f, 3.0f);
        Tuple direction = tupleFactory.vector(4.0f, 5.0f, 6.0f);
        Ray ray = rayFactory.create(origin, direction);
        Tuple expected = tupleFactory.point(1.0f, 2.0f, 3.0f);
        Tuple result = ray.origin();
        assertTrue(expected.equals(result));
        expected = tupleFactory.vector(4.0f, 5.0f, 6.0f);
        result = ray.direction();
        assertTrue(expected.equals(result));
    }

    @Test
    public void testPosition() {
        Ray ray = rayFactory.create(tupleFactory.point(2.0f, 3.0f, 4.0f), tupleFactory.vector(1.0f, 0.0f, 0.0f));
        Tuple expected = tupleFactory.point(2.0f, 3.0f, 4.0f);
        Tuple result = ray.position(0.0f);
        assertTrue(expected.equals(result));
        expected = tupleFactory.point(3.0f, 3.0f, 4.0f);
        result = ray.position(1.0f);
        assertTrue(expected.equals(result));
        expected = tupleFactory.point(1.0f, 3.0f, 4.0f);
        result = ray.position(-1.0f);
        assertTrue(expected.equals(result));
        expected = tupleFactory.point(4.5f, 3.0f, 4.0f);
        result = ray.position(2.5f);
        assertTrue(expected.equals(result));
    }
}