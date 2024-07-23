package dev.davelpz.jray.world;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import dev.davelpz.jray.canvas.CanvasFactory;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.light.LightSource;
import dev.davelpz.jray.light.LightSourceFactory;
import dev.davelpz.jray.light.operations.LightOperations;
import dev.davelpz.jray.material.MaterialFactory;
import dev.davelpz.jray.material.PatternFactory;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.Computation;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.shape.PlaneFactory;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.shape.Sphere;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WorldTest {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    CanvasFactory canvasFactory;
    ColorFactory colorFactory;
    LightSourceFactory lightSourceFactory;
    MaterialFactory materialFactory;
    LightOperations lightOperations;
    WorldFactory worldFactory;
    PlaneFactory planeFactory;
    PatternFactory patternFactory;

    @Before
    public void setUp() {
        injector =  Guice.createInjector(new RayTracerModule());
        rayOperations = injector.getInstance(RayOperations.class);
        intersectionFactory = injector.getInstance(IntersectionFactory.class);
        tupleFactory = injector.getInstance(TupleFactory.class);
        rayFactory = injector.getInstance(RayFactory.class);
        sphereFactory = injector.getInstance(SphereFactory.class);
        matrixOperations = injector.getInstance(MatrixOperations.class);
        canvasFactory = injector.getInstance(CanvasFactory.class);
        colorFactory = injector.getInstance(ColorFactory.class);
        lightSourceFactory = injector.getInstance(LightSourceFactory.class);
        materialFactory = injector.getInstance(MaterialFactory.class);
        lightOperations = injector.getInstance(LightOperations.class);
        worldFactory = injector.getInstance(WorldFactory.class);
        planeFactory = injector.getInstance(PlaneFactory.class);
        patternFactory = injector.getInstance(PatternFactory.class);
    }

    @Test
    public void testCreate() {
        World world = worldFactory.create();
        assertEquals(0, world.objects().size());
        assertEquals(0, world.lights().size());
    }

    @Test
    public void testDefaultWorld() {
        LightSource lightSource = lightSourceFactory.createPointLight(tupleFactory.point(-10f, 10f, -10f), colorFactory.create(1, 1, 1));
        Sphere sphere1 = sphereFactory.create();
        sphere1.material().setColor(colorFactory.create(0.8f, 1.0f, 0.6f));
        sphere1.material().setDiffuse(0.7f);
        sphere1.material().setSpecular(0.2f);
        Sphere sphere2 = sphereFactory.create();
        sphere2.setTransform(matrixOperations.scaling(0.5f, 0.5f, 0.5f));

        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        assertEquals(2, world.objects().size());
        assertEquals(1, world.lights().size());
        assertTrue(lightSource.equals(world.lights().get(0)));
        assertTrue(sphere1.equals(world.objects().get(0)));
        assertTrue(sphere2.equals(world.objects().get(1)));
    }

    @Test
    public void testIntersect() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -5), tupleFactory.vector(0, 0, 1));
        List<Intersection> intersections = world.intersect(ray);
        assertEquals(4, intersections.size());
        assertEquals(4, intersections.get(0).t(), 0.00001);
        assertEquals(4.5, intersections.get(1).t(), 0.00001);
        assertEquals(5.5, intersections.get(2).t(), 0.00001);
        assertEquals(6, intersections.get(3).t(), 0.00001);
    }

    @Test
    public void shadeHit1() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -5), tupleFactory.vector(0, 0, 1));
        Shape sphere = world.objects().get(0);
        Intersection intersection = intersectionFactory.create(4, sphere);
        Computation computation = rayOperations.prepareComputations(intersection, ray, List.of(intersection));
        Color color = world.shadeHit(computation, 5);
        assertTrue(color.equals(colorFactory.create(0.38066f, 0.47583f, 0.2855f)));
    }

    @Test
    public void shadeHit2() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        world.lights().clear();
        world.lights().add(lightSourceFactory.createPointLight(tupleFactory.point(0, 0.25f, 0), colorFactory.create(1, 1, 1)));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, 0), tupleFactory.vector(0, 0, 1));
        Shape sphere = world.objects().get(1);
        Intersection intersection = intersectionFactory.create(0.5f, sphere);
        Computation computation = rayOperations.prepareComputations(intersection, ray, List.of(intersection));
        Color color = world.shadeHit(computation, 5);
        assertTrue(color.equals(colorFactory.create(0.90498f, 0.90498f, 0.90498f)));
    }

    @Test
    public void shadeHit3() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        world.lights().clear();
        world.lights().add(lightSourceFactory.createPointLight(tupleFactory.point(0, 0, 10), colorFactory.create(1, 1, 1)));
        Sphere s1 = sphereFactory.create();
        world.addObject(s1);
        Sphere s2 = sphereFactory.create();
        s2.setTransform(matrixOperations.translation(0, 0, 10));
        world.addObject(s2);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, 5), tupleFactory.vector(0, 0, 1));
        Intersection intersection = intersectionFactory.create(4, s2);
        Computation computation = rayOperations.prepareComputations(intersection, ray, List.of(intersection));
        Color color = world.shadeHit(computation, 5);
        assertTrue(color.equals(colorFactory.create(0.1f, 0.1f, 0.1f)));
    }

    @Test
    public void shadeHit4() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Shape plane = planeFactory.create();
        plane.material().setReflectivity(0.5f);
        plane.setTransform(matrixOperations.translation(0, -1, 0));
        world.addObject(plane);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -3), tupleFactory.vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Intersection intersection = intersectionFactory.create(Math.sqrt(2), plane);
        Computation computation = rayOperations.prepareComputations(intersection, ray, List.of(intersection));
        Color color = world.shadeHit(computation, 5);
        assertTrue(color.equals(colorFactory.create(0.87677f, 0.92436f, 0.82918f)));
    }

    @Test
    public void shadeHit5() {
        World world = injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Shape floor = planeFactory.create();
        floor.setTransform(matrixOperations.translation(0, -1, 0));
        floor.material().setTransparency(0.5f);
        floor.material().setRefractiveIndex(1.5f);
        world.addObject(floor);
        Shape ball = sphereFactory.create();
        ball.material().setColor(colorFactory.create(1, 0, 0));
        ball.material().setAmbient(0.5f);
        ball.setTransform(matrixOperations.translation(0, -3.5f, -0.5f));
        world.addObject(ball);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -3), tupleFactory.vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        List<Intersection> intersections = List.of(intersectionFactory.create(Math.sqrt(2), floor));
        Computation computation = rayOperations.prepareComputations(intersections.get(0), ray, intersections);
        Color color = world.shadeHit(computation, 5);
        assertTrue(color.equals(colorFactory.create(0.93642f, 0.68642f, 0.68642f)));
    }

    @Test
    public void shadeHit6() {
        World world = injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -3), tupleFactory.vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Shape floor = planeFactory.create();
        floor.setTransform(matrixOperations.translation(0, -1, 0));
        floor.material().setReflectivity(0.5f);
        floor.material().setTransparency(0.5f);
        floor.material().setRefractiveIndex(1.5f);
        world.addObject(floor);
        Shape ball = sphereFactory.create();
        ball.material().setColor(colorFactory.create(1, 0, 0));
        ball.material().setAmbient(0.5f);
        ball.setTransform(matrixOperations.translation(0, -3.5f, -0.5f));
        world.addObject(ball);
        List<Intersection> intersections = List.of(intersectionFactory.create(Math.sqrt(2), floor));
        Computation computation = rayOperations.prepareComputations(intersections.get(0), ray, intersections);
        Color color = world.shadeHit(computation, 5);
        assertTrue(color.equals(colorFactory.create(0.93391f, 0.69643f, 0.69243f)));
    }

    @Test
    public void colorAt1() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -5), tupleFactory.vector(0, 1, 0));
        Color color = world.colorAt(ray, 5);
        assertTrue(color.equals(colorFactory.create(0, 0, 0)));
    }

    @Test
    public void colorAt2() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -5), tupleFactory.vector(0, 0, 1));
        Color color = world.colorAt(ray, 5);
        assertTrue(color.equals(colorFactory.create(0.38066f, 0.47583f, 0.2855f)));
    }

    @Test
    public void colorAt3() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Shape sphere1 = world.objects().get(0);
        sphere1.material().setAmbient(1);
        Shape sphere2 = world.objects().get(1);
        sphere2.material().setAmbient(1);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, 0.75f), tupleFactory.vector(0, 0, -1));
        Color color = world.colorAt(ray, 5);
        assertTrue(color.equals(sphere2.material().color()));
    }

    @Test
    public void colorAt4() {
        World world = worldFactory.create();
        LightSource pointLight = lightSourceFactory.createPointLight(tupleFactory.point(0, 0f, 0), colorFactory.create(1, 1, 1));
        world.lights().add(pointLight);
        Shape lower = planeFactory.create();
        lower.material().setReflectivity(1);
        lower.material().setAmbient(1);
        lower.setTransform(matrixOperations.translation(0, -1, 0));
        world.addObject(lower);
        Shape upper = planeFactory.create();
        upper.material().setReflectivity(1);
        upper.material().setAmbient(1);
        upper.setTransform(matrixOperations.translation(0, 1, 0));
        world.addObject(upper);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, 0), tupleFactory.vector(0, 1, 0));
        Color result = world.colorAt(ray, 5);
        assertTrue(result.equals(colorFactory.create(16.8f, 16.8f, 16.8f)));
    }

    @Test
    public void isShadowed() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Tuple point = tupleFactory.point(0, 10, 0);
        assertFalse(world.isShadowed(point));

        point = tupleFactory.point(10, -10, 10);
        assertTrue(world.isShadowed(point));

        point = tupleFactory.point(-20, 20, -20);
        assertFalse(world.isShadowed(point));

        point = tupleFactory.point(-2, 2, -2);
        assertFalse(world.isShadowed(point));
    }

    @Test
    public void reflectedColor1() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, 0), tupleFactory.vector(0, 0, 1));
        Shape shape = world.objects().get(1);
        shape.material().setAmbient(1);
        Intersection intersection = intersectionFactory.create(1, shape);
        Computation computation = rayOperations.prepareComputations(intersection, ray, List.of(intersection));
        Color color = world.reflectedColor(computation, 5);
        assertTrue(color.equals(colorFactory.create(0, 0, 0)));
    }

    @Test
    public void reflectedColor2() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Shape shape = planeFactory.create();
        shape.material().setReflectivity(0.5f);
        shape.setTransform(matrixOperations.translation(0, -1, 0));
        world.addObject(shape);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -3), tupleFactory.vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Intersection intersection = intersectionFactory.create(Math.sqrt(2), shape);
        Computation computation = rayOperations.prepareComputations(intersection, ray, List.of(intersection));
        Color color = world.reflectedColor(computation, 5);
        assertTrue(color.equals(colorFactory.create(0.19032f, 0.2379f, 0.14274f)));
    }

    @Test
    public void reflectedColor3() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Shape shape = planeFactory.create();
        shape.material().setReflectivity(0.5f);
        shape.setTransform(matrixOperations.translation(0, -1, 0));
        world.addObject(shape);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -3), tupleFactory.vector(0, -Math.sqrt(2)/2, Math.sqrt(2)/2));
        Intersection intersection = intersectionFactory.create(Math.sqrt(2), shape);
        Computation computation = rayOperations.prepareComputations(intersection, ray, List.of(intersection));
        Color color = world.reflectedColor(computation, 0);
        assertTrue(color.equals(colorFactory.create(0, 0, 0)));
    }

    @Test
    public void refractedColor1() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Shape shape = world.objects().get(0);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -5), tupleFactory.vector(0, 0, 1));
        List<Intersection> intersections = List.of(intersectionFactory.create(4, shape), intersectionFactory.create(6, shape));
        Computation computation = rayOperations.prepareComputations(intersections.get(0), ray, intersections);
        Color color = world.refractedColor(computation, 5);
        assertTrue(color.equals(colorFactory.create(0, 0, 0)));
    }

    @Test
    public void refractedColor2() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Shape shape = world.objects().get(0);
        shape.material().setTransparency(1);
        shape.material().setRefractiveIndex(1.5f);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, -5), tupleFactory.vector(0, 0, 1));
        List<Intersection> intersections = List.of(intersectionFactory.create(4, shape), intersectionFactory.create(6, shape));
        Computation computation = rayOperations.prepareComputations(intersections.get(0), ray, intersections);
        Color color = world.refractedColor(computation, 0);
        assertTrue(color.equals(colorFactory.create(0, 0, 0)));
    }

    @Test
    public void refractedColor3() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Shape shape = world.objects().get(0);
        shape.material().setTransparency(1);
        shape.material().setRefractiveIndex(1.5f);
        Ray ray = rayFactory.create(tupleFactory.point(0, 0, Math.sqrt(2)/2), tupleFactory.vector(0, 1, 0));
        List<Intersection> intersections = List.of(intersectionFactory.create(-Math.sqrt(2)/2, shape), intersectionFactory.create(Math.sqrt(2)/2, shape));
        Computation computation = rayOperations.prepareComputations(intersections.get(1), ray, intersections);
        Color color = world.refractedColor(computation, 5);
        assertTrue(color.equals(colorFactory.create(0, 0, 0)));
    }

    @Test
    public void refractedColor4() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Shape shapeA = world.objects().get(0);
        shapeA.material().setAmbient(1);
        shapeA.material().setPattern(patternFactory.createTestPattern());
        Shape shapeB = world.objects().get(1);
        shapeB.material().setTransparency(1);
        shapeB.material().setRefractiveIndex(1.5f);

        Ray ray = rayFactory.create(tupleFactory.point(0, 0, 0.1f), tupleFactory.vector(0, 1, 0));
        List<Intersection> intersections = List.of(intersectionFactory.create(-0.9899f, shapeA), intersectionFactory.create(-0.4899f, shapeB),
                intersectionFactory.create(0.4899f, shapeB), intersectionFactory.create(0.9899f, shapeA));
        Computation computation = rayOperations.prepareComputations(intersections.get(2), ray, intersections);
        Color color = world.refractedColor(computation, 5);
        assertTrue(color.equals(colorFactory.create(0, 0.99888f, 0.04725f)));
    }
}