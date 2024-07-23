package dev.davelpz.jray.shape;

import java.util.List;

public interface Group extends Shape, Iterable<Shape> {
    void addChild(Shape shape);
    void addChildren(List<Shape> shapes);
    Shape getChild(int index);
    void removeChild(Shape shape);
    int getChildrenCount();
    void clearChildren();
}
