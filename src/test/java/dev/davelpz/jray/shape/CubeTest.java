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
import static org.junit.Assert.assertTrue;

public class CubeTest {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    CubeFactory cubeFactory;

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
    }

    @Test
    public void testRayIntersectsCube() {
        Cube cube = cubeFactory.create();
        Tuple[] origins = {
                tupleFactory.point(5, 0.5, 0),
                tupleFactory.point(-5, 0.5, 0),
                tupleFactory.point(0.5, 5, 0),
                tupleFactory.point(0.5, -5, 0),
                tupleFactory.point(0.5, 0, 5),
                tupleFactory.point(0.5, 0, -5),
                tupleFactory.point(0, 0.5, 0) };
        Tuple[] directions = {
                tupleFactory.vector(-1, 0, 0),
                tupleFactory.vector(1, 0, 0),
                tupleFactory.vector(0, -1, 0),
                tupleFactory.vector(0, 1, 0),
                tupleFactory.vector(0, 0, -1),
                tupleFactory.vector(0, 0, 1),
                tupleFactory.vector(0, 0, 1) };

        double[] t1s = { 4, 4, 4, 4, 4, 4, -1 };
        double[] t2s = { 6, 6, 6, 6, 6, 6, 1 };

        for (int i = 0; i < origins.length; i++) {
            Ray ray = rayFactory.create(origins[i], directions[i]);
            List<Intersection> intersections = cube.intersect(ray);
            assertEquals(2, intersections.size());
            assertEquals(t1s[i], intersections.get(0).t(), 0.00001);
            assertEquals(t2s[i], intersections.get(1).t(), 0.00001);
        }
    }

    @Test
    public void testRayMissesCube() {
        Cube cube = cubeFactory.create();
        Tuple[] origins = {
                tupleFactory.point(-2, 0, 0),
                tupleFactory.point(0, -2, 0),
                tupleFactory.point(0, 0, -2),
                tupleFactory.point(2, 0, 2),
                tupleFactory.point(0, 2, 2),
                tupleFactory.point(2, 2, 0)};
        Tuple[] directions = {
                tupleFactory.vector(0.2673, 0.5345, 0.8018),
                tupleFactory.vector(0.8018, 0.2673, 0.5345),
                tupleFactory.vector(0.5345, 0.8018, 0.2673),
                tupleFactory.vector(0, 0, -1),
                tupleFactory.vector(0, -1, 0),
                tupleFactory.vector(-1, 0, 0) };


        for (int i = 0; i < origins.length; i++) {
            Ray ray = rayFactory.create(origins[i], directions[i]);
            List<Intersection> intersections = cube.intersect(ray);
            assertEquals(0, intersections.size());
        }
    }

    @Test
    public void testNormal() {
        Cube cube = cubeFactory.create();
        Tuple[] points = {
                tupleFactory.point(1, 0.5, -0.8),
                tupleFactory.point(-1, -0.2, 0.9),
                tupleFactory.point(-0.4, 1, -0.1),
                tupleFactory.point(0.3, -1, -0.7),
                tupleFactory.point(-0.6, 0.3, 1),
                tupleFactory.point(0.4, 0.4, -1),
                tupleFactory.point(1, 1, 1),
                tupleFactory.point(-1, -1, -1) };
        Tuple[] normals = {
                tupleFactory.vector(1, 0, 0),
                tupleFactory.vector(-1, 0, 0),
                tupleFactory.vector(0, 1, 0),
                tupleFactory.vector(0, -1, 0),
                tupleFactory.vector(0, 0, 1),
                tupleFactory.vector(0, 0, -1),
                tupleFactory.vector(1, 0, 0),
                tupleFactory.vector(-1, 0, 0) };

        for (int i = 0; i < points.length; i++) {
            Tuple normal = cube.localNormalAt(points[i]);
            assertTrue(normals[i].equals(normal));
        }
    }
}