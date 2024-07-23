package dev.davelpz.jray.shape;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SphereTest {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;

    @Before
    public void setUp() {
        injector =  Guice.createInjector(new RayTracerModule());
        rayOperations = injector.getInstance(RayOperations.class);
        intersectionFactory = injector.getInstance(IntersectionFactory.class);
        tupleFactory = injector.getInstance(TupleFactory.class);
        rayFactory = injector.getInstance(RayFactory.class);
        sphereFactory = injector.getInstance(SphereFactory.class);
        matrixOperations = injector.getInstance(MatrixOperations.class);
    }

    @Test
    public void transform() {
        Sphere sphere = sphereFactory.create();
        Matrix identity = injector.getInstance(Key.get(Matrix.class, Names.named("identityMatrix")));
        assertEquals(identity, sphere.transform());
    }

    @Test
    public void setTransform() {
        Sphere sphere = sphereFactory.create();
        Matrix t = matrixOperations.translation(2, 3, 4);
        sphere.setTransform(t);
        assertEquals(t, sphere.transform());
    }

    @Test
    public void testNormalAt() {
        Sphere sphere = sphereFactory.create();
        Tuple n = sphere.normalAt(tupleFactory.point(1, 0, 0));
        assertTrue(tupleFactory.vector(1, 0, 0).equals(n));

        n = sphere.normalAt(tupleFactory.point(0, 1, 0));
        assertTrue(tupleFactory.vector(0, 1, 0).equals(n));

        n = sphere.normalAt(tupleFactory.point(0, 0, 1));
        assertTrue(tupleFactory.vector(0, 0, 1).equals(n));

        double sqrt3over3 = Math.sqrt(3) / 3;
        n = sphere.normalAt(tupleFactory.point(sqrt3over3, sqrt3over3, sqrt3over3));
        assertTrue(tupleFactory.vector(sqrt3over3, sqrt3over3, sqrt3over3).equals(n));
        assertTrue(n.equals(n.normalize()));

        sphere.setTransform(matrixOperations.translation(0, 1, 0));
        n = sphere.normalAt(tupleFactory.point(0, 1.70711f, -0.70711f));
        assertTrue(tupleFactory.vector(0, 0.70711f, -0.70711f).equals(n));

        sphere.setTransform(matrixOperations.scaling(1, 0.5f, 1).multiply(matrixOperations.rotationZ(Math.PI / 5)));
        n = sphere.normalAt(tupleFactory.point(0, Math.sqrt(2) / 2, -(double) Math.sqrt(2) / 2));
        assertTrue(tupleFactory.vector(0, 0.97014f, -0.24254f).equals(n));
    }

    @Test
    public void material() {
        Sphere sphere = sphereFactory.create();
        Material m = sphere.material();
        Material defaultMaterial = injector.getInstance(Key.get(Material.class, Names.named("defaultMaterial")));
        assertTrue(defaultMaterial.equals(m));

        Material m2 = injector.getInstance(Key.get(Material.class, Names.named("defaultMaterial")));
        m2.setAmbient(1);
        sphere.setMaterial(m2);
        assertTrue(m2.equals(sphere.material()));
    }

    @Test
    public void testGlassSphere() {
        Sphere sphere = injector.getInstance(Key.get(Sphere.class, Names.named("glassSphere")));
        Matrix identity = injector.getInstance(Key.get(Matrix.class, Names.named("identityMatrix")));
        assertEquals(identity, sphere.transform());
        Material m = sphere.material();
        assertEquals(1.0, m.transparency(), 0.0);
        assertEquals(1.5, m.refractiveIndex(), 0.0);
    }
}