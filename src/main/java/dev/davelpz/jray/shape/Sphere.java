package dev.davelpz.jray.shape;


/**
 * Sphere interface models a sphere in 3D space.
 */
public interface Sphere extends Shape {
    default boolean equals(Sphere s) {
        return this.transform().equals(s.transform()) && this.material().equals(s.material());
    }
}
