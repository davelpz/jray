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

public class CylinderTest {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    CubeFactory cubeFactory;
    CylinderFactory cylinderFactory;

    @Before
    public void setUp() {
        injector = Guice.createInjector(new RayTracerModule());
        rayOperations = injector.getInstance(RayOperations.class);
        intersectionFactory = injector.getInstance(IntersectionFactory.class);
        tupleFactory = injector.getInstance(TupleFactory.class);
        rayFactory = injector.getInstance(RayFactory.class);
        sphereFactory = injector.getInstance(SphereFactory.class);
        matrixOperations = injector.getInstance(MatrixOperations.class);
        cubeFactory = injector.getInstance(CubeFactory.class);
        cylinderFactory = injector.getInstance(CylinderFactory.class);
    }

    @Test
    public void testLocalIntersectMiss() {
        Cylinder cylinder = cylinderFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        Tuple[] origins = {
                tupleFactory.point(1, 0, 0),
                tupleFactory.point(0, 0, 0),
                tupleFactory.point(0, 0, -5) };
        Tuple[] directions = {
                tupleFactory.vector(0, 1, 0),
                tupleFactory.vector(0, 1, 0),
                tupleFactory.vector(1, 1, 1) };
        for (int i = 0; i < origins.length; i++) {
            Ray ray = rayFactory.create(origins[i], directions[i]);
            List<Intersection> intersections = cylinder.intersect(ray);
            assertEquals(0, intersections.size());
        }
    }

    @Test
    public void testLocalIntersectHit() {
        Cylinder cylinder = cylinderFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        Tuple[] origins = {
                tupleFactory.point(1, 0, -5),
                tupleFactory.point(0, 0, -5),
                tupleFactory.point(0.5, 0, -5) };
        Tuple[] directions = {
                tupleFactory.vector(0, 0, 1).normalize(),
                tupleFactory.vector(0, 0, 1).normalize(),
                tupleFactory.vector(0.1, 1, 1).normalize() };
        double[] t0s = { 5, 4, 6.80798 };
        double[] t1s = { 5, 6, 7.08872 };
        for (int i = 0; i < origins.length; i++) {
            Ray ray = rayFactory.create(origins[i], directions[i]);
            List<Intersection> intersections = cylinder.intersect(ray);
            assertEquals(2, intersections.size());
            assertEquals(t0s[i], intersections.get(0).t(), 0.00001);
            assertEquals(t1s[i], intersections.get(1).t(), 0.00001);
        }
    }

    @Test
    public void testLocalNormal() {
        Cylinder cylinder = cylinderFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        Tuple[] points = {
                tupleFactory.point(1, 0, 0),
                tupleFactory.point(0, 5, -1),
                tupleFactory.point(0, -2, 1),
                tupleFactory.point(-1, 1, 0) };
        Tuple[] normals = {
                tupleFactory.vector(1, 0, 0),
                tupleFactory.vector(0, 0, -1),
                tupleFactory.vector(0, 0, 1),
                tupleFactory.vector(-1, 0, 0) };
        for (int i = 0; i < points.length; i++) {
            Tuple normal = cylinder.normalAt(points[i]);
            assertTrue(normals[i].equals(normal));
        }
    }

    @Test
    public void testMinMax() {
        Cylinder cylinder = cylinderFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        assertEquals(Double.NEGATIVE_INFINITY, cylinder.minimum(), 0.00001);
        assertEquals(Double.POSITIVE_INFINITY, cylinder.maximum(), 0.00001);
    }

    @Test
    public void testContrainedCylinder() {
        Cylinder cylinder = cylinderFactory.create(1, 2, false);
        Tuple[] origins = {
                tupleFactory.point(0, 1.5, 0),
                tupleFactory.point(0, 3, -5),
                tupleFactory.point(0, 0, -5),
                tupleFactory.point(0, 2, -5),
                tupleFactory.point(0, 1, -5),
                tupleFactory.point(0, 1.5, -2) };
        Tuple[] directions = {
                tupleFactory.vector(0.1, 1, 0),
                tupleFactory.vector(0, 0, 1),
                tupleFactory.vector(0, 0, 1),
                tupleFactory.vector(0, 0, 1),
                tupleFactory.vector(0, 0, 1),
                tupleFactory.vector(0, 0, 1) };
        int[] counts = { 0, 0, 0, 0, 0, 2 };
        for (int i = 0; i < origins.length; i++) {
            Ray ray = rayFactory.create(origins[i], directions[i].normalize());
            List<Intersection> intersections = cylinder.intersect(ray);
            assertEquals(counts[i], intersections.size());
        }
    }

    @Test
    public void testClosed() {
        Cylinder cylinder = cylinderFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        assertFalse(cylinder.closed());
    }

    @Test
    public void testContrainedCylinderClosed() {
        Cylinder cylinder = cylinderFactory.create(1, 2, true);
        Tuple[] origins = {
                tupleFactory.point(0, 3, 0),
                tupleFactory.point(0, 3, -2),
                tupleFactory.point(0, 4, -2),
                tupleFactory.point(0, 0, -2),
                tupleFactory.point(0, -1, -2) };
        Tuple[] directions = {
                tupleFactory.vector(0, -1, 0),
                tupleFactory.vector(0, -1, 2),
                tupleFactory.vector(0, -1, 1),
                tupleFactory.vector(0, 1, 2),
                tupleFactory.vector(0, 1, 1)
        };
        int[] counts = { 2, 2, 2, 2, 2 };
        for (int i = 0; i < origins.length; i++) {
            Ray ray = rayFactory.create(origins[i], directions[i].normalize());
            List<Intersection> intersections = cylinder.intersect(ray);
            assertEquals(counts[i], intersections.size());
        }
    }

    @Test
    public void testLocalNormalClosed() {
        Cylinder cylinder = cylinderFactory.create(1, 2, true);
        Tuple[] points = {
                tupleFactory.point(0, 1, 0),
                tupleFactory.point(0.5, 1, 0),
                tupleFactory.point(0, 1, 0.5),
                tupleFactory.point(0, 2, 0),
                tupleFactory.point(0.5, 2, 0),
                tupleFactory.point(0, 2, 0.5) };
        Tuple[] normals = {
                tupleFactory.vector(0, -1, 0),
                tupleFactory.vector(0, -1, 0),
                tupleFactory.vector(0, -1, 0),
                tupleFactory.vector(0, 1, 0),
                tupleFactory.vector(0, 1, 0),
                tupleFactory.vector(0, 1, 0) };
        for (int i = 0; i < points.length; i++) {
            Tuple normal = cylinder.normalAt(points[i]);
            assertTrue(normals[i].equals(normal));
        }
    }
}