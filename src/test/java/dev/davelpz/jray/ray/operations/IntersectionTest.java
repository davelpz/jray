package dev.davelpz.jray.ray.operations;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.shape.Sphere;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IntersectionTest {
    Injector injector;
    RayOperations rayOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;

    @Before
    public void setUp() {
        injector =  Guice.createInjector(new RayTracerModule());
        rayOperations = injector.getInstance(RayOperations.class);
        tupleFactory = injector.getInstance(TupleFactory.class);
        rayFactory = injector.getInstance(RayFactory.class);
        sphereFactory = injector.getInstance(SphereFactory.class);
        intersectionFactory = injector.getInstance(IntersectionFactory.class);
        matrixOperations = injector.getInstance(MatrixOperations.class);
    }

    @Test
    public void testTAndSphere() {
        double t = 3.5f;
        Sphere sphere = sphereFactory.create();
        Intersection intersection = intersectionFactory.create(t, sphere);
        assertEquals(intersection.t(), t, Constants.EPSILON);
        assertEquals(intersection.shape(), sphere);
    }

    @Test
    public void testConstruct() {
        double t = 3.5f;
        Sphere sphere = sphereFactory.create();
        Intersection intersection = intersectionFactory.create(t, sphere,0.2,0.4);
        assertEquals(intersection.t(), t, Constants.EPSILON);
        assertEquals(intersection.shape(), sphere);
        assertEquals(0.2, intersection.u(), Constants.EPSILON);
        assertEquals(0.4, intersection.v(), Constants.EPSILON);
    }

    @Test
    public void testReflection() {
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -1), tupleFactory.vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Sphere sphere = sphereFactory.create();
        Intersection intersection = intersectionFactory.create(Math.sqrt(2), sphere);
        Computation computation = rayOperations.prepareComputations(intersection, ray, List.of(intersection));
        assertTrue(computation.reflectV().equals(tupleFactory.vector(0, Math.sqrt(2)/2, Math.sqrt(2)/2)));
    }

    @Test
    public void testN1N2() {
        Sphere sphereA = injector.getInstance(Key.get(Sphere.class, Names.named("glassSphere")));
        sphereA.setTransform(matrixOperations.scaling(2, 2, 2));
        sphereA.material().setRefractiveIndex(1.5);
        Sphere sphereB = injector.getInstance(Key.get(Sphere.class, Names.named("glassSphere")));
        sphereB.setTransform(matrixOperations.translation(0, 0, -0.25));
        sphereB.material().setRefractiveIndex(2.0);
        Sphere sphereC = injector.getInstance(Key.get(Sphere.class, Names.named("glassSphere")));
        sphereC.setTransform(matrixOperations.translation(0, 0, 0.25));
        sphereC.material().setRefractiveIndex(2.5);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -4), tupleFactory.vector(0, 0, 1));
        List<Intersection> xs = new ArrayList<>();
        xs.add(intersectionFactory.create(2, sphereA));
        xs.add(intersectionFactory.create(2.75, sphereB));
        xs.add(intersectionFactory.create(3.25, sphereC));
        xs.add(intersectionFactory.create(4.75, sphereB));
        xs.add(intersectionFactory.create(5.25, sphereC));
        xs.add(intersectionFactory.create(6, sphereA));

        double[] expectedN1 = {1.0, 1.5, 2.0, 2.5, 2.5, 1.5};
        double[] expectedN2 = {1.5, 2.0, 2.5, 2.5, 1.5, 1.0};

        int index = 0;
        for (Intersection intersection: xs) {
            Computation computation = rayOperations.prepareComputations(intersection, ray, xs);
            assertEquals(expectedN1[index], computation.n1(), Constants.EPSILON);
            assertEquals(expectedN2[index], computation.n2(), Constants.EPSILON);
            index++;
        }
    }

    @Test
    public void testUnderPoint() {
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -5), tupleFactory.vector(0, 0, 1));
        Sphere sphere = sphereFactory.create();
        sphere.setTransform(matrixOperations.translation(0, 0, 1));
        Intersection intersection = intersectionFactory.create(5, sphere);
        Computation computation = rayOperations.prepareComputations(intersection, ray, List.of(intersection));
        assertTrue(computation.underPoint().z() > Constants.EPSILON/2);
        assertTrue(computation.point().z() < computation.underPoint().z());
    }

    @Test
    public void testSchlick1() {
        Sphere sphere = injector.getInstance(Key.get(Sphere.class, Names.named("glassSphere")));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, Math.sqrt(2)/2), tupleFactory.vector(0, 1, 0));
        List<Intersection> xs = new ArrayList<>();
        xs.add(intersectionFactory.create(-Math.sqrt(2)/2, sphere));
        xs.add(intersectionFactory.create(Math.sqrt(2)/2, sphere));
        Computation computation = rayOperations.prepareComputations(xs.get(1), ray, xs);
        assertEquals(1.0, computation.schlick(), Constants.EPSILON);
    }

    @Test
    public void testSchlick2() {
        Sphere sphere = injector.getInstance(Key.get(Sphere.class, Names.named("glassSphere")));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, 0), tupleFactory.vector(0, 1, 0));
        List<Intersection> xs = List.of(intersectionFactory.create(-1, sphere), intersectionFactory.create(1, sphere));
        Computation computation = rayOperations.prepareComputations(xs.get(1), ray, xs);
        assertEquals(0.04, computation.schlick(), Constants.EPSILON);
    }

    @Test
    public void testSchlick3() {
        Sphere sphere = injector.getInstance(Key.get(Sphere.class, Names.named("glassSphere")));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0.99, -2), tupleFactory.vector(0, 0, 1));
        List<Intersection> xs = List.of(intersectionFactory.create(1.8589, sphere));
        Computation computation = rayOperations.prepareComputations(xs.get(0), ray, xs);
        assertEquals(0.48873, computation.schlick(), Constants.EPSILON);
    }

}