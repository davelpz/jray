package dev.davelpz.jray.light;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LightSourceTest {
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
    public void intensity() {
        Color intensity = colorFactory.create(1, 1, 1);
        Tuple position = tupleFactory.point(0, 0, 0);
        LightSource light = lightSourceFactory.createPointLight(position, intensity);
        assertEquals(intensity, light.intensity());
    }

    @Test
    public void position() {
        Color intensity = colorFactory.create(1, 1, 1);
        Tuple position = tupleFactory.point(0, 0, 0);
        LightSource light = lightSourceFactory.createPointLight(position, intensity);
        assertEquals(position, light.position());
    }
}