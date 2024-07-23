package dev.davelpz.jray.ray.operations;

import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.shape.Shape;

import java.util.List;
import java.util.Optional;

public interface RayOperations {
    List<Intersection> intersect(Shape shape, Ray ray);
    Optional<Intersection> hit(List<Intersection> intersections);
    Computation prepareComputations(Intersection intersection, Ray ray, List<Intersection> intersections);
    Ray transform(Ray ray, Matrix m);
}
