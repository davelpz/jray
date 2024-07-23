package dev.davelpz.jray.tuple;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TupleTest {
    Injector injector;
    TupleFactory factory;

    @Before
    public void setUp() {
        injector =  Guice.createInjector(new RayTracerModule());
        factory = injector.getInstance(TupleFactory.class);
    }

    @Test
    public void x() {
        Tuple tuple = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(1.0f, tuple.x(), Constants.EPSILON);
    }

    @Test
    public void y() {
        Tuple tuple = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(2.0f, tuple.y(), Constants.EPSILON);
    }

    @Test
    public void z() {
        Tuple tuple = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(3.0f, tuple.z(), Constants.EPSILON);
    }

    @Test
    public void w() {
        Tuple tuple = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(4.0f, tuple.w(), Constants.EPSILON);
    }

    @Test
    public void testEquals() {
        Tuple tuple1 = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        Tuple tuple2 = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        assertTrue(tuple1.equals(tuple2));
    }

    @Test
    public void isPoint() {
        Tuple tuple = factory.point(1.0f, 2.0f, 3.0f);
        assertTrue(tuple.isPoint());
        assertFalse(tuple.isVector());
    }

    @Test
    public void isVector() {
        Tuple tuple = factory.vector(1.0f, 2.0f, 3.0f);
        assertTrue(tuple.isVector());
        assertFalse(tuple.isPoint());
    }

    @Test
    public void add() {
        Tuple tuple1 = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        Tuple tuple2 = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        Tuple tuple3 = tuple1.add(tuple2);
        assertEquals(2.0f, tuple3.x(), Constants.EPSILON);
        assertEquals(4.0f, tuple3.y(), Constants.EPSILON);
        assertEquals(6.0f, tuple3.z(), Constants.EPSILON);
        assertEquals(8.0f, tuple3.w(), Constants.EPSILON);
    }

    @Test
    public void subtract() {
        Tuple tuple1 = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        Tuple tuple2 = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        Tuple tuple3 = tuple1.subtract(tuple2);
        assertEquals(0.0f, tuple3.x(), Constants.EPSILON);
        assertEquals(0.0f, tuple3.y(), Constants.EPSILON);
        assertEquals(0.0f, tuple3.z(), Constants.EPSILON);
        assertEquals(0.0f, tuple3.w(), Constants.EPSILON);
    }

    @Test
    public void negate() {
        Tuple tuple1 = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        Tuple tuple2 = tuple1.negate();
        assertEquals(-1.0f, tuple2.x(), Constants.EPSILON);
        assertEquals(-2.0f, tuple2.y(), Constants.EPSILON);
        assertEquals(-3.0f, tuple2.z(), Constants.EPSILON);
        assertEquals(-4.0f, tuple2.w(), Constants.EPSILON);
    }

    @Test
    public void multiply() {
        Tuple tuple1 = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        Tuple tuple2 = tuple1.multiply(2.0f);
        assertEquals(2.0f, tuple2.x(), Constants.EPSILON);
        assertEquals(4.0f, tuple2.y(), Constants.EPSILON);
        assertEquals(6.0f, tuple2.z(), Constants.EPSILON);
        assertEquals(8.0f, tuple2.w(), Constants.EPSILON);
    }

    @Test
    public void divide() {
        Tuple tuple1 = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        Tuple tuple2 = tuple1.divide(2.0f);
        assertEquals(0.5f, tuple2.x(), Constants.EPSILON);
        assertEquals(1.0f, tuple2.y(), Constants.EPSILON);
        assertEquals(1.5f, tuple2.z(), Constants.EPSILON);
        assertEquals(2.0f, tuple2.w(), Constants.EPSILON);
    }

    @Test
    public void magnitude() {
        Tuple tuple = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        assertEquals(5.4772f, tuple.magnitude(), Constants.EPSILON);
    }

    @Test
    public void normalize() {
        Tuple tuple = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        Tuple normalized = tuple.normalize();
        assertEquals(0.1826f, normalized.x(), Constants.EPSILON);
        assertEquals(0.3651f, normalized.y(), Constants.EPSILON);
        assertEquals(0.5477f, normalized.z(), Constants.EPSILON);
        assertEquals(0.7303f, normalized.w(), Constants.EPSILON);
    }

    @Test
    public void dot() {
        Tuple tuple1 = factory.create(1.0f, 2.0f, 3.0f, 4.0f);
        Tuple tuple2 = factory.create(2.0f, 3.0f, 4.0f, 5.0f);
        assertEquals(40.0f, tuple1.dot(tuple2), Constants.EPSILON);
    }

    @Test
    public void cross() {
        Tuple tuple1 = factory.vector(1.0f, 2.0f, 3.0f);
        Tuple tuple2 = factory.vector(2.0f, 3.0f, 4.0f);
        Tuple tuple3 = tuple1.cross(tuple2);
        assertEquals(-1.0f, tuple3.x(), Constants.EPSILON);
        assertEquals(2.0f, tuple3.y(), Constants.EPSILON);
        assertEquals(-1.0f, tuple3.z(), Constants.EPSILON);
        assertEquals(0.0f, tuple3.w(), Constants.EPSILON);
    }

    @Test
    public void reflect() {
        Tuple vector = factory.vector(1.0f, -1.0f, 0.0f);
        Tuple normal = factory.vector(0.0f, 1.0f, 0.0f);
        Tuple reflected = vector.reflect(normal);
        assertEquals(1.0f, reflected.x(), Constants.EPSILON);
        assertEquals(1.0f, reflected.y(), Constants.EPSILON);
        assertEquals(0.0f, reflected.z(), Constants.EPSILON);
        assertEquals(0.0f, reflected.w(), Constants.EPSILON);

        vector = factory.vector(0.0f, -1.0f, 0.0f);
        normal = factory.vector(Math.sqrt(2.0f) / 2.0f, Math.sqrt(2.0f) / 2.0f, 0.0f);
        reflected = vector.reflect(normal);
        assertEquals(1.0f, reflected.x(), Constants.EPSILON);
        assertEquals(0.0f, reflected.y(), Constants.EPSILON);
        assertEquals(0.0f, reflected.z(), Constants.EPSILON);
        assertEquals(0.0f, reflected.w(), Constants.EPSILON);
    }
}