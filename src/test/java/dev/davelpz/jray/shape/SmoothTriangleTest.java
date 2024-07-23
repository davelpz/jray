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
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SmoothTriangleTest {

    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    CubeFactory cubeFactory;
    SmoothTriangleFactory smoothTriangleFactory;

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
        smoothTriangleFactory = injector.getInstance(SmoothTriangleFactory.class);
    }

    @Test
    public void testConstruct() {
        SmoothTriangle triangle = smoothTriangleFactory.create(tupleFactory.point(0, 1, 0), tupleFactory.point(-1, 0, 0), tupleFactory.point(1, 0, 0),
                tupleFactory.vector(0, 1, 0), tupleFactory.vector(-1, 0, 0), tupleFactory.vector(1, 0, 0));
        assertTrue(tupleFactory.point(0, 1, 0).equals(triangle.p1()));
        assertTrue(tupleFactory.point(-1, 0, 0).equals(triangle.p2()));
        assertTrue(tupleFactory.point(1, 0, 0).equals(triangle.p3()));
        assertTrue(tupleFactory.vector(0, 1, 0).equals(triangle.n1()));
        assertTrue(tupleFactory.vector(-1, 0, 0).equals(triangle.n2()));
        assertTrue(tupleFactory.vector(1, 0, 0).equals(triangle.n3()));
    }

    @Test
    public void testNormal() {
    }

    @Test
    public void testIntersect1() {
        SmoothTriangle triangle = smoothTriangleFactory.create(tupleFactory.point(0, 1, 0), tupleFactory.point(-1, 0, 0), tupleFactory.point(1, 0, 0),
                tupleFactory.vector(0, 1, 0), tupleFactory.vector(-1, 0, 0), tupleFactory.vector(1, 0, 0));
        Ray ray = rayFactory.create(tupleFactory.point(-0.2, 0.3, -2), tupleFactory.vector(0, 0, 1));
        List<Intersection> intersections = triangle.intersect(ray);
        assertEquals(1, intersections.size());
        assertEquals(0.45, intersections.get(0).u(), 0.0001);
        assertEquals(0.25, intersections.get(0).v(), 0.0001);
    }

    @Test
    public void testIntersect2() {
    }

    @Test
    public void testIntersect3() {
    }
    @Test
    public void testIntersect4() {
    }

    @Test
    public void testIntersect5() {
    }
}