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

public class TriangleTest {

    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    CubeFactory cubeFactory;
    TriangleFactory triangleFactory;

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
        triangleFactory = injector.getInstance(TriangleFactory.class);
    }

    @Test
    public void testConstruct() {
        Triangle triangle = triangleFactory.create(tupleFactory.point(0, 1, 0), tupleFactory.point(-1, 0, 0), tupleFactory.point(1, 0, 0));
        assertTrue(tupleFactory.point(0, 1, 0).equals(triangle.p1()));
        assertTrue(tupleFactory.point(-1, 0, 0).equals(triangle.p2()));
        assertTrue(tupleFactory.point(1, 0, 0).equals(triangle.p3()));
        assertTrue(tupleFactory.vector(-1, -1, 0).equals(triangle.e1()));
        assertTrue(tupleFactory.vector(1, -1, 0).equals(triangle.e2()));
        assertTrue(tupleFactory.vector(0, 0, -1).equals(triangle.normal()));
    }

    @Test
    public void testNormal() {
        Triangle triangle = triangleFactory.create(tupleFactory.point(0, 1, 0), tupleFactory.point(-1, 0, 0), tupleFactory.point(1, 0, 0));
        Tuple n1 = triangle.localNormalAt(tupleFactory.point(0, 0.5, 0));
        Tuple n2 = triangle.localNormalAt(tupleFactory.point(-0.5, 0.75, 0));
        Tuple n3 = triangle.localNormalAt(tupleFactory.point(0.5, 0.25, 0));
        assertTrue(n1.equals(triangle.normal()));
        assertTrue(n2.equals(triangle.normal()));
        assertTrue(n3.equals(triangle.normal()));
    }

    @Test
    public void testIntersect1() {
        Triangle triangle = triangleFactory.create(tupleFactory.point(0, 1, 0), tupleFactory.point(-1, 0, 0), tupleFactory.point(1, 0, 0));
        Ray ray = rayFactory.create(tupleFactory.point(0, -1, -2), tupleFactory.vector(0, 1, 0));
        List<Intersection> xs = triangle.intersect(ray);
        assertEquals(0, xs.size());
    }

    @Test
    public void testIntersect2() {
        Triangle triangle = triangleFactory.create(tupleFactory.point(0, 1, 0), tupleFactory.point(-1, 0, 0), tupleFactory.point(1, 0, 0));
        Ray ray = rayFactory.create(tupleFactory.point(1, 1, -2), tupleFactory.vector(0, 0, 1));
        List<Intersection> xs = triangle.intersect(ray);
        assertEquals(0, xs.size());
    }

    @Test
    public void testIntersect3() {
        Triangle triangle = triangleFactory.create(tupleFactory.point(0, 1, 0), tupleFactory.point(-1, 0, 0), tupleFactory.point(1, 0, 0));
        Ray ray = rayFactory.create(tupleFactory.point(-1, 1, -2), tupleFactory.vector(0, 0, 1));
        List<Intersection> xs = triangle.intersect(ray);
        assertEquals(0, xs.size());
    }
    @Test
    public void testIntersect4() {
        Triangle triangle = triangleFactory.create(tupleFactory.point(0, 1, 0), tupleFactory.point(-1, 0, 0), tupleFactory.point(1, 0, 0));
        Ray ray = rayFactory.create(tupleFactory.point(0, -1, -2), tupleFactory.vector(0, 0, 1));
        List<Intersection> xs = triangle.intersect(ray);
        assertEquals(0, xs.size());
    }

    @Test
    public void testIntersect5() {
        Triangle triangle = triangleFactory.create(tupleFactory.point(0, 1, 0), tupleFactory.point(-1, 0, 0), tupleFactory.point(1, 0, 0));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0.5, -2), tupleFactory.vector(0, 0, 1));
        List<Intersection> xs = triangle.intersect(ray);
        assertEquals(1, xs.size());
        assertEquals(2, xs.get(0).t(), 0.00001);
    }
}