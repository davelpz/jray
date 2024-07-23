package dev.davelpz.jray.shape;

public interface Cone extends Shape {
    double minimum();
    double maximum();
    boolean closed();
    void setClosed(boolean closed);
    void setMinimum(double minimum);
    void setMaximum(double maximum);
}
