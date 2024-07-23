package dev.davelpz.jray.shape.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import dev.davelpz.jray.bvh.Aabb;
import dev.davelpz.jray.bvh.AabbFactory;
import dev.davelpz.jray.material.Material;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.ray.operations.IntersectionFactory;
import dev.davelpz.jray.shape.Shape;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ShapeBase implements Shape {
    private Matrix transform;
    private Material material;
    private Shape parent;

    protected final TupleFactory tupleFactory;
    protected final IntersectionFactory intersectionFactory;
    protected final AabbFactory aabbFactory;


    @Inject
    protected ShapeBase(@Named("identityMatrix") Matrix identityMatrix, @Named("defaultMaterial") Material material, TupleFactory tupleFactory,
            IntersectionFactory intersectionFactory, AabbFactory aabbFactory) {
        this.transform = identityMatrix;
        this.material = material;
        this.tupleFactory = tupleFactory;
        this.intersectionFactory = intersectionFactory;
        this.aabbFactory = aabbFactory;
    }

    @Override
    public Material material() {
        return material;
    }

    @Override
    public void setMaterial(Material m) {
        material = m;
    }

    @Override
    public Matrix transform() {
        return transform;
    }

    @Override
    public void setTransform(Matrix m) {
        transform = m;
    }

    @Override
    public Optional<Shape> parent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public void setParent(Shape s) {
        parent = s;
    }

    @Override
    public Tuple worldToObject(Tuple point) {
        Optional<Shape> shapeOptional = parent();
        if (shapeOptional.isPresent()) {
            point = shapeOptional.get().worldToObject(point);
        }
        return transform().inverse().multiply(point);
    }

    @Override
    public Tuple normalToWorld(Tuple normal) {
        normal = transform().inverse().transpose().multiply(normal);
        normal.setW(0);
        normal = normal.normalize();

        Optional<Shape> shapeOptional = parent();
        if (shapeOptional.isPresent()) {
            normal = shapeOptional.get().normalToWorld(normal);
        }

        return normal;
    }

    abstract Aabb buildBoundingBox();

    private final AtomicReference<Aabb> aabbAtomicReference = new AtomicReference<>(null);
    @Override
    public Aabb boundingBox() {
        Aabb box = aabbAtomicReference.get();
        if (box == null) {
            box =  buildBoundingBox();
            if (aabbAtomicReference.compareAndSet(null, box)) {
                return box;
            } else {
                return aabbAtomicReference.get();
            }
        }

        return box;
    }
}

