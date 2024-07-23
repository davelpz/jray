package dev.davelpz.jray.obj;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.shape.CubeFactory;
import dev.davelpz.jray.shape.Group;
import dev.davelpz.jray.shape.GroupFactory;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.shape.TriangleFactory;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;

public class TestObjReader {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    CubeFactory cubeFactory;
    TriangleFactory triangleFactory;
    GroupFactory groupFactory;
    ObjLoader objLoader;

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
        triangleFactory = injector.getInstance(TriangleFactory.class);
        groupFactory = injector.getInstance(GroupFactory.class);
        objLoader = injector.getInstance(ObjLoader.class);
    }

    @Test
    public void testReader() {
        List<Group> groups = objLoader.parseObjFile("src/main/resources/teapot.obj");
        assertNotNull(groups);
    }
}
