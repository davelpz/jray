package dev.davelpz.jray.light.operations;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.light.LightSource;
import dev.davelpz.jray.light.LightSourceFactory;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.material.MaterialFactory;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.shape.Sphere;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LightOperationsTest {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    LightSourceFactory lightSourceFactory;
    ColorFactory colorFactory;
    MaterialFactory materialFactory;
    LightOperations lightOperations;
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
        materialFactory = injector.getInstance(MaterialFactory.class);
        lightOperations = injector.getInstance(LightOperations.class);
    }

    @Test
    public void testLighting1() {
        Sphere sphere = sphereFactory.create();
        Material material = injector.getInstance(Key.get(Material.class, Names.named("defaultMaterial")));
        Tuple position = tupleFactory.point(0, 0, 0);
        Tuple eyev = tupleFactory.vector(0, 0, -1);
        Tuple normalv = tupleFactory.vector(0, 0, -1);
        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(0, 0, -10), colorFactory.create(1, 1, 1));
        Color result = lightOperations.lighting(material, sphere, light, position, eyev, normalv, false);
        Color expected = colorFactory.create(1.9f, 1.9f, 1.9f);
        assertTrue(result.equals(expected));
    }

    @Test
    public void testLighting2() {
        Sphere sphere = sphereFactory.create();
        Material material = injector.getInstance(Key.get(Material.class, Names.named("defaultMaterial")));
        Tuple position = tupleFactory.point(0, 0, 0);
        Tuple eyev = tupleFactory.vector(0f, Math.sqrt(2)/2, -Math.sqrt(2)/2);
        Tuple normalv = tupleFactory.vector(0, 0, -1);
        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(0, 0, -10), colorFactory.create(1, 1, 1));
        Color result = lightOperations.lighting(material, sphere, light, position, eyev, normalv, false);
        Color expected = colorFactory.create(1.0f, 1.0f, 1.0f);
        assertTrue(result.equals(expected));
    }

    @Test
    public void testLighting3() {
        Sphere sphere = sphereFactory.create();
        Material material = injector.getInstance(Key.get(Material.class, Names.named("defaultMaterial")));
        Tuple position = tupleFactory.point(0, 0, 0);
        Tuple eyev = tupleFactory.vector(0f, 0, -1);
        Tuple normalv = tupleFactory.vector(0, 0, -1);
        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(0, 10, -10), colorFactory.create(1, 1, 1));
        Color result = lightOperations.lighting(material, sphere, light, position, eyev, normalv, false);
        Color expected = colorFactory.create(0.7364f, 0.7364f, 0.7364f);
        assertTrue(result.equals(expected));
    }

    @Test
    public void testLighting4() {
        Sphere sphere = sphereFactory.create();
        Material material = injector.getInstance(Key.get(Material.class, Names.named("defaultMaterial")));
        Tuple position = tupleFactory.point(0, 0, 0);
        Tuple eyev = tupleFactory.vector(0f, -Math.sqrt(2)/2, -Math.sqrt(2)/2);
        Tuple normalv = tupleFactory.vector(0, 0, -1);
        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(0, 10, -10), colorFactory.create(1, 1, 1));
        Color result = lightOperations.lighting(material, sphere, light, position, eyev, normalv, false);
        Color expected = colorFactory.create(1.6364f, 1.6364f, 1.6364f);
        assertTrue(result.equals(expected));
    }

    @Test
    public void testLighting5() {
        Sphere sphere = sphereFactory.create();
        Material material = injector.getInstance(Key.get(Material.class, Names.named("defaultMaterial")));
        Tuple position = tupleFactory.point(0, 0, 0);
        Tuple eyev = tupleFactory.vector(0f, 0, -1);
        Tuple normalv = tupleFactory.vector(0, 0, -1);
        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(0, 0, 10), colorFactory.create(1, 1, 1));
        Color result = lightOperations.lighting(material, sphere, light, position, eyev, normalv, false);
        Color expected = colorFactory.create(0.1f, 0.1f, 0.1f);
        assertTrue(result.equals(expected));
    }

    @Test
    public void testLighting6() {
        Sphere sphere = sphereFactory.create();
        Material material = injector.getInstance(Key.get(Material.class, Names.named("defaultMaterial")));
        Tuple position = tupleFactory.point(0, 0, 0);
        Tuple eyev = tupleFactory.vector(0f, 0, -1);
        Tuple normalv = tupleFactory.vector(0, 0, -1);
        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(0, 0, -10), colorFactory.create(1, 1, 1));
        Color result = lightOperations.lighting(material, sphere, light, position, eyev, normalv, true);
        Color expected = colorFactory.create(0.1f, 0.1f, 0.1f);
        assertTrue(result.equals(expected));
    }
}