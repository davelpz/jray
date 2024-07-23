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

public class GroupTest {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    CubeFactory cubeFactory;
    ConeFactory coneFactory;
    GroupFactory groupFactory;

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
        groupFactory = injector.getInstance(GroupFactory.class);
    }

    @Test
    public void getChildren() {
        Group group = groupFactory.create();
        Shape s = sphereFactory.create();
        assertEquals(0, group.getChildrenCount());
        group.addChild(s);
        assertEquals(1, group.getChildrenCount());
        assertEquals(s, group.getChild(0));
        assertFalse(group.parent().isPresent());
        assertEquals(group, s.parent().get());
    }

    @Test
    public void testIntersectMiss() {
        Group group = groupFactory.create();
        Ray r = rayFactory.create(tupleFactory.point(0, 0, 0), tupleFactory.vector(0, 0, 1));
        List<Intersection> xs = group.intersect(r);
        assertEquals(0, xs.size());
    }

    @Test
    public void testIntersectHit() {
        Group group = groupFactory.create();
        Sphere s1 = sphereFactory.create();
        Sphere s2 = sphereFactory.create();
        s2.setTransform(matrixOperations.translation(0, 0, -3));
        Sphere s3 = sphereFactory.create();
        s3.setTransform(matrixOperations.translation(5, 0, 0));
        group.addChild(s1);
        group.addChild(s2);
        group.addChild(s3);
        Ray r = rayFactory.create(tupleFactory.point(0, 0, -5), tupleFactory.vector(0, 0, 1));
        List<Intersection> xs = group.intersect(r);
        assertEquals(4, xs.size());
        assertEquals(s2, xs.get(0).shape());
        assertEquals(s2, xs.get(1).shape());
        assertEquals(s1, xs.get(2).shape());
        assertEquals(s1, xs.get(3).shape());
    }

    @Test
    public void testWorldToObject() {
        Group g1 = groupFactory.create();
        g1.setTransform(matrixOperations.rotationY(Math.PI / 2));
        Group g2 = groupFactory.create();
        g2.setTransform(matrixOperations.scaling(2, 2, 2));
        g1.addChild(g2);
        Sphere s = sphereFactory.create();
        s.setTransform(matrixOperations.translation(5, 0, 0));
        g2.addChild(s);
        Tuple n = s.worldToObject(tupleFactory.point(-2, 0, -10));
        assertTrue(tupleFactory.point(0, 0, -1).equals(n));
    }

    @Test
    public void testNormalToWorld() {
        Group g1 = groupFactory.create();
        g1.setTransform(matrixOperations.rotationY(Math.PI / 2));
        Group g2 = groupFactory.create();
        g2.setTransform(matrixOperations.scaling(1, 2, 3));
        g1.addChild(g2);
        Sphere s = sphereFactory.create();
        s.setTransform(matrixOperations.translation(5, 0, 0));
        g2.addChild(s);
        Tuple n = s.normalToWorld(tupleFactory.point(Math.sqrt(3) / 3, Math.sqrt(3) / 3, Math.sqrt(3) / 3));
        assertTrue(tupleFactory.vector(0.2857, 0.4286, -0.8571).equals(n));
    }

    @Test
    public void testNormalAt() {
        Group g1 = groupFactory.create();
        g1.setTransform(matrixOperations.rotationY(Math.PI / 2));
        Group g2 = groupFactory.create();
        g2.setTransform(matrixOperations.scaling(1, 2, 3));
        g1.addChild(g2);
        Sphere s = sphereFactory.create();
        s.setTransform(matrixOperations.translation(5, 0, 0));
        g2.addChild(s);
        Tuple n = s.normalAt(tupleFactory.point(1.7321, 1.1547, -5.5774));
        assertTrue(tupleFactory.vector(0.2857, 0.4286, -0.8571).equals(n));
    }
}