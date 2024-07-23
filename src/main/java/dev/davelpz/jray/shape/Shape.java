package dev.davelpz.jray.shape;

import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.bvh.Aabb;
import java.util.List;
import java.util.Optional;

public interface Shape {
    Optional<Shape> parent();
    void setParent(Shape s);
    Material material();
    void setMaterial(Material m);
    Matrix transform();
    void setTransform(Matrix m);
    List<Intersection> intersect(Ray ray);
    Tuple localNormalAt(Tuple worldPoint);

    default Tuple normalAt(Tuple worldPoint) {
        Tuple localPoint = worldToObject(worldPoint);
        Tuple localNormal = localNormalAt(localPoint);
        return normalToWorld(localNormal);
    }

    Tuple worldToObject(Tuple point);

    Tuple normalToWorld(Tuple point);

    Aabb boundingBox();

    default boolean equals(Shape s) {
        return this.transform().equals(s.transform()) && this.material().equals(s.material());
    }
}
