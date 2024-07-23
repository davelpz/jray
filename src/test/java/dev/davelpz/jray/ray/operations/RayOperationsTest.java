package dev.davelpz.jray.ray.operations;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.shape.Sphere;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RayOperationsTest {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;

    @Before
    public void setUp() {
        injector = Guice.createInjector(new RayTracerModule());
        rayOperations = injector.getInstance(RayOperations.class);
        intersectionFactory = injector.getInstance(IntersectionFactory.class);
        tupleFactory = injector.getInstance(TupleFactory.class);
        rayFactory = injector.getInstance(RayFactory.class);
        sphereFactory = injector.getInstance(SphereFactory.class);
        matrixOperations = injector.getInstance(MatrixOperations.class);
    }

    @Test
    public void testIntersect() {
        Tuple origin = tupleFactory.point(0, 0, -5);
        Tuple direction = tupleFactory.vector(0, 0, 1);
        Ray ray = rayFactory.create(origin, direction);
        Sphere sphere = sphereFactory.create();
        List<Intersection> result = rayOperations.intersect(sphere, ray);
        assertEquals(2, result.size());
        assertEquals(4.0, result.get(0).t(), Constants.EPSILON);
        assertEquals(6.0, result.get(1).t(), Constants.EPSILON);
        assertEquals(sphere, result.get(0).shape());
        assertEquals(sphere, result.get(1).shape());

        origin = tupleFactory.point(0, 1, -5);
        ray = rayFactory.create(origin, direction);
        result = rayOperations.intersect(sphere, ray);
        assertEquals(2, result.size());
        assertEquals(5.0, result.get(0).t(), Constants.EPSILON);
        assertEquals(5.0, result.get(1).t(), Constants.EPSILON);

        origin = tupleFactory.point(0, 2, -5);
        ray = rayFactory.create(origin, direction);
        result = rayOperations.intersect(sphere, ray);
        assertEquals(0, result.size());

        origin = tupleFactory.point(0, 0, 0);
        ray = rayFactory.create(origin, direction);
        result = rayOperations.intersect(sphere, ray);
        assertEquals(2, result.size());
        assertEquals(-1.0, result.get(0).t(), Constants.EPSILON);
        assertEquals(1.0, result.get(1).t(), Constants.EPSILON);

        origin = tupleFactory.point(0, 0, 5);
        ray = rayFactory.create(origin, direction);
        result = rayOperations.intersect(sphere, ray);
        assertEquals(2, result.size());
        assertEquals(-6.0, result.get(0).t(), Constants.EPSILON);
        assertEquals(-4.0, result.get(1).t(), Constants.EPSILON);
    }

    @Test
    public void testHit() {
        Sphere s = sphereFactory.create();
        Intersection i1 = intersectionFactory.create(1.0f, s);
        Intersection i2 = intersectionFactory.create(2.0f, s);
        List<Intersection> xs = List.of(i2, i1 );
        Optional<Intersection> i = rayOperations.hit(xs);
        assertTrue(i.isPresent());
        assertEquals(i1, i.get());

        i1 = intersectionFactory.create(-1.0f, s);
        i2 = intersectionFactory.create(1.0f, s);
        xs = List.of(i2, i1 );
        i = rayOperations.hit(xs);
        assertTrue(i.isPresent());
        assertEquals(i2, i.get());

        i1 = intersectionFactory.create(-2.0f, s);
        i2 = intersectionFactory.create(-1.0f, s);
        xs = List.of(i2, i1 );
        i = rayOperations.hit(xs);
        assertFalse(i.isPresent());

        i1 = intersectionFactory.create(5.0f, s);
        i2 = intersectionFactory.create(7.0f, s);
        Intersection i3 = intersectionFactory.create(-3.0f, s);
        Intersection i4 = intersectionFactory.create(2.0f, s);
        xs = List.of(i1, i2, i3, i4 );
        i = rayOperations.hit(xs);
        assertTrue(i.isPresent());
        assertEquals(i4, i.get());
    }

    @Test
    public void testTransform() {
        Tuple origin = tupleFactory.point(1, 2, 3);
        Tuple direction = tupleFactory.vector(0, 1, 0);
        Ray r = rayFactory.create(origin, direction);
        Matrix m = matrixOperations.translation(3, 4, 5);
        Ray r2 = rayOperations.transform(r, m);
        assertTrue(tupleFactory.point(4, 6, 8).equals(r2.origin()));
        assertTrue(tupleFactory.vector(0, 1, 0).equals(r2.direction()));

        origin = tupleFactory.point(1, 2, 3);
        direction = tupleFactory.vector(0, 1, 0);
        r = rayFactory.create(origin, direction);
        m = matrixOperations.scaling(2, 3, 4);
        r2 = rayOperations.transform(r, m);
        assertTrue(tupleFactory.point(2, 6, 12).equals(r2.origin()));
        assertTrue(tupleFactory.vector(0, 3, 0).equals(r2.direction()));
    }

    @Test
    public void testRaySphereIntersection() {
        Tuple origin = tupleFactory.point(0, 0, -5);
        Tuple direction = tupleFactory.vector(0, 0, 1);
        Ray r = rayFactory.create(origin, direction);
        Sphere s = sphereFactory.create();
        s.setTransform(matrixOperations.scaling(2, 2, 2));
        List<Intersection> xs = rayOperations.intersect(s, r);
        assertEquals(2, xs.size());
        assertEquals(3.0, xs.get(0).t(), Constants.EPSILON);
        assertEquals(7.0, xs.get(1).t(), Constants.EPSILON);

        origin = tupleFactory.point(0, 0, -5);
        direction = tupleFactory.vector(0, 0, 1);
        r = rayFactory.create(origin, direction);
        s = sphereFactory.create();
        s.setTransform(matrixOperations.translation(5, 0, 0));
        xs = rayOperations.intersect(s, r);
        assertEquals(0, xs.size());
    }

    @Test
    public void testPrepareComputationsOutside() {
        Ray r = rayFactory.create(tupleFactory.point(0, 0, -5), tupleFactory.vector(0, 0, 1));
        Sphere s = sphereFactory.create();
        Intersection i = intersectionFactory.create(4, s);
        Computation comps = rayOperations.prepareComputations(i, r, List.of(i));
        assertEquals(i.t(), comps.t(), Constants.EPSILON);
        assertEquals(i.shape(), comps.shape());
        assertTrue(tupleFactory.point(0, 0, -1).equals(comps.point()));
        assertTrue(tupleFactory.vector(0, 0, -1).equals(comps.eyeV()));
        assertTrue(tupleFactory.vector(0, 0, -1).equals(comps.normalV()));
        assertFalse(comps.inside());
    }

    @Test
    public void testPrepareComputationsInside() {
        Ray r = rayFactory.create(tupleFactory.point(0, 0, 0), tupleFactory.vector(0, 0, 1));
        Sphere s = sphereFactory.create();
        Intersection i = intersectionFactory.create(1, s);
        Computation comps = rayOperations.prepareComputations(i, r, List.of(i));
        assertEquals(i.t(), comps.t(), Constants.EPSILON);
        assertEquals(i.shape(), comps.shape());
        assertTrue(tupleFactory.point(0, 0, 1).equals(comps.point()));
        assertTrue(tupleFactory.vector(0, 0, -1).equals(comps.eyeV()));
        assertTrue(comps.inside());
        assertTrue(tupleFactory.vector(0, 0, -1).equals(comps.normalV()));
    }

    @Test
    public void testPrepareComputationsOverPoint() {
        Ray r = rayFactory.create(tupleFactory.point(0, 0, -5), tupleFactory.vector(0, 0, 1));
        Sphere s = sphereFactory.create();
        s.setTransform(matrixOperations.translation(0, 0, 1));
        Intersection i = intersectionFactory.create(5, s);
        Computation comps = rayOperations.prepareComputations(i, r, List.of(i));
        assertTrue(comps.overPoint().z() < -Constants.EPSILON/2);
        assertTrue(comps.point().z() > comps.overPoint().z());
    }

}