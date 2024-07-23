package dev.davelpz.jray.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import dev.davelpz.jray.bvh.Aabb;
import dev.davelpz.jray.bvh.AabbFactory;
import dev.davelpz.jray.bvh.BvhOperations;
import dev.davelpz.jray.bvh.impl.AabbImpl;
import dev.davelpz.jray.camera.Camera;
import dev.davelpz.jray.camera.CameraFactory;
import dev.davelpz.jray.camera.impl.CameraImpl;
import dev.davelpz.jray.canvas.Canvas;
import dev.davelpz.jray.canvas.CanvasFactory;
import dev.davelpz.jray.canvas.impl.AntiAliasCanvas;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.color.impl.RGB;
import dev.davelpz.jray.light.LightSource;
import dev.davelpz.jray.light.LightSourceFactory;
import dev.davelpz.jray.light.impl.PointLight;
import dev.davelpz.jray.light.operations.LightOperations;
import dev.davelpz.jray.light.operations.LightOperationsFactory;
import dev.davelpz.jray.light.operations.impl.LightOperationsImpl;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.material.MaterialFactory;
import dev.davelpz.jray.material.Pattern;
import dev.davelpz.jray.material.PatternFactory;
import dev.davelpz.jray.material.impl.CheckersPattern;
import dev.davelpz.jray.material.impl.DefaultMaterial;
import dev.davelpz.jray.material.impl.GradientPattern;
import dev.davelpz.jray.material.impl.RingPattern;
import dev.davelpz.jray.material.impl.StripePattern;
import dev.davelpz.jray.material.impl.TestPattern;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.matrix.operations.MatrixOperationsFactory;
import dev.davelpz.jray.matrix.operations.impl.MatrixOperationsImpl;
import dev.davelpz.jray.obj.ObjLoader;
import dev.davelpz.jray.ray.operations.*;
import dev.davelpz.jray.ray.operations.impl.ComputationImpl;
import dev.davelpz.jray.ray.operations.impl.IntersectionFactoryImpl;
import dev.davelpz.jray.ray.operations.impl.RayOperationsImpl;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.matrix.MatrixFactory;
import dev.davelpz.jray.matrix.impl.MatrixImpl;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.impl.RayImpl;
import dev.davelpz.jray.shape.Cone;
import dev.davelpz.jray.shape.ConeFactory;
import dev.davelpz.jray.shape.Cube;
import dev.davelpz.jray.shape.CubeFactory;
import dev.davelpz.jray.shape.Cylinder;
import dev.davelpz.jray.shape.CylinderFactory;
import dev.davelpz.jray.shape.Group;
import dev.davelpz.jray.shape.GroupFactory;
import dev.davelpz.jray.shape.Plane;
import dev.davelpz.jray.shape.PlaneFactory;
import dev.davelpz.jray.shape.ShapeBuilder;
import dev.davelpz.jray.shape.SmoothTriangle;
import dev.davelpz.jray.shape.SmoothTriangleFactory;
import dev.davelpz.jray.shape.Sphere;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.shape.Triangle;
import dev.davelpz.jray.shape.TriangleFactory;
import dev.davelpz.jray.shape.impl.ConeImpl;
import dev.davelpz.jray.shape.impl.CubeImpl;
import dev.davelpz.jray.shape.impl.CylinderImpl;
import dev.davelpz.jray.shape.impl.GroupImpl;
import dev.davelpz.jray.shape.impl.PlaneImpl;
import dev.davelpz.jray.shape.impl.SmoothTriangleImpl;
import dev.davelpz.jray.shape.impl.SphereImpl;
import dev.davelpz.jray.shape.impl.TriangleImpl;
import dev.davelpz.jray.tuple.impl.Tup;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.TupleFactoryBase;
import dev.davelpz.jray.world.World;
import dev.davelpz.jray.world.WorldFactory;
import dev.davelpz.jray.world.impl.WorldImpl;

public class RayTracerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TupleFactory.class);
        bind(ShapeBuilder.class);
        bind(ObjLoader.class);
        bind(IntersectionFactory.class).to(IntersectionFactoryImpl.class);
        install(new FactoryModuleBuilder().implement(Tuple.class, Tup.class).build(TupleFactoryBase.class));
        install(new FactoryModuleBuilder().implement(Color.class, RGB.class).build(ColorFactory.class));
        install(new FactoryModuleBuilder().implement(Canvas.class, AntiAliasCanvas.class).build(CanvasFactory.class));
        install(new FactoryModuleBuilder().implement(Matrix.class, MatrixImpl.class).build(MatrixFactory.class));
        install(new FactoryModuleBuilder().implement(MatrixOperations.class, MatrixOperationsImpl.class).build(MatrixOperationsFactory.class));
        install(new FactoryModuleBuilder().implement(Ray.class, RayImpl.class).build(RayFactory.class));
        install(new FactoryModuleBuilder().implement(RayOperations.class, RayOperationsImpl.class).build(RayOperationsFactory.class));
        install(new FactoryModuleBuilder().implement(Computation.class, ComputationImpl.class).build(ComputationFactory.class));
        install(new FactoryModuleBuilder().implement(Sphere.class, SphereImpl.class).build(SphereFactory.class));
        install(new FactoryModuleBuilder().implement(LightSource.class, Names.named("point"), PointLight.class).build(LightSourceFactory.class));
        install(new FactoryModuleBuilder().implement(LightOperations.class, LightOperationsImpl.class).build(LightOperationsFactory.class));
        install(new FactoryModuleBuilder().implement(Material.class, DefaultMaterial.class).build(MaterialFactory.class));
        install(new FactoryModuleBuilder().implement(World.class, WorldImpl.class).build(WorldFactory.class));
        install(new FactoryModuleBuilder().implement(Camera.class, CameraImpl.class).build(CameraFactory.class));
        install(new FactoryModuleBuilder().implement(Plane.class, PlaneImpl.class).build(PlaneFactory.class));
        install(new FactoryModuleBuilder().implement(Cube.class, CubeImpl.class).build(CubeFactory.class));
        install(new FactoryModuleBuilder().implement(Cylinder.class, CylinderImpl.class).build(CylinderFactory.class));
        install(new FactoryModuleBuilder().implement(Cone.class, ConeImpl.class).build(ConeFactory.class));
        install(new FactoryModuleBuilder().implement(Triangle.class, TriangleImpl.class).build(TriangleFactory.class));
        install(new FactoryModuleBuilder().implement(SmoothTriangle.class, SmoothTriangleImpl.class).build(SmoothTriangleFactory.class));
        install(new FactoryModuleBuilder().implement(Group.class, GroupImpl.class).build(GroupFactory.class));
        install(new FactoryModuleBuilder().implement(Aabb.class, AabbImpl.class).build(AabbFactory.class));
        install(new FactoryModuleBuilder()
                .implement(Pattern.class, Names.named("stripePattern"), StripePattern.class)
                .implement(Pattern.class, Names.named("gradientPattern"), GradientPattern.class)
                .implement(Pattern.class, Names.named("ringPattern"), RingPattern.class)
                .implement(Pattern.class, Names.named("checkersPattern"), CheckersPattern.class)
                .implement(Pattern.class, Names.named("testPattern"), TestPattern.class)
                .build(PatternFactory.class));
    }

    @Provides
    @Singleton
    LightOperations providesLightOperations(LightOperationsFactory lightOperationsFactory) {
        return lightOperationsFactory.create();
    }

    @Provides
    @Singleton
    RayOperations providesRayOperations(RayOperationsFactory rayOperationsFactory) {
        return rayOperationsFactory.create();
    }

    @Provides
    @Singleton
    MatrixOperations providesMatrixOperations(MatrixOperationsFactory matrixOperationsFactory) {
        return matrixOperationsFactory.create();
    }

    @Provides
    @Singleton
    BvhOperations providesBvhOperations(TupleFactory tupleFactory) {
        return new BvhOperations(tupleFactory);
    }

    @Provides
    @Singleton
    @Named("identityMatrix")
    Matrix providesIdentityMatrix(MatrixFactory matrixFactory) {
        Matrix matrix = matrixFactory.create(4, 4);
        matrix.set(0, 0, 1);
        matrix.set(1, 1, 1);
        matrix.set(2, 2, 1);
        matrix.set(3, 3, 1);
        return matrix;
    }

    @Provides
    @Named("defaultMaterial")
    Material providesDefaultMaterial(MaterialFactory materialFactory, ColorFactory colorFactory) {
        return materialFactory.create(colorFactory.create(1, 1, 1), 0.1, 0.9, 0.9, 200.0,
                0.0, 0.0, 1.0);
    }

    @Provides
    @Named("defaultWorld")
    World provideDefaultWorld(WorldFactory worldFactory, LightSourceFactory lightSourceFactory, SphereFactory sphereFactory, TupleFactory tupleFactory, ColorFactory colorFactory, MatrixOperations matrixOperations ) {
        LightSource lightSource = lightSourceFactory.createPointLight(tupleFactory.point(-10f, 10f, -10f), colorFactory.create(1, 1, 1));
        Sphere sphere1 = sphereFactory.create();
        sphere1.material().setColor(colorFactory.create(0.8f, 1.0f, 0.6f));
        sphere1.material().setDiffuse(0.7f);
        sphere1.material().setSpecular(0.2f);
        Sphere sphere2 = sphereFactory.create();
        sphere2.setTransform(matrixOperations.scaling(0.5f, 0.5f, 0.5f));
        World world = worldFactory.create();
        world.addLight(lightSource);
        world.addObject(sphere1);
        world.addObject(sphere2);
        return world;
    }

    @Provides
    @Singleton
    @Named("blackColor")
    Color providesBlackColor(ColorFactory colorFactory) {
        return colorFactory.create(0, 0, 0);
    }

    @Provides
    @Singleton
    @Named("whiteColor")
    Color providesWhiteColor(ColorFactory colorFactory) {
        return colorFactory.create(1, 1, 1);
    }

    @Provides
    @Named("glassSphere")
    Sphere providesGlassSphere(SphereFactory sphereFactory, TupleFactory tupleFactory, ColorFactory colorFactory) {
        Sphere sphere = sphereFactory.create();
        sphere.material().setTransparency(1.0);
        sphere.material().setRefractiveIndex(1.5);
        return sphere;
    }
}


