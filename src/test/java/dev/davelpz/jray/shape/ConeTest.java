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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConeTest {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    CubeFactory cubeFactory;
    ConeFactory coneFactory;

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
        coneFactory = injector.getInstance(ConeFactory.class);
    }

    @Test
    public void testLocalIntersectHit() {
        Cone cone = coneFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        Tuple[] origins = {
                tupleFactory.point(0, 0, -5),
                tupleFactory.point(0, 0, -5),
                tupleFactory.point(1, 1, -5) };
        Tuple[] directions = {
                tupleFactory.vector(0, 0, 1),
                tupleFactory.vector(1, 1, 1),
                tupleFactory.vector(-0.5, -1, 1) };
        double[] t0s = { 5, 8.66025, 4.55006 };
        double[] t1s = { 5, 8.66025, 49.44994 };
        for (int i = 0; i < origins.length; i++) {
            Ray ray = rayFactory.create(origins[i], directions[i].normalize());
            List<Intersection> intersections = cone.intersect(ray);
            assertEquals(2, intersections.size());
            assertEquals(t0s[i], intersections.get(0).t(), 0.00001);
            assertEquals(t1s[i], intersections.get(1).t(), 0.00001);
        }
    }

    @Test
    public void testLocalIntersectHitHalf() {
        Cone cone = coneFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        Tuple[] origins = {
                tupleFactory.point(0, 0, -1)};
        Tuple[] directions = {
                tupleFactory.vector(0, 1, 1)};
        double[] t0s = { 0.35355 };
        for (int i = 0; i < origins.length; i++) {
            Ray ray = rayFactory.create(origins[i], directions[i].normalize());
            List<Intersection> intersections = cone.intersect(ray);
            assertEquals(1, intersections.size());
            assertEquals(t0s[i], intersections.get(0).t(), 0.00001);
        }
    }

    @Test
    public void testLocalIntersectHitEndCap() {
        Cone cone = coneFactory.create(-0.5, 0.5, true);
        Tuple[] origins = {
                tupleFactory.point(0, 0, -5),
                tupleFactory.point(0, 0, -0.25),
                tupleFactory.point(0, 0, -0.25) };
        Tuple[] directions = {
                tupleFactory.vector(0, 1, 0),
                tupleFactory.vector(0, 1, 1),
                tupleFactory.vector(0, 1, 0) };
        int[] counts = { 0, 1, 4 };
        for (int i = 0; i < origins.length; i++) {
            Ray ray = rayFactory.create(origins[i], directions[i].normalize());
            List<Intersection> intersections = cone.intersect(ray);
            assertEquals(counts[i], intersections.size());
        }
    }

    @Test
    public void testLocalNormal() {
        Cone cone = coneFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        Tuple[] points = {
                tupleFactory.point(0, 0, 0),
                tupleFactory.point(1, 1, 1),
                tupleFactory.point(-1, -1, 0)};
        Tuple[] normals = {
                tupleFactory.vector(0, 0, 0),
                tupleFactory.vector(1, -Math.sqrt(2), 1),
                tupleFactory.vector(-1, 1, 0)};
        for (int i = 0; i < points.length; i++) {
            Tuple normal = cone.localNormalAt(points[i]);
            assertTrue(normals[i].equals(normal));
        }
    }

    @Test
    public void testMinMax() {
        Cone cone = coneFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        assertEquals(Double.NEGATIVE_INFINITY, cone.minimum(), 0.00001);
        assertEquals(Double.POSITIVE_INFINITY, cone.maximum(), 0.00001);
    }

    @Test
    public void testClosed() {
        Cone cone = coneFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
        assertFalse(cone.closed());
    }
}