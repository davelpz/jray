package dev.davelpz.jray.camera;

import dev.davelpz.jray.canvas.Canvas;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.world.World;

/**
 * Simple model a camera in the world.
 * Ray tracing is a reverse process, so we need to shoot rays from the camera into the world.
 * The camera is located at the origin, and points toward the negative z-axis.
 */
public interface Camera {
    /**
     * @return the horizontal size of the canvas in pixels
     */
    int hSize();

    /**
     * @return the vertical size of the canvas in pixels
     */
    int vSize();

    /**
     * @return the field of view in radians
     * The camera's field of view is the angle between the left and right edges of the canvas.
     */
    double fieldOfView();

    void setFieldOfView(double fieldOfView);

    /**
     * @return the size of a pixel in world units
     */
    double pixelSize();
    void setPixelSize(double pixelSize);

    /**
     * @return the half of the canvas width the camera can see
     */
    double halfWidth();

    /**
     * @return the half of the canvas height the camera can see
     */
    double halfHeight();

    /**
     * @return the transformation matrix that places the camera in the world
     */
    Matrix transform();
    void setTransform(Matrix transform);

    int numberOfSamples();
    void setNumberOfSamples(int numberOfSamples);

    /**
     * @param world the world to render
     * @return a canvas with the rendered image
     */
    Canvas render(World world);

    /**
     * @param px the x-coordinate of the pixel
     * @param py the y-coordinate of the pixel
     * @return the ray that passes through the pixel starting at the camera
     */
    Ray rayForPixel(int px, int py);
}
