package dev.davelpz.jray.camera;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import dev.davelpz.jray.canvas.Canvas;
import dev.davelpz.jray.canvas.CanvasFactory;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.light.LightSourceFactory;
import dev.davelpz.jray.light.operations.LightOperations;
import dev.davelpz.jray.material.MaterialFactory;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import dev.davelpz.jray.world.World;
import dev.davelpz.jray.world.WorldFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CameraTest {
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
    CameraFactory cameraFactory;

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
        cameraFactory = injector.getInstance(CameraFactory.class);
    }

    @Test
    public void testCreate() {
        Matrix identity = injector.getInstance(Key.get(Matrix.class, Names.named("identityMatrix")));
        int hSize = 160;
        int vSize = 120;
        double fieldOfView = Math.PI / 2;
        Camera camera = cameraFactory.create(hSize, vSize, fieldOfView);
        assertEquals(camera.hSize(), hSize);
        assertEquals(camera.vSize(), vSize);
        assertEquals(camera.fieldOfView(), fieldOfView, Constants.EPSILON);
        assertTrue(camera.transform().equals(identity));
    }

    @Test
    public void testPixelSize() {
        Camera camera = cameraFactory.create(200, 125, Math.PI / 2);
        assertEquals(0.01, camera.pixelSize(), Constants.EPSILON);

        camera = cameraFactory.create(125, 200, Math.PI / 2);
        assertEquals(0.01, camera.pixelSize(), Constants.EPSILON);
    }

    @Test
    public void rayForPixel() {
        Camera camera = cameraFactory.create(201, 101, Math.PI / 2);
        Ray ray = camera.rayForPixel(100, 50);
        assertTrue(ray.origin().equals(tupleFactory.point(0f, 0f, 0f)));
        assertTrue(ray.direction().equals(tupleFactory.vector(0f, 0f, -1f)));

        camera = cameraFactory.create(201, 101, Math.PI / 2);
        ray = camera.rayForPixel(0, 0);
        assertTrue(ray.origin().equals(tupleFactory.point(0f, 0f, 0f)));
        assertTrue(ray.direction().equals(tupleFactory.vector(0.66519f, 0.33259f, -0.66851f)));

        camera = cameraFactory.create(201, 101, Math.PI / 2);
        camera.setTransform(matrixOperations.rotationY(Math.PI / 4));
        camera.setTransform(camera.transform().multiply(matrixOperations.translation(0, -2, 5)));
        ray = camera.rayForPixel(100, 50);
        assertTrue(ray.origin().equals(tupleFactory.point(0f, 2f, -5f)));
        assertTrue(ray.direction().equals(tupleFactory.vector(Math.sqrt(2) / 2, 0, -(double) Math.sqrt(2) / 2)));
    }

    @Test
    public void render() {
        World world =  injector.getInstance(Key.get(World.class, Names.named("defaultWorld")));
        Camera camera = cameraFactory.create(11, 11, Math.PI / 2);
        Tuple from = tupleFactory.point(0, 0, -5);
        Tuple to = tupleFactory.point(0, 0, 0);
        Tuple up = tupleFactory.vector(0, 1, 0);
        camera.setTransform(matrixOperations.viewTransform(from, to, up));
        Canvas canvas = camera.render(world);
        assertTrue(canvas.pixelAt(5, 5).equals(colorFactory.create(0.38066f, 0.47583f, 0.2855f)));
    }
}