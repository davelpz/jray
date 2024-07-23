package dev.davelpz.jray.matrix.impl;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.matrix.MatrixFactory;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;

import java.util.concurrent.atomic.AtomicReference;

public class MatrixImpl implements Matrix {
    private final int width;
    private final int height;
    private final double[][] matrix;
    private final MatrixFactory factory;
    private final TupleFactory tupleFactory;

    @Inject
    public MatrixImpl(@Assisted("width") int width, @Assisted("height") int height, MatrixFactory factory, TupleFactory tupleFactory) {
        this.width = width;
        this.height = height;
        this.factory = factory;
        this.tupleFactory = tupleFactory;
        matrix = new double[height][width];
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public int height() {
        return height;
    }

    @Override
    public double get(int row, int col) {
        return matrix[row][col];
    }

    @Override
    public void set(int row, int col, double value) {
        matrix[row][col] = value;
    }

    @Override
    public boolean equals(Matrix other) {

        if (width != other.width() || height != other.height()) {
            return false;
        }

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (Math.abs(matrix[row][col] - other.get(row, col)) > Constants.EPSILON) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Matrix multiply(Matrix other) {
        Matrix result = factory.create(width, other.height());
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < other.width(); col++) {
                double sum = 0;
                for (int i = 0; i < width; i++) {
                    sum += matrix[row][i] * other.get(i, col);
                }
                result.set(row, col, sum);
            }
        }
        return result;
    }

    @Override
    public Tuple multiply(Tuple other) {
        double[] tupleValues = new double[4];

        for (int row = 0; row < height; row++) {
            double[] rowValues = matrix[row];
            tupleValues[row] = rowValues[0] * other.x() + rowValues[1] * other.y() + rowValues[2] * other.z() + rowValues[3] * other.w();
        }

        return tupleFactory.create(tupleValues[0], tupleValues[1], tupleValues[2], tupleValues[3]);
    }

    private final AtomicReference<Matrix> cachedTranspose = new AtomicReference<>();
    @Override
    public Matrix transpose() {
        Matrix result = cachedTranspose.get();
        if (result == null) {
            result = transposeImpl();
            if (cachedTranspose.compareAndSet(null, result)) {
                return result;
            } else {
                return cachedTranspose.get();
            }
        }
        return result;
    }

    private Matrix transposeImpl() {
        Matrix result = factory.create(height, width);
        for (int row = 0; row < height; row++) {
            double[] rowValues = matrix[row];
            for (int col = 0; col < width; col++) {
                result.set(col, row, rowValues[col]);
            }
        }
        return result;
    }

    //Caching inverse calculation results in a ~20x speedup :-)
    private final AtomicReference<Matrix> cachedInverse = new AtomicReference<>();
    @Override
    public Matrix inverse() {
        Matrix result = cachedInverse.get();
        if (result == null) {
            result = inverseImpl();
            if (cachedInverse.compareAndSet(null, result)) {
                return result;
            } else {
                return cachedInverse.get();
            }
        }
        return result;
    }

    private Matrix inverseImpl() {
        Matrix result = factory.create(width, height);
        double determinant = determinant();
        if (determinant == 0) {
            throw new RuntimeException("Matrix is not invertible");
        }
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result.set(col, row, cofactor(row, col) / determinant);
            }
        }
        return result;
    }

    @Override
    public Matrix submatrix(int row, int col) {
        Matrix result = factory.create(width - 1, height - 1);
        int resultRow = 0;
        for (int i = 0; i < height; i++) {
            if (i == row) {
                continue;
            }
            int resultCol = 0;
            for (int j = 0; j < width; j++) {
                if (j == col) {
                    continue;
                }
                result.set(resultRow, resultCol, matrix[i][j]);
                resultCol++;
            }
            resultRow++;
        }
        return result;
    }

    @Override
    public double determinant() {
        if (width == 2 && height == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        } else {
            double sum = 0;
            for (int col = 0; col < width; col++) {
                sum += matrix[0][col] * cofactor(0, col);
            }
            return sum;
        }
    }

    @Override
    public double cofactor(int row, int col) {
        return minor(row, col) * ((row + col) % 2 == 0 ? 1 : -1);
    }

    @Override
    public double minor(int row, int col) {
        return submatrix(row, col).determinant();
    }

    @Override
    public boolean isInvertible() {
        return determinant() != 0;
    }
}
