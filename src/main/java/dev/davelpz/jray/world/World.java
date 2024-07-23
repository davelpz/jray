package dev.davelpz.jray.world;

import dev.davelpz.jray.color.Color;
import dev.davelpz.jray.light.LightSource;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.operations.Computation;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;

import java.util.List;

public interface World {
    List<LightSource> lights();
    List<Shape> objects();
    void addLight(LightSource light);
    void addObject(Shape shape);
    List<Intersection> intersect(Ray ray);
    Color shadeHit(Computation computation, int remaining);
    Color colorAt(Ray ray, int remaining);
    Color reflectedColor(Computation computation, int remaining);
    Color refractedColor(Computation computation, int remaining);
    boolean isShadowed(Tuple point);

}
