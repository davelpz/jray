package dev.davelpz.jray.matrix.operations;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.matrix.Matrix;
import dev.davelpz.jray.matrix.MatrixFactory;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatrixOperationsTest {

    Injector injector;
    MatrixFactory factory;
    TupleFactory tupleFactory;

    MatrixOperations matrixOperations;

    @Before
    public void setUp() {
        injector = Guice.createInjector(new RayTracerModule());
        factory = injector.getInstance(MatrixFactory.class);
        tupleFactory = injector.getInstance(TupleFactory.class);
        matrixOperations = injector.getInstance(MatrixOperations.class);
    }

    @Test
    public void testTranslation() {
        Matrix matrix = matrixOperations.translation(5, -3, 2);
        Tuple tuple = tupleFactory.point(-3, 4, 5);
        Tuple expected = tupleFactory.point(2, 1, 7);
        assertTrue(expected.equals(matrix.multiply(tuple)));

        Matrix inverse = matrix.inverse();
        Tuple inverseExpected = tupleFactory.point(-8, 7, 3);
        assertTrue(inverseExpected.equals(inverse.multiply(tuple)));

        Tuple vector = tupleFactory.vector(-3, 4, 5);
        assertTrue(vector.equals(matrix.multiply(vector)));
    }

    @Test
    public void testScaling() {
        Matrix matrix = matrixOperations.scaling(2, 3, 4);
        Tuple tuple = tupleFactory.point(-4, 6, 8);
        Tuple expected = tupleFactory.point(-8, 18, 32);
        assertTrue(expected.equals(matrix.multiply(tuple)));

        Tuple vector = tupleFactory.vector(-4, 6, 8);
        Tuple vectorExpected = tupleFactory.vector(-8, 18, 32);
        assertTrue(vectorExpected.equals(matrix.multiply(vector)));

        Matrix inverse = matrix.inverse();
        Tuple inverseExpected = tupleFactory.vector(-2, 2, 2);
        assertTrue(inverseExpected.equals(inverse.multiply(vector)));

        matrix = matrixOperations.scaling(-1, 1, 1);
        tuple = tupleFactory.point(2, 3, 4);
        expected = tupleFactory.point(-2, 3, 4);
        assertTrue(expected.equals(matrix.multiply(tuple)));
    }

    @Test
    public void testRotationX() {
        Matrix matrix = matrixOperations.rotationX(Math.PI / 4);
        Tuple tuple = tupleFactory.point(0, 1, 0);
        Tuple expected = tupleFactory.point(0, Math.sqrt(2) / 2, Math.sqrt(2) / 2);
        assertTrue(expected.equals(matrix.multiply(tuple)));
        Matrix inverse = matrix.inverse();
        Tuple inverseExpected = tupleFactory.point(0, Math.sqrt(2) / 2, -(double) Math.sqrt(2) / 2.0);
        assertTrue(inverseExpected.equals(inverse.multiply(tuple)));

        matrix = matrixOperations.rotationX(Math.PI / 2);
        tuple = tupleFactory.point(0, 1, 0);
        expected = tupleFactory.point(0, 0, 1);
        assertTrue(expected.equals(matrix.multiply(tuple)));

        matrix = matrixOperations.rotationX(-Math.PI / 2);
        tuple = tupleFactory.point(0, 1, 0);
        expected = tupleFactory.point(0, 0, -1);
        assertTrue(expected.equals(matrix.multiply(tuple)));
    }

    @Test
    public void testRotationY() {
        Matrix matrix = matrixOperations.rotationY(Math.PI / 4);
        Tuple tuple = tupleFactory.point(0, 0, 1);
        Tuple expected = tupleFactory.point(Math.sqrt(2) / 2.0, 0, Math.sqrt(2) / 2);
        assertTrue(expected.equals(matrix.multiply(tuple)));
        Matrix inverse = matrix.inverse();
        Tuple inverseExpected = tupleFactory.point(-(double) Math.sqrt(2) / 2.0, 0, Math.sqrt(2) / 2);
        assertTrue(inverseExpected.equals(inverse.multiply(tuple)));

        matrix = matrixOperations.rotationY(Math.PI / 2);
        tuple = tupleFactory.point(0, 0, 1);
        expected = tupleFactory.point(1, 0, 0);
        assertTrue(expected.equals(matrix.multiply(tuple)));

        matrix = matrixOperations.rotationY(-Math.PI / 2.0);
        tuple = tupleFactory.point(0, 0, 1);
        expected = tupleFactory.point(-1, 0, 0);
        assertTrue(expected.equals(matrix.multiply(tuple)));
    }

    @Test
    public void testRotationZ() {
        Matrix matrix = matrixOperations.rotationZ(Math.PI / 4);
        Tuple tuple = tupleFactory.point(0, 1, 0);
        Tuple expected = tupleFactory.point(-(double) Math.sqrt(2) / 2.0, Math.sqrt(2) / 2, 0);
        assertTrue(expected.equals(matrix.multiply(tuple)));
        Matrix inverse = matrix.inverse();
        Tuple inverseExpected = tupleFactory.point(Math.sqrt(2) / 2, Math.sqrt(2) / 2, 0);
        assertTrue(inverseExpected.equals(inverse.multiply(tuple)));

        matrix = matrixOperations.rotationZ(Math.PI / 2);
        tuple = tupleFactory.point(0, 1, 0);
        expected = tupleFactory.point(-1, 0, 0);
        assertTrue(expected.equals(matrix.multiply(tuple)));

        matrix = matrixOperations.rotationZ(-Math.PI / 2);
        tuple = tupleFactory.point(0, 1, 0);
        expected = tupleFactory.point(1, 0, 0);
        assertTrue(expected.equals(matrix.multiply(tuple)));
    }

    @Test
    public void testShearing() {
        Matrix matrix = matrixOperations.shearing(1, 0, 0, 0, 0, 0);
        Tuple tuple = tupleFactory.point(2, 3, 4);
        Tuple expected = tupleFactory.point(5, 3, 4);
        assertTrue(expected.equals(matrix.multiply(tuple)));

        matrix = matrixOperations.shearing(0, 1, 0, 0, 0, 0);
        tuple = tupleFactory.point(2, 3, 4);
        expected = tupleFactory.point(6, 3, 4);
        assertTrue(expected.equals(matrix.multiply(tuple)));

        matrix = matrixOperations.shearing(0, 0, 1, 0, 0, 0);
        tuple = tupleFactory.point(2, 3, 4);
        expected = tupleFactory.point(2, 5, 4);
        assertTrue(expected.equals(matrix.multiply(tuple)));

        matrix = matrixOperations.shearing(0, 0, 0, 1, 0, 0);
        tuple = tupleFactory.point(2, 3, 4);
        expected = tupleFactory.point(2, 7, 4);
        assertTrue(expected.equals(matrix.multiply(tuple)));

        matrix = matrixOperations.shearing(0, 0, 0, 0, 1, 0);
        tuple = tupleFactory.point(2, 3, 4);
        expected = tupleFactory.point(2, 3, 6);
        assertTrue(expected.equals(matrix.multiply(tuple)));

        matrix = matrixOperations.shearing(0, 0, 0, 0, 0, 1);
        tuple = tupleFactory.point(2, 3, 4);
        expected = tupleFactory.point(2, 3, 7);
        assertTrue(expected.equals(matrix.multiply(tuple)));
    }

    @Test
    public void testChaining() {
        Tuple tuple = tupleFactory.point(1, 0, 1);
        Matrix a = matrixOperations.rotationX(Math.PI / 2);
        Matrix b = matrixOperations.scaling(5, 5, 5);
        Matrix c = matrixOperations.translation(10, 5, 7);

        Tuple p2 = a.multiply(tuple);
        assertTrue(tupleFactory.point(1, -1, 0).equals(p2));

        Tuple p3 = b.multiply(p2);
        assertTrue(tupleFactory.point(5, -5, 0).equals(p3));

        Tuple p4 = c.multiply(p3);
        assertTrue(tupleFactory.point(15, 0, 7).equals(p4));

        Tuple expected = tupleFactory.point(15, 0, 7);
        assertTrue(expected.equals(c.multiply(b.multiply(a.multiply(tuple)))));
    }

    @Test
    public void viewTransform1() {
        Matrix identity = injector.getInstance(Key.get(Matrix.class, Names.named("identityMatrix")));
        Tuple from = tupleFactory.point(0, 0, 0);
        Tuple to = tupleFactory.point(0, 0, -1);
        Tuple up = tupleFactory.vector(0, 1, 0);
        Matrix t = matrixOperations.viewTransform(from, to, up);
        assertTrue(identity.equals(t));
    }

    @Test
    public void viewTransform2() {
        Tuple from = tupleFactory.point(0, 0, 0);
        Tuple to = tupleFactory.point(0, 0, 1);
        Tuple up = tupleFactory.vector(0, 1, 0);
        Matrix t = matrixOperations.viewTransform(from, to, up);
        assertTrue(matrixOperations.scaling(-1, 1, -1).equals(t));
    }

    @Test
    public void viewTransform3() {
        Tuple from = tupleFactory.point(0, 0, 8);
        Tuple to = tupleFactory.point(0, 0, 0);
        Tuple up = tupleFactory.vector(0, 1, 0);
        Matrix t = matrixOperations.viewTransform(from, to, up);
        assertTrue(matrixOperations.translation(0, 0, -8).equals(t));
    }

    @Test
    public void viewTransform4() {
        Tuple from = tupleFactory.point(1, 3, 2);
        Tuple to = tupleFactory.point(4, -2, 8);
        Tuple up = tupleFactory.vector(1, 1, 0);
        Matrix t = matrixOperations.viewTransform(from, to, up);
        Matrix expected = factory.create(4, 4);
        expected.set(0, 0, -0.50709f);
        expected.set(0, 1, 0.50709f);
        expected.set(0, 2, 0.67612f);
        expected.set(0, 3, -2.36643f);
        expected.set(1, 0, 0.76772f);
        expected.set(1, 1, 0.60609f);
        expected.set(1, 2, 0.12122f);
        expected.set(1, 3, -2.82843f);
        expected.set(2, 0, -0.35857f);
        expected.set(2, 1, 0.59761f);
        expected.set(2, 2, -0.71714f);
        expected.set(2, 3, 0.00000f);
        expected.set(3, 0, 0.00000f);
        expected.set(3, 1, 0.00000f);
        expected.set(3, 2, 0.00000f);
        expected.set(3, 3, 1.00000f);
        assertTrue(expected.equals(t));
    }

}