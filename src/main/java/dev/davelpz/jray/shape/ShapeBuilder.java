package dev.davelpz.jray.shape;

import com.google.inject.Inject;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.color.ColorFactory;
import dev.davelpz.jray.material.MaterialFactory;
import dev.davelpz.jray.material.Pattern;
import dev.davelpz.jray.material.PatternFactory;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.matrix.operations.MatrixOperations;
import dev.davelpz.jray.tuple.impl.TupleFactory;

public class ShapeBuilder {
    private final MatrixOperations matrixOperations;
    private final TupleFactory tupleFactory;
    private final SphereFactory sphereFactory;
    private final CubeFactory cubeFactory;
    private final CylinderFactory cylinderFactory;
    private final ConeFactory coneFactory;
    private final ColorFactory colorFactory;
    private final MaterialFactory materialFactory;
    private final PlaneFactory planeFactory;
    private final PatternFactory patternFactory;

    @Inject
    public ShapeBuilder(MatrixOperations matrixOperations, TupleFactory tupleFactory, SphereFactory sphereFactory, CubeFactory cubeFactory,
            CylinderFactory cylinderFactory, ConeFactory coneFactory, ColorFactory colorFactory, MaterialFactory materialFactory, PlaneFactory planeFactory, PatternFactory patternFactory) {
        this.matrixOperations = matrixOperations;
        this.tupleFactory = tupleFactory;
        this.sphereFactory = sphereFactory;
        this.cubeFactory = cubeFactory;
        this.cylinderFactory = cylinderFactory;
        this.coneFactory = coneFactory;
        this.colorFactory = colorFactory;
        this.materialFactory = materialFactory;
        this.planeFactory = planeFactory;
        this.patternFactory = patternFactory;
    }

    public ShapeBuilderHelper createSphere() {
        return new ShapeBuilderHelper(sphereFactory.create());
    }

    public ShapeBuilderHelper createCube() {
        return new ShapeBuilderHelper(cubeFactory.create());
    }

    public ShapeBuilderHelper createPlane() {
        return new ShapeBuilderHelper(planeFactory.create());
    }

    public ShapeBuilderHelper createCylinder(double minimum, double maximum, boolean closed) {
        return new ShapeBuilderHelper(cylinderFactory.create(minimum, maximum, closed));
    }

    public ShapeBuilderHelper createCylinder(double minimum, double maximum) {
        return new ShapeBuilderHelper(cylinderFactory.create(minimum, maximum, true));
    }

    public ShapeBuilderHelper createCylinder() {
        return createCylinder(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);
    }

    public ShapeBuilderHelper createCone(double minimum, double maximum, boolean closed) {
        return new ShapeBuilderHelper(coneFactory.create(minimum, maximum, closed));
    }

    public ShapeBuilderHelper createCone(double minimum, double maximum) {
        return new ShapeBuilderHelper(coneFactory.create(minimum, maximum, true));
    }

    public ShapeBuilderHelper createCone() {
        return new ShapeBuilderHelper(coneFactory.create(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false));
    }

    public class ShapeBuilderHelper {
        private final Shape shape;

        public ShapeBuilderHelper(Shape shape) {
            this.shape = shape;
        }

        public Shape build() {
            return shape;
        }

        public ShapeBuilderHelper withTransform(Matrix transformation) {
            shape.setTransform(transformation);
            return this;
        }

        public ShapeBuilderHelper withTranslation(double x, double y, double z) {
            shape.setTransform(shape.transform().multiply(matrixOperations.translation(x, y, z)));
            return this;
        }

        public ShapeBuilderHelper withRotationY(double radians) {
            shape.setTransform(shape.transform().multiply(matrixOperations.rotationY(radians)));
            return this;
        }

        public ShapeBuilderHelper withRotationYDeg(double degrees) {
            shape.setTransform(shape.transform().multiply(matrixOperations.rotationYDeg(degrees)));
            return this;
        }

        public ShapeBuilderHelper withRotationX(double radians) {
            shape.setTransform(shape.transform().multiply(matrixOperations.rotationX(radians)));
            return this;
        }

        public ShapeBuilderHelper withRotationXDeg(double degrees) {
            shape.setTransform(shape.transform().multiply(matrixOperations.rotationXDeg(degrees)));
            return this;
        }

        public ShapeBuilderHelper withRotationZ(double radians) {
            shape.setTransform(shape.transform().multiply(matrixOperations.rotationZ(radians)));
            return this;
        }

        public ShapeBuilderHelper withRotationZDeg(double degrees) {
            shape.setTransform(shape.transform().multiply(matrixOperations.rotationZDeg(degrees)));
            return this;
        }

        public ShapeBuilderHelper withScaling(double x, double y, double z) {
            shape.setTransform(shape.transform().multiply(matrixOperations.scaling(x, y, z)));
            return this;
        }

        public ShapeBuilderHelper withPattern(Pattern pattern) {
            shape.material().setPattern(pattern);
            return this;
        }

        public ShapeBuilderHelper withColor(Color color) {
            shape.material().setColor(color);
            return this;
        }

        public ShapeBuilderHelper withColor(double red, double green, double blue) {
            shape.material().setColor(colorFactory.create(red, green, blue));
            return this;
        }

        public ShapeBuilderHelper withAmbient(double value) {
            shape.material().setAmbient(value);
            return this;
        }

        public ShapeBuilderHelper withDiffuse(double value) {
            shape.material().setDiffuse(value);
            return this;
        }

        public ShapeBuilderHelper withSpecular(double value) {
            shape.material().setSpecular(value);
            return this;
        }

        public ShapeBuilderHelper withShininess(double value) {
            shape.material().setShininess(value);
            return this;
        }

        public ShapeBuilderHelper withReflectivity(double value) {
            shape.material().setReflectivity(value);
            return this;
        }

        public ShapeBuilderHelper withTransparency(double value) {
            shape.material().setTransparency(value);
            return this;
        }

        public ShapeBuilderHelper withRefractiveIndex(double value) {
            shape.material().setRefractiveIndex(value);
            return this;
        }
    }
}
