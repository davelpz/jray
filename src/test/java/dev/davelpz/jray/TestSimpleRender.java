package dev.davelpz.jray;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.davelpz.jray.animation.Animator;
import dev.davelpz.jray.camera.Camera;
import dev.davelpz.jray.camera.CameraFactory;
import dev.davelpz.jray.canvas.Canvas;
import dev.davelpz.jray.canvas.CanvasFactory;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.light.LightSource;
import dev.davelpz.jray.light.LightSourceFactory;
import dev.davelpz.jray.light.operations.LightOperations;
import dev.davelpz.jray.material.MaterialFactory;
import dev.davelpz.jray.material.PatternFactory;
import dev.davelpz.jray.material.impl.CheckersPattern;
import dev.davelpz.jray.material.impl.GradientPattern;
import dev.davelpz.jray.material.impl.StripePattern;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.obj.ObjLoader;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.shape.CubeFactory;
import dev.davelpz.jray.shape.GroupFactory;
import dev.davelpz.jray.shape.PlaneFactory;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.shape.Group;
import dev.davelpz.jray.shape.ShapeBuilder;
import dev.davelpz.jray.shape.SphereFactory;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import dev.davelpz.jray.world.World;
import dev.davelpz.jray.world.WorldFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class TestSimpleRender {
    Injector injector;
    RayOperations rayOperations;
    IntersectionFactory intersectionFactory;
    MatrixOperations matrixOperations;
    TupleFactory tupleFactory;
    RayFactory rayFactory;
    SphereFactory sphereFactory;
    CubeFactory cubeFactory;
    CanvasFactory canvasFactory;
    ColorFactory colorFactory;
    LightSourceFactory lightSourceFactory;
    MaterialFactory materialFactory;
    LightOperations lightOperations;
    WorldFactory worldFactory;
    CameraFactory cameraFactory;
    PlaneFactory planeFactory;
    PatternFactory patternFactory;
    ShapeBuilder shapeBuilder;
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
        cubeFactory = injector.getInstance(CubeFactory.class);
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
        shapeBuilder = injector.getInstance(ShapeBuilder.class);
        groupFactory = injector.getInstance(GroupFactory.class);
        objLoader = injector.getInstance(ObjLoader.class);
    }

    @Test
    @Ignore
    public void play() {
        Tuple rayOrigin = tupleFactory.point(0, 0, -5);
        int wallZ = 10;
        int wallSize = 7;
        int canvasPixels = 100;
        double pixelSize = (double) wallSize / canvasPixels;
        double half = wallSize / 2.0;
        Canvas canvas = canvasFactory.create(canvasPixels, canvasPixels);
        Shape sphere = shapeBuilder.createSphere().withColor(1, 0.2f, 1).build();
        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(-10, 10, -10), colorFactory.create(1, 1, 1));

        //sphere.setTransform(matrixOperations.scaling(1, 0.5f, 1).multiply(matrixOperations.shearing(1, 0, 0, 0, 0, 0)));
        for (int y = 0; y < canvasPixels; y++) {
            double worldY = half - pixelSize * y;
            for (int x = 0; x < canvasPixels; x++) {
                double worldX = -half + pixelSize * x;
                Tuple position = tupleFactory.point(worldX, worldY, wallZ);
                Ray ray = rayFactory.create(rayOrigin, position.subtract(rayOrigin).normalize());
                List<Intersection> intersections = rayOperations.intersect(sphere, ray);
                Optional<Intersection> hitOpt = rayOperations.hit(intersections);
                if (hitOpt.isPresent()) {
                    Intersection hit = hitOpt.get();
                    Tuple point = ray.position(hit.t());
                    Tuple normal = hit.shape().normalAt(point);
                    Tuple eye = ray.direction().negate();
                    Color color = lightOperations.lighting(hit.shape().material(), hit.shape(), light, point, eye, normal, false);
                    canvas.writePixel(x, y, color);
                }
            }
        }
        canvas.saveToFile("output.png");
    }

    @Test
    @Ignore
    public void renderShapesWithCamera() {
        Shape floor = shapeBuilder.createPlane().withColor(1, 0.9f, 0.9f).withSpecular(0).withReflectivity(.35f)
                .withPattern(patternFactory.createCheckersPattern(colorFactory.create(0.5f, 1, 0.5f), colorFactory.create(0.5f, 0.5f, 1))).build();

        StripePattern stripePattern = patternFactory.createStripePattern(colorFactory.create(1, 0.5f, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        stripePattern.setTransform(matrixOperations.scaling(0.25f, 0.25f, 0.25f));
        Shape leftWall = shapeBuilder.createPlane().withTranslation(0, 0, 5).withRotationY(-Math.PI / 4).withRotationX(Math.PI / 2)
                .withPattern(stripePattern).build();

        GradientPattern gradientPattern = patternFactory.createGradientPattern(colorFactory.create(1, 0.5f, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        gradientPattern.setTransform(matrixOperations.scaling(5.5f, 5.5f, 5.5f));
        Shape rightWall = shapeBuilder.createPlane().withTranslation(0, 0, 5).withRotationY(Math.PI / 4).withRotationX(Math.PI / 2)
                .withPattern(gradientPattern).build();

        StripePattern middlePattern = patternFactory.createStripePattern(colorFactory.create(0.1f, 1, 0.5f), colorFactory.create(0.1f, 0.5f, 1));
        middlePattern.setTransform(matrixOperations.scaling(0.1f, 0.1f, 0.1f).multiply(matrixOperations.rotationY(Math.PI / 3)));
        Shape middle = shapeBuilder.createSphere().withTranslation(-0.5f, 1, 0.5f).withColor(0.1f, 1, 0.5f).withDiffuse(0.7f).withSpecular(0.3f)
                .withPattern(middlePattern).withReflectivity(0.05f).build();

        Shape right = shapeBuilder.createCylinder(0.0,2.0,true).withTranslation(1.5f, 0.0f, -0.5f).withScaling(0.5f, 0.5f, 0.5f).withColor(0.5f, 1, 0.1f)
                .withDiffuse(0.1f).withSpecular(0.3f).withReflectivity(0.90f).withTransparency(1.0).withRefractiveIndex(1.5).build();

        Shape left = shapeBuilder.createCube().withTranslation(-1.5f, 0.33f, -0.75f).withScaling(0.33f, 0.33f, 0.33f).withColor(1, 0.4f, 0.4f)
                .withDiffuse(0.6f).withSpecular(0.6f).withReflectivity(0.50f).withTransparency(0.0).withRefractiveIndex(1.5).build();

        Shape cone = shapeBuilder.createCone(-1.0,0.0,true).withTranslation(0.0f, 0.75f, -0.75f).withColor(0.4f, 0.8f, 0.4f)
                .withDiffuse(0.6f).withSpecular(0.6f).withReflectivity(0.10f).withTransparency(0.0).withRefractiveIndex(1.5)
                .build();

        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(-10, 10, -10), colorFactory.create(1, 1, 1));
        World world = worldFactory.create();
        world.addLight(light);
        world.addObject(floor);
        world.addObject(leftWall);
        world.addObject(rightWall);
        world.addObject(middle);
        world.addObject(right);
        world.addObject(left);
        world.addObject(cone);
        int size = 50;
        Camera camera = cameraFactory.create(size * 16, size * 9, Math.PI / 3);
        camera.setTransform(matrixOperations.viewTransform(tupleFactory.point(0, 1.5f, -5), tupleFactory.point(0, 1, 0), tupleFactory.vector(0, 1, 0)));
        camera.setNumberOfSamples(20);
        Canvas canvas = camera.render(world);
        canvas.saveToFile("output.png");
    }

    @Test
    @Ignore
    public void renderCubesWithCamera() {
        CheckersPattern checkersPattern = patternFactory.createCheckersPattern(colorFactory.create(0.5f, 1, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        Shape floor = shapeBuilder.createPlane().withColor(1, 0.9f, 0.9f).withSpecular(0).withReflectivity(.35f).withPattern(checkersPattern).build();

        StripePattern stripePattern = patternFactory.createStripePattern(colorFactory.create(1, 0.5f, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        stripePattern.setTransform(matrixOperations.scaling(0.25f, 0.25f, 0.25f));
        Shape leftWall = shapeBuilder.createPlane().withTranslation(0, 0, 5).withRotationY(-Math.PI / 4).withRotationX(Math.PI / 2)
                .withPattern(stripePattern).build();

        GradientPattern gradientPattern = patternFactory.createGradientPattern(colorFactory.create(1, 0.5f, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        gradientPattern.setTransform(matrixOperations.scaling(5.5f, 5.5f, 5.5f));
        Shape rightWall = shapeBuilder.createPlane().withTranslation(0, 0, 5).withRotationY(Math.PI / 4).withRotationX(Math.PI / 2)
                .withPattern(gradientPattern).build();

        StripePattern middlePattern = patternFactory.createStripePattern(colorFactory.create(0.1f, 1, 0.5f), colorFactory.create(0.1f, 0.5f, 1));
        middlePattern.setTransform(matrixOperations.scaling(0.1f, 0.1f, 0.1f).multiply(matrixOperations.rotationY(Math.PI / 3)));

        Shape middle = shapeBuilder.createCube().withTranslation(-0.5f, 1, 0.5f).withColor(0.1f, 1, 0.5f).withDiffuse(0.7f).withSpecular(0.3f)
                .withPattern(middlePattern).withReflectivity(0.05f).build();

        Shape right = shapeBuilder.createCube().withTranslation(1.5f, 0.5f, -0.5f).withScaling(0.5f, 0.5f, 0.5f).withColor(0.5f, 1, 0.1f)
                .withDiffuse(0.9f).withSpecular(0.3f).withReflectivity(0.0f).withTransparency(0.0).withRefractiveIndex(1.5).build();

        Shape left = shapeBuilder.createCube().withTranslation(-1.5f, 0.33f, -1.00f).withScaling(0.33f, 0.33f, 0.33f).withColor(1, 0.8f, 0.1f)
                .withDiffuse(0.9f).withSpecular(0.3f).withReflectivity(0.0).withTransparency(0.0).withRefractiveIndex(1.5).build();

        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(-10, 10, -10), colorFactory.create(1, 1, 1));
        World world = worldFactory.create();
        world.addLight(light);
        world.addObject(floor);
        world.addObject(leftWall);
        world.addObject(rightWall);
        world.addObject(middle);
        world.addObject(right);
        world.addObject(left);
        Camera camera = cameraFactory.create(20 * 16, 20 * 9, Math.PI / 3);
        camera.setTransform(matrixOperations.viewTransform(tupleFactory.point(0, 1.5f, -5), tupleFactory.point(0, 1, 0), tupleFactory.vector(0, 1, 0)));
        Canvas canvas = camera.render(world);
        canvas.saveToFile("output.png");
    }

    @Test
    @Ignore
    public void renderCylinderWithCamera() {
        CheckersPattern checkersPattern = patternFactory.createCheckersPattern(colorFactory.create(0.5f, 1, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        Shape floor = shapeBuilder.createPlane().withColor(1, 0.9f, 0.9f).withSpecular(0).withReflectivity(.35f).withPattern(checkersPattern).build();

        StripePattern stripePattern = patternFactory.createStripePattern(colorFactory.create(1, 0.5f, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        stripePattern.setTransform(matrixOperations.scaling(0.25f, 0.25f, 0.25f));
        Shape leftWall = shapeBuilder.createPlane().withTranslation(0, 0, 5).withRotationY(-Math.PI / 4).withRotationX(Math.PI / 2)
                .withPattern(stripePattern).build();

        GradientPattern gradientPattern = patternFactory.createGradientPattern(colorFactory.create(1, 0.5f, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        gradientPattern.setTransform(matrixOperations.scaling(5.5f, 5.5f, 5.5f));
        Shape rightWall = shapeBuilder.createPlane().withTranslation(0, 0, 5).withRotationY(Math.PI / 4).withRotationX(Math.PI / 2)
                .withPattern(gradientPattern).build();

        StripePattern middlePattern = patternFactory.createStripePattern(colorFactory.create(0.1f, 1, 0.5f), colorFactory.create(0.1f, 0.5f, 1));
        middlePattern.setTransform(matrixOperations.scaling(0.1f, 0.1f, 0.1f).multiply(matrixOperations.rotationY(Math.PI / 3)));

        Shape middle = shapeBuilder.createCylinder(-1, 1, true).withTranslation(-0.5f, 1, 0.5f).withRotationY(Math.PI / 4).withRotationX(Math.PI / 2)
                .withColor(0.1f, 1, 0.5f).withDiffuse(0.7f).withSpecular(0.3f).withPattern(middlePattern).withReflectivity(0.05f).build();

        Shape right = shapeBuilder.createCylinder(0, 1, false).withTranslation(1.5f, 0.0f, -0.5f).withScaling(0.5f, 0.5f, 0.5f)
                .withColor(0.5f, 1, 0.1f).withDiffuse(0.9f).withSpecular(0.3f).withReflectivity(0.0f).withTransparency(0.0).withRefractiveIndex(1.5)
                .build();

        Shape left = shapeBuilder.createCylinder(0, 1, false).withTranslation(-1.5f, 0.0f, -1.00f).withScaling(0.33f, 0.33f, 0.33f)
                .withColor(1, 0.8f, 0.1f).withDiffuse(0.9f).withSpecular(0.3f).withReflectivity(0.0).withTransparency(0.0).withRefractiveIndex(1.5)
                .build();

        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(-10, 10, -10), colorFactory.create(1, 1, 1));
        World world = worldFactory.create();
        world.addLight(light);
        world.addObject(floor);
        world.addObject(leftWall);
        world.addObject(rightWall);
        world.addObject(middle);
        world.addObject(right);
        world.addObject(left);
        Camera camera = cameraFactory.create(20 * 16, 20 * 9, Math.PI / 3);
        camera.setTransform(matrixOperations.viewTransform(tupleFactory.point(0, 1.5f, -5), tupleFactory.point(0, 1, 0), tupleFactory.vector(0, 1, 0)));
        Canvas canvas = camera.render(world);
        canvas.saveToFile("output.png");
    }

    @Test
    @Ignore
    public void renderConeWithCamera() {
        CheckersPattern checkersPattern = patternFactory.createCheckersPattern(colorFactory.create(0.5f, 1, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        Shape floor = shapeBuilder.createPlane().withColor(1, 0.9f, 0.9f).withSpecular(0).withReflectivity(.35f).withPattern(checkersPattern).build();

        StripePattern stripePattern = patternFactory.createStripePattern(colorFactory.create(1, 0.5f, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        stripePattern.setTransform(matrixOperations.scaling(0.25f, 0.25f, 0.25f));
        Shape leftWall = shapeBuilder.createPlane().withTranslation(0, 0, 5).withRotationY(-Math.PI / 4).withRotationX(Math.PI / 2)
                .withPattern(stripePattern).build();

        GradientPattern gradientPattern = patternFactory.createGradientPattern(colorFactory.create(1, 0.5f, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        gradientPattern.setTransform(matrixOperations.scaling(5.5f, 5.5f, 5.5f));
        Shape rightWall = shapeBuilder.createPlane().withTranslation(0, 0, 5).withRotationY(Math.PI / 4).withRotationX(Math.PI / 2)
                .withPattern(gradientPattern).build();

        StripePattern middlePattern = patternFactory.createStripePattern(colorFactory.create(0.1f, 1, 0.5f), colorFactory.create(0.1f, 0.5f, 1));
        middlePattern.setTransform(matrixOperations.scaling(0.1f, 0.1f, 0.1f).multiply(matrixOperations.rotationY(Math.PI / 3)));

        Shape middle = shapeBuilder.createCone(-1, 1, true).withTranslation(-0.5f, 1, 0.5f).withRotationY(Math.PI / 4).withRotationX(Math.PI / 2)
                .withColor(0.1f, 1, 0.5f).withDiffuse(0.7f).withSpecular(0.3f).withPattern(middlePattern).withReflectivity(0.05f).build();

        Shape right = shapeBuilder.createCone(-1, 0, false).withTranslation(1.5f, 0.5f, -0.5f).withScaling(0.5f, 0.5f, 0.5f).withColor(0.5f, 1, 0.1f)
                .withDiffuse(0.9f).withSpecular(0.3f).withReflectivity(0.0f).withTransparency(0.0).withRefractiveIndex(1.5).build();

        Shape left = shapeBuilder.createCone(0, 1, false).withTranslation(-1.5f, 0.0f, -1.00f).withScaling(0.33f, 0.33f, 0.33f)
                .withColor(1, 0.8f, 0.1f).withDiffuse(0.9f).withSpecular(0.3f).withReflectivity(0.0).withTransparency(0.0).withRefractiveIndex(1.5)
                .build();

        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(-10, 10, -10), colorFactory.create(1, 1, 1));
        World world = worldFactory.create();
        world.addLight(light);
        world.addObject(floor);
        world.addObject(leftWall);
        world.addObject(rightWall);
        world.addObject(middle);
        world.addObject(right);
        world.addObject(left);
        Camera camera = cameraFactory.create(20 * 16, 20 * 9, Math.PI / 3);
        camera.setTransform(matrixOperations.viewTransform(tupleFactory.point(0, 1.5f, -5), tupleFactory.point(0, 1, 0), tupleFactory.vector(0, 1, 0)));
        Canvas canvas = camera.render(world);
        canvas.saveToFile("output.png");
    }


    Shape hexagonCorner() {
        return shapeBuilder.createSphere()
                .withTranslation(0, 0, -1)
                .withScaling(0.25f, 0.25f, 0.25f)
                .withColor(1, 0.4f, 0.4f)
                //.withDiffuse(0.1f).withSpecular(0.3f).withReflectivity(0.90f).withTransparency(1.0).withRefractiveIndex(1.5)
                .withDiffuse(0.1f).withSpecular(0.3f).withReflectivity(0.40f).withRefractiveIndex(1.5)
                .build();
    }

    Shape hexagonEdge() {
        return shapeBuilder.createCylinder(0,1, false)
                .withTranslation(0, 0, -1)
                .withRotationY(-Math.PI / 6)
                .withRotationZ(-Math.PI / 2)
                .withScaling(0.25f, 1, 0.25f)
                .withColor(1, 0.4f, 0.4f)
                //.withDiffuse(0.1f).withSpecular(0.3f).withReflectivity(0.90f).withTransparency(1.0).withRefractiveIndex(1.5)
                .withDiffuse(0.1f).withSpecular(0.3f).withReflectivity(0.40f).withRefractiveIndex(1.5)
                .build();
    }

    Shape hexagonSide() {
        Group group = groupFactory.create();
        group.addChild(hexagonCorner());
        group.addChild(hexagonEdge());
        return group;
    }

    Shape hexagon() {
        Group group = groupFactory.create();
        for (int n = 0; n < 6; n++) {
            Shape side = hexagonSide();
            side.setTransform(matrixOperations.rotationY(n * Math.PI / 3));
            group.addChild(side);
        }
        return group;
    }

    World generate(int frame) {
        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(-10, 10, -10), colorFactory.create(1, 1, 1));
        World world = worldFactory.create();
        world.addLight(light);
        CheckersPattern checkersPattern = patternFactory.createCheckersPattern(colorFactory.create(0.5f, 1, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        checkersPattern.setTransform(matrixOperations.scaling(0.1f, 0.1f, 0.1f));
        GradientPattern gradientPattern = patternFactory.createGradientPattern(colorFactory.create(1, 0.5f, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        //gradientPattern.setTransform(matrixOperations.scaling(5.5f, 5.5f, 5.5f));
        StripePattern stripePattern = patternFactory.createStripePattern(colorFactory.create(1, 0.5f, 0.5f), colorFactory.create(0.5f, 0.5f, 1));
        stripePattern.setTransform(matrixOperations.scaling(0.25f, 0.25f, 0.25f));

        Shape floor = shapeBuilder.createSphere()
                .withScaling(50,50,50)
                .withColor(1, 0.9f, 0.9f)
                .withSpecular(0)
                //.withReflectivity(.35f)
                .withDiffuse(0.4f)
                .withPattern(stripePattern)
                .build();
        world.addObject(floor);

        Shape hex = hexagon();
        frame = (frame * 10) % 360;
        hex.setTransform(matrixOperations.translation(0,1,0).multiply(matrixOperations.rotationXDeg(-frame)));
        world.addObject(hex);
        return world;
    }

    @Test
    @Ignore
    public void renderGroupWithCamera() {
        Camera camera = cameraFactory.create(40 * 16, 40 * 9, Math.PI / 3);
        camera.setTransform(matrixOperations.viewTransform(tupleFactory.point(0, 1.5f, -5), tupleFactory.point(0, 1, 0), tupleFactory.vector(0, 1, 0)));
        camera.setNumberOfSamples(20);
        Canvas canvas = camera.render(generate(8));
        canvas.saveToFile("output.png");
    }

    @Test
    @Ignore
    public void animateGroupWithCamera() {
        Camera camera = cameraFactory.create(20 * 16, 20 * 9, Math.PI / 3);
        camera.setTransform(matrixOperations.viewTransform(tupleFactory.point(0, 1.5f, -5), tupleFactory.point(0, 1, 0), tupleFactory.vector(0, 1, 0)));
        camera.setNumberOfSamples(20);
        Animator animator = new Animator(camera, this::generate, 36, ".", "frame-");
        animator.animate();
    }

    private Color getRandomColor() {
        return colorFactory.create((float)Math.random(), (float)Math.random(), (float)Math.random());
    }

    private Shape getRandomBall(double x, double z) {
        double specular = 0.2 + (Math.random() * 0.8);
        double diffuse = 0.2 + (Math.random() * 0.8);
        return shapeBuilder.createSphere()
                .withTranslation(x * 2, 1.0f, z*2)
                .withColor(getRandomColor())
                .withDiffuse(diffuse)
                .withSpecular(specular)
                .withReflectivity(0)
                .withTransparency(0.0)
                .withRefractiveIndex(1.5)
                .build();
    }
    @Test
    @Ignore
    public void renderHugeNumberOfSpheres() {
        World world = worldFactory.create();
        Shape floor = shapeBuilder.createPlane().withColor(1, 0.9f, 0.9f).withSpecular(0).withReflectivity(.35f)
                .withPattern(patternFactory.createCheckersPattern(colorFactory.create(0.5f, 1, 0.5f), colorFactory.create(0.5f, 0.5f, 1))).build();
        world.addObject(floor);
        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(-10, 10, -10), colorFactory.create(1, 1, 1));
        world.addLight(light);

        for (int startZ=-4;startZ<14;startZ+=4) {
            Group group = groupFactory.create();
            for (int z=startZ;z<(startZ+4);z++) {
                for (int x = -8; x < 9; x++) {
                    Shape right = getRandomBall(x, z);
                    group.addChild(right);
                }
            }
            world.addObject(group);
        }

        Camera camera = cameraFactory.create(40 * 16, 40 * 9, Math.PI / 3);
        camera.setTransform(matrixOperations.viewTransform(tupleFactory.point(0, 6.0f, -15), tupleFactory.point(0, 1, 0), tupleFactory.vector(0, 1, 0)));
        camera.setNumberOfSamples(20);
        Canvas canvas = camera.render(world);
        canvas.saveToFile("output.png");
    }

    @Test
    @Ignore
    public void renderObjFile() {
        World world = worldFactory.create();
        Shape floor = shapeBuilder.createPlane().withColor(1, 0.9f, 0.9f).withSpecular(0).withReflectivity(.35f)
                .withPattern(patternFactory.createCheckersPattern(colorFactory.create(0.5f, 1, 0.5f), colorFactory.create(0.5f, 0.5f, 1))).build();
        world.addObject(floor);
        LightSource light = lightSourceFactory.createPointLight(tupleFactory.point(-10, 10, -10), colorFactory.create(1, 1, 1));
        world.addLight(light);

        List<Group> groups = objLoader.parseObjFile("src/main/resources/teapot.obj");
        for (Group group : groups) {
            group.setTransform(matrixOperations.rotationXDeg(0));
            world.addObject(group);
        }

        Camera camera = cameraFactory.create(40 * 16, 40 * 9, Math.PI / 3);
        camera.setTransform(matrixOperations.viewTransform(tupleFactory.point(0, 6.0f, -15), tupleFactory.point(0, 1, 0), tupleFactory.vector(0, 1, 0)));
        //camera.setNumberOfSamples(10);
        Canvas canvas = camera.render(world);
        canvas.saveToFile("output.png");
    }

}