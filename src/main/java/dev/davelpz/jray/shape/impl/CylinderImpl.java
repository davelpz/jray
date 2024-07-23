package dev.davelpz.jray.shape.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import dev.davelpz.jray.bvh.Aabb;
import dev.davelpz.jray.bvh.AabbFactory;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.shape.Cylinder;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;

import java.util.ArrayList;
import java.util.List;

public class CylinderImpl extends ShapeBase implements Cylinder {
    private boolean closed;
    private double minimum;
    private double maximum;

    @Inject
    public CylinderImpl(@Assisted("minimum") double minimum, @Assisted("maximum") double maximum, @Assisted("closed") boolean closed,
            @Named("defaultMaterial") Material material,@Named("identityMatrix") Matrix transform, TupleFactory tupleFactory, IntersectionFactory intersectionFactory,
            AabbFactory aabbFactory) {
        super(transform, material, tupleFactory, intersectionFactory, aabbFactory);
        this.minimum = minimum;
        this.maximum = maximum;
        this.closed = closed;
    }

    @Override
    public List<Intersection> intersect(Ray ray) {
        List<Intersection> intersections = new ArrayList<>();

        double a = ray.direction().x() * ray.direction().x() + ray.direction().z() * ray.direction().z();

        // ray is parallel to the y-axis
        if (Math.abs(a) < Constants.EPSILON) {
            return intersectCaps(ray, intersections);
        }

        double b = 2 * ray.origin().x() * ray.direction().x() + 2 * ray.origin().z() * ray.direction().z();
        double c = ray.origin().x() * ray.origin().x() + ray.origin().z() * ray.origin().z() - 1;
        double disc = b * b - 4 * a * c;

        // ray does not intersect the cylinder
        if (disc < 0) {
            return List.of();
        }

        // ray intersects the cylinder twice
        double t0 = (-b - Math.sqrt(disc)) / (2 * a);
        double t1 = (-b + Math.sqrt(disc)) / (2 * a);
        if (t0 > t1) {
            double temp = t0;
            t0 = t1;
            t1 = temp;
        }

        double y0 = ray.origin().y() + t0 * ray.direction().y();
        if (minimum < y0 && y0 < maximum) {
            intersections.add(intersectionFactory.create(t0, this));
        }

        double y1 = ray.origin().y() + t1 * ray.direction().y();
        if (minimum < y1 && y1 < maximum) {
            intersections.add(intersectionFactory.create(t1, this));
        }

        intersectCaps(ray, intersections);

        return intersections;
    }

    private List<Intersection> intersectCaps(Ray ray, List<Intersection> intersections) {
        // caps only matter if the cylinder is closed, and might be intersected by the ray
        if (!closed || Math.abs(ray.direction().y()) < Constants.EPSILON) {
            return intersections;
        }

        // check for an intersection with the lower end cap by intersecting
        // the ray with the plane at y=cyl.minimum
        double t = (minimum - ray.origin().y()) / ray.direction().y();
        if (checkCap(ray, t)) {
            intersections.add(intersectionFactory.create(t, this));
        }

        // check for an intersection with the upper end cap by intersecting
        // the ray with the plane at y=cyl.maximum
        t = (maximum - ray.origin().y()) / ray.direction().y();
        if (checkCap(ray, t)) {
            intersections.add(intersectionFactory.create(t, this));
        }

        return intersections;
    }

    //checks to see if the intersection at 't' is within a radius of 1 (the radius of the cylinder)
    private boolean checkCap(Ray ray, double t) {
        double x = ray.origin().x() + t * ray.direction().x();
        double z = ray.origin().z() + t * ray.direction().z();
        return (x * x + z * z) <= 1;
    }

    @Override
    public Tuple localNormalAt(Tuple worldPoint) {
        // compute the square of the distance from the y axis
        double dist = worldPoint.x() * worldPoint.x() + worldPoint.z() * worldPoint.z();

        if (dist < 1 && worldPoint.y() >= maximum - Constants.EPSILON) {
            return tupleFactory.vector(0, 1, 0);
        } else if (dist < 1 && worldPoint.y() <= minimum + Constants.EPSILON) {
            return tupleFactory.vector(0, -1, 0);
        } else {
            return tupleFactory.vector(worldPoint.x(), 0, worldPoint.z());
        }
    }

    @Override
    public Aabb buildBoundingBox() {
        return aabbFactory.create(tupleFactory.point(-1, minimum, -1), tupleFactory.point(1, maximum, 1));
    }

    @Override
    public double minimum() {
        return minimum;
    }

    @Override
    public double maximum() {
        return maximum;
    }

    @Override
    public boolean closed() {
        return closed;
    }

    @Override
    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    @Override
    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }
}
