package dev.davelpz.jray.shape.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.davelpz.jray.bvh.Aabb;
import dev.davelpz.jray.bvh.AabbFactory;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.Ray;
import dev.davelpz.jray.ray.operations.Intersection;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.ray.operations.RayOperations;
import dev.davelpz.jray.shape.Group;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class GroupImpl extends ShapeBase implements Group {

    private final RayOperations rayOperations;
    private final List<Shape> children = new ArrayList<>();

    @Inject
    public GroupImpl(@Named("identityMatrix") Matrix identityMatrix, @Named("defaultMaterial") Material material, TupleFactory tupleFactory,
            IntersectionFactory intersectionFactory, RayOperations rayOperations, AabbFactory aabbFactory) {
        super(identityMatrix, material, tupleFactory, intersectionFactory, aabbFactory);
        this.rayOperations = rayOperations;
    }

    @Override
    public List<Intersection> intersect(Ray ray) {
        List<Intersection> intersections = new ArrayList<>();

        if (boundingBox().intersect(ray)) {
            for (Shape child : children) {
                intersections.addAll(rayOperations.intersect(child, ray));
            }
            intersections.sort(Comparator.comparingDouble(Intersection::t));
        }

        return intersections;
    }

    @Override
    public Tuple localNormalAt(Tuple objectPoint) {
        //return objectPoint.subtract(tupleFactory.point(0, 0, 0));
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void addChild(Shape shape) {
        children.add(shape);
        shape.setParent(this);
    }

    @Override
    public void addChildren(List<Shape> shapes) {
        children.addAll(shapes);
        shapes.forEach(shape -> shape.setParent(this));
    }

    @Override
    public Shape getChild(int index) {
        return children.get(index);
    }

    @Override
    public void removeChild(Shape shape) {
        children.remove(shape);
        shape.setParent(null);
    }

    @Override
    public int getChildrenCount() {
        return children.size();
    }

    @Override
    public void clearChildren() {
        children.clear();
    }

    @Override
    public Iterator<Shape> iterator() {
        return children.iterator();
    }

    @Override
    public void forEach(Consumer<? super Shape> action) {
        children.forEach(action);
    }

    @Override
    public Spliterator<Shape> spliterator() {
        return children.spliterator();
    }

    @Override
    Aabb buildBoundingBox() {
        Aabb box = aabbFactory.create(tupleFactory.point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
                tupleFactory.point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        for (Shape child : children) {
            Aabb childBox = child.boundingBox();
            box.adjust(childBox.applyTransform(child.transform()));
        }
        return box;
    }
}
