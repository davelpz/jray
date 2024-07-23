package dev.davelpz.jray.camera.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import dev.davelpz.jray.camera.Camera;
import dev.davelpz.jray.canvas.Canvas;
import dev.davelpz.jray.canvas.CanvasFactory;
import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.RayFactory;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import dev.davelpz.jray.world.World;
import me.tongfei.progressbar.ProgressBar;

public class CameraImpl implements Camera {
    private final int hSize;
    private final int vSize;
    private double fieldOfView;
    private int numberOfSamples = 1;
    private Matrix transform;
    private double halfWidth;
    private double halfHeight;
    private double pixelSize;
    private final TupleFactory tupleFactory;
    private final RayFactory rayFactory;
    private final CanvasFactory canvasFactory;

    @Inject
    public CameraImpl(@Assisted("hSize") int hSize,@Assisted("vSize")  int vSize,@Assisted("fieldOfView")  double fieldOfView, @Named("identityMatrix") Matrix identityMatrix,
            TupleFactory tupleFactory, RayFactory rayFactory, CanvasFactory canvasFactory) {
        this.hSize = hSize;
        this.vSize = vSize;
        this.tupleFactory = tupleFactory;
        this.rayFactory = rayFactory;
        this.canvasFactory = canvasFactory;
        setFieldOfView(fieldOfView);
        this.transform = identityMatrix;
    }

    @Override
    public int hSize() {
        return hSize;
    }

    @Override
    public int vSize() {
        return vSize;
    }

    @Override
    public double fieldOfView() {
        return fieldOfView;
    }

    @Override
    public void setFieldOfView(double fieldOfView) {
        this.fieldOfView = fieldOfView;
        double halfView = Math.tan(fieldOfView / 2);
        double aspect = (double) hSize / vSize;
        if (aspect >= 1) {
            halfWidth = halfView;
            halfHeight = halfView / aspect;
        } else {
            halfWidth = halfView * aspect;
            halfHeight = halfView;
        }
        pixelSize = (halfWidth * 2) / hSize;
    }

    @Override
    public double pixelSize() {
        return pixelSize;
    }

    @Override
    public void setPixelSize(double pixelSize) {
        this.pixelSize = pixelSize;
    }

    @Override
    public double halfWidth() {
        return halfWidth;
    }

    @Override
    public double halfHeight() {
        return halfHeight;
    }

    @Override
    public Matrix transform() {
        return transform;
    }

    @Override
    public void setTransform(Matrix transform) {
        this.transform = transform;
    }

    @Override
    public int numberOfSamples() {
        return numberOfSamples;
    }

    @Override
    public void setNumberOfSamples(int numberOfSamples) {
        if (numberOfSamples < 1) {
            throw new IllegalArgumentException("Number of samples must be greater than 0");
        }
        this.numberOfSamples = numberOfSamples;
    }

    @Override
    public Canvas render(World world) {
        System.out.println("Rendering a " + hSize + "x" + vSize + " image");
        Canvas image = canvasFactory.create(hSize, vSize);

        try (ProgressBar totalProgressBar = new ProgressBar("Rendering", (long) vSize * hSize * numberOfSamples)) {
            PixelStream.genStream(hSize, vSize).parallel().forEach(pixel -> {
                for (int i = 0; i < numberOfSamples; i++) {
                    Ray ray = rayForPixel(pixel.getX(), pixel.getY());
                    Color color = world.colorAt(ray, 5);
                    image.writePixel(pixel.getX(), pixel.getY(), color);
                    totalProgressBar.step();
                }
            });
        }

        return image;
    }

    @Override
    public Ray rayForPixel(int px, int py) {
        // offset from the edge of the canvas to the pixel's center
        // if anti-aliasing is enabled, then the offset is random
        double xOffset = px;
        double yOffset = py;
        if (numberOfSamples > 1) {
            xOffset += Math.random();
            yOffset += Math.random();
        } else {
            xOffset += 0.5;
            yOffset += 0.5;
        }
        xOffset *= pixelSize;
        yOffset *= pixelSize;

        // the untransformed coordinates of the pixel in world space.
        // (remember that the camera looks toward -z, so +x is to the *left*.)
        double worldX = halfWidth - xOffset;
        double worldY = halfHeight - yOffset;

        // using the camera matrix, transform the canvas point and the origin,
        // and then compute the ray's direction vector.
        // (remember that the canvas is at z=-1)
        Tuple pixel = transform.inverse().multiply(tupleFactory.point(worldX, worldY, -1));
        Tuple origin = transform.inverse().multiply(tupleFactory.point(0, 0, 0));
        Tuple direction = pixel.subtract(origin).normalize();

        return rayFactory.create(origin, direction);
    }
}
