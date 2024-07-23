package dev.davelpz.jray.material;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import dev.davelpz.jray.camera.CameraFactory;
import dev.davelpz.jray.canvas.CanvasFactory;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.light.LightSourceFactory;
import dev.davelpz.jray.light.operations.LightOperations;
import dev.davelpz.jray.material.impl.CheckersPattern;
import dev.davelpz.jray.material.impl.GradientPattern;
import dev.davelpz.jray.material.impl.RingPattern;
import dev.davelpz.jray.material.impl.StripePattern;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.shape.PlaneFactory;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import dev.davelpz.jray.world.WorldFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PatternTest {
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
        cameraFactory = injector.getInstance(CameraFactory.class);
        planeFactory = injector.getInstance(PlaneFactory.class);
        patternFactory = injector.getInstance(PatternFactory.class);
    }

    @Test
    public void testCreateStripePattern() {
        Matrix identity = injector.getInstance(Key.get(Matrix.class, Names.named("identityMatrix")));
        StripePattern stripePattern = patternFactory.createStripePattern(colorFactory.create(1, 1, 1), colorFactory.create(0, 0, 0));
        assertTrue(colorFactory.create(1, 1, 1).equals(stripePattern.a()));
        assertTrue(colorFactory.create(0, 0, 0).equals(stripePattern.b()));
        assertTrue(identity.equals(stripePattern.transform()));
    }

    @Test
    public void testCreateGradientPattern() {
        Color white = colorFactory.create(1, 1, 1);
        Color black = colorFactory.create(0, 0, 0);
        Matrix identity = injector.getInstance(Key.get(Matrix.class, Names.named("identityMatrix")));
        GradientPattern gradientPattern = patternFactory.createGradientPattern(white, black);
        assertTrue(white.equals(gradientPattern.a()));
        assertTrue(black.equals(gradientPattern.b()));
        assertTrue(identity.equals(gradientPattern.transform()));
        assertTrue(gradientPattern.patternAt(tupleFactory.point(0, 0, 0)).equals(white));
        assertTrue(gradientPattern.patternAt(tupleFactory.point(0.25, 0, 0)).equals(colorFactory.create(0.75, 0.75, 0.75)));
        assertTrue(gradientPattern.patternAt(tupleFactory.point(0.5, 0, 0)).equals(colorFactory.create(0.5, 0.5, 0.5)));
        assertTrue(gradientPattern.patternAt(tupleFactory.point(0.75, 0, 0)).equals(colorFactory.create(0.25, 0.25, 0.25)));
    }

    @Test
    public void testCreateRingPattern() {
        Color white = colorFactory.create(1, 1, 1);
        Color black = colorFactory.create(0, 0, 0);
        Matrix identity = injector.getInstance(Key.get(Matrix.class, Names.named("identityMatrix")));
        RingPattern ringPattern = patternFactory.createRingPattern(white, black);
        assertTrue(white.equals(ringPattern.a()));
        assertTrue(black.equals(ringPattern.b()));
        assertTrue(identity.equals(ringPattern.transform()));
        assertTrue(ringPattern.patternAt(tupleFactory.point(0, 0, 0)).equals(white));
        assertTrue(ringPattern.patternAt(tupleFactory.point(1, 0, 0)).equals(black));
        assertTrue(ringPattern.patternAt(tupleFactory.point(0, 0, 1)).equals(black));
    }

    @Test
    public void testCreatCheckersPattern() {
        Color white = colorFactory.create(1, 1, 1);
        Color black = colorFactory.create(0, 0, 0);
        Matrix identity = injector.getInstance(Key.get(Matrix.class, Names.named("identityMatrix")));
        CheckersPattern checkersPattern = patternFactory.createCheckersPattern(white, black);
        assertTrue(white.equals(checkersPattern.a()));
        assertTrue(black.equals(checkersPattern.b()));
        assertTrue(identity.equals(checkersPattern.transform()));
        assertTrue(checkersPattern.patternAt(tupleFactory.point(0, 0, 0)).equals(white));
        assertTrue(checkersPattern.patternAt(tupleFactory.point(0.99, 0, 0)).equals(white));
        assertTrue(checkersPattern.patternAt(tupleFactory.point(1.01, 0, 0)).equals(black));
        assertTrue(checkersPattern.patternAt(tupleFactory.point(0,0, 0)).equals(white));
        assertTrue(checkersPattern.patternAt(tupleFactory.point(0,0.99, 0)).equals(white));
        assertTrue(checkersPattern.patternAt(tupleFactory.point(0,1.01, 0)).equals(black));
        assertTrue(checkersPattern.patternAt(tupleFactory.point(0,0,0)).equals(white));
        assertTrue(checkersPattern.patternAt(tupleFactory.point(0,0,0.99)).equals(white));
        assertTrue(checkersPattern.patternAt(tupleFactory.point(0,0,1.01)).equals(black));
    }

    @Test
    public void testPatternAt() {
        Color white = colorFactory.create(1, 1, 1);
        Color black = colorFactory.create(0, 0, 0);
        StripePattern stripePattern = patternFactory.createStripePattern(white, black);
        assertTrue(stripePattern.patternAt(tupleFactory.point(0, 0, 0)).equals(white));
        assertTrue(stripePattern.patternAt(tupleFactory.point(0, 1, 0)).equals(white));
        assertTrue(stripePattern.patternAt(tupleFactory.point(0, 2, 0)).equals(white));
        assertTrue(stripePattern.patternAt(tupleFactory.point(0, 0, 0)).equals(white));
        assertTrue(stripePattern.patternAt(tupleFactory.point(0, 0, 1)).equals(white));
        assertTrue(stripePattern.patternAt(tupleFactory.point(0, 0, 2)).equals(white));
        assertTrue(stripePattern.patternAt(tupleFactory.point(0.9, 0, 0)).equals(white));
        assertTrue(stripePattern.patternAt(tupleFactory.point(1, 0, 0)).equals(black));
        assertTrue(stripePattern.patternAt(tupleFactory.point(-0.1, 0, 0)).equals(black));
        assertTrue(stripePattern.patternAt(tupleFactory.point(-1, 0, 0)).equals(black));
        assertTrue(stripePattern.patternAt(tupleFactory.point(-1.1, 0, 0)).equals(white));
    }
}