package dev.davelpz.jray.material;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.light.LightSourceFactory;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MaterialTest {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    LightSourceFactory lightSourceFactory;
    ColorFactory colorFactory;

    @Before
    public void setUp() {
        injector =  Guice.createInjector(new RayTracerModule());
        rayOperations = injector.getInstance(RayOperations.class);
        intersectionFactory = injector.getInstance(IntersectionFactory.class);
        tupleFactory = injector.getInstance(TupleFactory.class);
        rayFactory = injector.getInstance(RayFactory.class);
        sphereFactory = injector.getInstance(SphereFactory.class);
        matrixOperations = injector.getInstance(MatrixOperations.class);
        lightSourceFactory = injector.getInstance(LightSourceFactory.class);
        colorFactory = injector.getInstance(ColorFactory.class);
    }

    @Test
    public void testDefault() {
        Material material = injector.getInstance(Key.get(Material.class, Names.named("defaultMaterial")));
        assertTrue(colorFactory.create(1f, 1f, 1f).equals(material.color()));
        assertEquals(0.1f, material.ambient(), Constants.EPSILON);
        assertEquals(0.9f, material.diffuse(), Constants.EPSILON);
        assertEquals(0.9f, material.specular(), Constants.EPSILON);
        assertEquals(200.0f, material.shininess(), Constants.EPSILON);
        assertEquals(0.0f, material.reflectivity(), Constants.EPSILON);
        assertEquals(0.0f, material.transparency(), Constants.EPSILON);
        assertEquals(1.0f, material.refractiveIndex(), Constants.EPSILON);
    }
}