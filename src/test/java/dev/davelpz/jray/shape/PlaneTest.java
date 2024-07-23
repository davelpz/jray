package dev.davelpz.jray.shape;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PlaneTest {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    PlaneFactory planeFactory;

    @Before
    public void setUp() {
        injector =  Guice.createInjector(new RayTracerModule());
        rayOperations = injector.getInstance(RayOperations.class);
        intersectionFactory = injector.getInstance(IntersectionFactory.class);
        tupleFactory = injector.getInstance(TupleFactory.class);
        rayFactory = injector.getInstance(RayFactory.class);
        sphereFactory = injector.getInstance(SphereFactory.class);
        matrixOperations = injector.getInstance(MatrixOperations.class);
        planeFactory = injector.getInstance(PlaneFactory.class);
    }

    @Test
    public void testNormal() {
        Plane plane = planeFactory.create();
        Tuple n1 = plane.localNormalAt(tupleFactory.point(0, 0, 0));
        Tuple n2 = plane.localNormalAt(tupleFactory.point(10, 0, -10));
        Tuple n3 = plane.localNormalAt(tupleFactory.point(-5, 0, 150));
        assertTrue(tupleFactory.vector(0, 1, 0).equals(n1));
        assertTrue(tupleFactory.vector(0, 1, 0).equals(n2));
        assertTrue(tupleFactory.vector(0, 1, 0).equals(n3));
    }

    @Test
    public void testIntersect() {
        Plane plane = planeFactory.create();
        Ray ray = rayFactory.create(tupleFactory.point(0, 10, 0), tupleFactory.vector(0, 0, 1));
        List<Intersection> xs = plane.intersect(ray);
        assertTrue(xs.isEmpty());

        ray = rayFactory.create(tupleFactory.point(0, 0, 0), tupleFactory.vector(0, 0, 1));
        xs = plane.intersect(ray);
        assertTrue(xs.isEmpty());

        ray = rayFactory.create(tupleFactory.point(0, 1, 0), tupleFactory.vector(0, -1, 0));
        xs = plane.intersect(ray);
        assertEquals(1, xs.size());
        assertEquals(1, xs.get(0).t(), 0.00001);
        assertEquals(plane, xs.get(0).shape());

        ray = rayFactory.create(tupleFactory.point(0, -1, 0), tupleFactory.vector(0, 1, 0));
        xs = plane.intersect(ray);
        assertEquals(1, xs.size());
        assertEquals(1, xs.get(0).t(), 0.00001);
        assertEquals(plane, xs.get(0).shape());
    }
}