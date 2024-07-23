package dev.davelpz.jray.matrix;

import dev.davelpz.jray.tuple.Tuple;

/**
 * Matrix interface
 */
public interface Matrix {
    int width();
    int height();
    double get(int row, int col);
    void set(int row, int col, double value);
    boolean equals(Matrix other);
    double determinant();
    double minor(int row, int col);
    double cofactor(int row, int col);
    boolean isInvertible();
    Tuple multiply(Tuple other);
    Matrix multiply(Matrix other);
    Matrix transpose();
    Matrix inverse();
    Matrix submatrix(int row, int col);
}
