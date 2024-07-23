package dev.davelpz.jray.ray;

import dev.davelpz.jray.tuple.Tuple;

/**
 * Ray interface models a ray in the ray tracer.
 * A ray is a line that starts at a point and goes in a direction.
 * The ray tracer uses rays to calculate intersections with objects in the scene.
 * The ray tracer then uses the intersections to calculate the color of the pixel on the canvas.
 */
public interface Ray {
    Tuple origin();
    Tuple direction();
    Tuple position(double t);
}
