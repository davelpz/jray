package dev.davelpz.jray.matrix;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import dev.davelpz.jray.config.Constants;
import dev.davelpz.jray.config.RayTracerModule;
import dev.davelpz.jray.tuple.Tuple;
import dev.davelpz.jray.tuple.impl.TupleFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MatrixTest {
    Injector injector;
    MatrixFactory factory;
    TupleFactory tupleFactory;

    @Before
    public void setUp() {
        injector = Guice.createInjector(new RayTracerModule());
        factory = injector.getInstance(MatrixFactory.class);
        tupleFactory = injector.getInstance(TupleFactory.class);
    }

    @Test
    public void test4x4() {
        Matrix matrix = factory.create(4, 4);
        matrix.set(0, 0, 1);
        matrix.set(0, 1, 2);
        matrix.set(0, 2, 3);
        matrix.set(0, 3, 4);
        matrix.set(1, 0, 5.5f);
        matrix.set(1, 1, 6.5f);
        matrix.set(1, 2, 7.5f);
        matrix.set(1, 3, 8.5f);
        matrix.set(2, 0, 9);
        matrix.set(2, 1, 10);
        matrix.set(2, 2, 11);
        matrix.set(2, 3, 12);
        matrix.set(3, 0, 13.5f);
        matrix.set(3, 1, 14.5f);
        matrix.set(3, 2, 15.5f);
        matrix.set(3, 3, 16.5f);
        assertEquals(4, matrix.width());
        assertEquals(4, matrix.height());
        assertEquals(1, matrix.get(0, 0), Constants.EPSILON);
        assertEquals(4, matrix.get(0, 3), Constants.EPSILON);
        assertEquals(5.5, matrix.get(1, 0), Constants.EPSILON);
        assertEquals(7.5, matrix.get(1, 2), Constants.EPSILON);
        assertEquals(11, matrix.get(2, 2), Constants.EPSILON);
        assertEquals(13.5, matrix.get(3, 0), Constants.EPSILON);
        assertEquals(15.5, matrix.get(3, 2), Constants.EPSILON);
    }

    @Test
    public void test2x2() {
        Matrix matrix = factory.create(2, 2);
        assertEquals(2, matrix.width());
        assertEquals(2, matrix.height());
        matrix.set(0, 0, -3);
        matrix.set(0, 1, 5);
        matrix.set(1, 0, 1);
        matrix.set(1, 1, -2);
        assertEquals(-3, matrix.get(0, 0), Constants.EPSILON);
        assertEquals(5, matrix.get(0, 1), Constants.EPSILON);
        assertEquals(1, matrix.get(1, 0), Constants.EPSILON);
        assertEquals(-2, matrix.get(1, 1), Constants.EPSILON);
    }

    @Test
    public void test3x3() {
        Matrix matrix = factory.create(3, 3);
        assertEquals(3, matrix.width());
        assertEquals(3, matrix.height());
        matrix.set(0, 0, -3);
        matrix.set(0, 1, 5);
        matrix.set(0, 2, 0);
        matrix.set(1, 0, 1);
        matrix.set(1, 1, -2);
        matrix.set(1, 2, -7);
        matrix.set(2, 0, 0);
        matrix.set(2, 1, 1);
        matrix.set(2, 2, 1);
        assertEquals(-3, matrix.get(0, 0), Constants.EPSILON);
        assertEquals(-2, matrix.get(1, 1), Constants.EPSILON);
        assertEquals(1, matrix.get(2, 2), Constants.EPSILON);
    }

    @Test
    public void testEquals() {
        Matrix matrix1 = factory.create(3, 3);
        assertEquals(3, matrix1.width());
        assertEquals(3, matrix1.height());
        matrix1.set(0, 0, -3);
        matrix1.set(0, 1, 5);
        matrix1.set(0, 2, 0);
        matrix1.set(1, 0, 1);
        matrix1.set(1, 1, -2);
        matrix1.set(1, 2, -7);
        matrix1.set(2, 0, 0);
        matrix1.set(2, 1, 1);
        matrix1.set(2, 2, 1);

        Matrix matrix2 = factory.create(3, 3);
        assertEquals(3, matrix2.width());
        assertEquals(3, matrix2.height());
        matrix2.set(0, 0, -3);
        matrix2.set(0, 1, 5);
        matrix2.set(0, 2, 0);
        matrix2.set(1, 0, 1);
        matrix2.set(1, 1, -2);
        matrix2.set(1, 2, -7);
        matrix2.set(2, 0, 0);
        matrix2.set(2, 1, 1);
        matrix2.set(2, 2, 1);

        assertTrue(matrix1.equals(matrix2));

        matrix2.set(1, 2, 7);
        assertFalse(matrix1.equals(matrix2));
    }

    @Test
    public void testMultiplyMatrix() {
        Matrix matrix1 = factory.create(4, 4);
        matrix1.set(0, 0, 1);
        matrix1.set(0, 1, 2);
        matrix1.set(0, 2, 3);
        matrix1.set(0, 3, 4);
        matrix1.set(1, 0, 5);
        matrix1.set(1, 1, 6);
        matrix1.set(1, 2, 7);
        matrix1.set(1, 3, 8);
        matrix1.set(2, 0, 9);
        matrix1.set(2, 1, 8);
        matrix1.set(2, 2, 7);
        matrix1.set(2, 3, 6);
        matrix1.set(3, 0, 5);
        matrix1.set(3, 1, 4);
        matrix1.set(3, 2, 3);
        matrix1.set(3, 3, 2);

        Matrix matrix2 = factory.create(4, 4);
        matrix2.set(0, 0, -2);
        matrix2.set(0, 1, 1);
        matrix2.set(0, 2, 2);
        matrix2.set(0, 3, 3);
        matrix2.set(1, 0, 3);
        matrix2.set(1, 1, 2);
        matrix2.set(1, 2, 1);
        matrix2.set(1, 3, -1);
        matrix2.set(2, 0, 4);
        matrix2.set(2, 1, 3);
        matrix2.set(2, 2, 6);
        matrix2.set(2, 3, 5);
        matrix2.set(3, 0, 1);
        matrix2.set(3, 1, 2);
        matrix2.set(3, 2, 7);
        matrix2.set(3, 3, 8);

        Matrix matrix3 = matrix1.multiply(matrix2);
        assertEquals(4, matrix3.width());
        assertEquals(4, matrix3.height());
        assertEquals(20, matrix3.get(0, 0), Constants.EPSILON);
        assertEquals(22, matrix3.get(0, 1), Constants.EPSILON);
        assertEquals(50, matrix3.get(0, 2), Constants.EPSILON);
        assertEquals(48, matrix3.get(0, 3), Constants.EPSILON);
        assertEquals(44, matrix3.get(1, 0), Constants.EPSILON);
        assertEquals(54, matrix3.get(1, 1), Constants.EPSILON);
        assertEquals(114, matrix3.get(1, 2), Constants.EPSILON);
        assertEquals(108, matrix3.get(1, 3), Constants.EPSILON);
        assertEquals(40, matrix3.get(2, 0), Constants.EPSILON);
        assertEquals(58, matrix3.get(2, 1), Constants.EPSILON);
        assertEquals(110, matrix3.get(2, 2), Constants.EPSILON);
        assertEquals(102, matrix3.get(2, 3), Constants.EPSILON);
        assertEquals(16, matrix3.get(3, 0), Constants.EPSILON);
        assertEquals(26, matrix3.get(3, 1), Constants.EPSILON);
        assertEquals(46, matrix3.get(3, 2), Constants.EPSILON);
        assertEquals(42, matrix3.get(3, 3), Constants.EPSILON);
    }

    @Test
    public void testMultiplyTuple() {
        Matrix matrix = factory.create(4, 4);
        matrix.set(0, 0, 1);
        matrix.set(0, 1, 2);
        matrix.set(0, 2, 3);
        matrix.set(0, 3, 4);
        matrix.set(1, 0, 2);
        matrix.set(1, 1, 4);
        matrix.set(1, 2, 4);
        matrix.set(1, 3, 2);
        matrix.set(2, 0, 8);
        matrix.set(2, 1, 6);
        matrix.set(2, 2, 4);
        matrix.set(2, 3, 1);
        matrix.set(3, 0, 0);
        matrix.set(3, 1, 0);
        matrix.set(3, 2, 0);
        matrix.set(3, 3, 1);

        Tuple tuple = tupleFactory.create(1, 2, 3, 1);
        Tuple result = matrix.multiply(tuple);
        assertEquals(18, result.x(), Constants.EPSILON);
        assertEquals(24, result.y(), Constants.EPSILON);
        assertEquals(33, result.z(), Constants.EPSILON);
        assertEquals(1, result.w(), Constants.EPSILON);
    }

    @Test
    public void testIdentity() {
        Matrix identity = injector.getInstance(Key.get(Matrix.class, Names.named("identityMatrix")));
        assertNotNull(identity);
        assertEquals(4, identity.width());
        assertEquals(4, identity.height());
        assertEquals(1, identity.get(0, 0), Constants.EPSILON);
        assertEquals(0, identity.get(0, 1), Constants.EPSILON);
        assertEquals(0, identity.get(0, 2), Constants.EPSILON);
        assertEquals(0, identity.get(0, 3), Constants.EPSILON);
        assertEquals(0, identity.get(1, 0), Constants.EPSILON);
        assertEquals(1, identity.get(1, 1), Constants.EPSILON);
        assertEquals(0, identity.get(1, 2), Constants.EPSILON);
        assertEquals(0, identity.get(1, 3), Constants.EPSILON);
        assertEquals(0, identity.get(2, 0), Constants.EPSILON);
        assertEquals(0, identity.get(2, 1), Constants.EPSILON);
        assertEquals(1, identity.get(2, 2), Constants.EPSILON);
        assertEquals(0, identity.get(2, 3), Constants.EPSILON);
        assertEquals(0, identity.get(3, 0), Constants.EPSILON);
        assertEquals(0, identity.get(3, 1), Constants.EPSILON);
        assertEquals(0, identity.get(3, 2), Constants.EPSILON);
        assertEquals(1, identity.get(3, 3), Constants.EPSILON);
    }

    @Test
    public void testTranspose() {
        Matrix matrix = factory.create(4, 4);
        matrix.set(0, 0, 0);
        matrix.set(0, 1, 9);
        matrix.set(0, 2, 3);
        matrix.set(0, 3, 0);
        matrix.set(1, 0, 9);
        matrix.set(1, 1, 8);
        matrix.set(1, 2, 0);
        matrix.set(1, 3, 8);
        matrix.set(2, 0, 1);
        matrix.set(2, 1, 8);
        matrix.set(2, 2, 5);
        matrix.set(2, 3, 3);
        matrix.set(3, 0, 0);
        matrix.set(3, 1, 0);
        matrix.set(3, 2, 5);
        matrix.set(3, 3, 8);

        Matrix transposed = matrix.transpose();
        assertEquals(4, transposed.width());
        assertEquals(4, transposed.height());
        assertEquals(0, transposed.get(0, 0), Constants.EPSILON);
        assertEquals(9, transposed.get(0, 1), Constants.EPSILON);
        assertEquals(1, transposed.get(0, 2), Constants.EPSILON);
        assertEquals(0, transposed.get(0, 3), Constants.EPSILON);
        assertEquals(9, transposed.get(1, 0), Constants.EPSILON);
        assertEquals(8, transposed.get(1, 1), Constants.EPSILON);
        assertEquals(8, transposed.get(1, 2), Constants.EPSILON);
        assertEquals(0, transposed.get(1, 3), Constants.EPSILON);
        assertEquals(3, transposed.get(2, 0), Constants.EPSILON);
        assertEquals(0, transposed.get(2, 1), Constants.EPSILON);
        assertEquals(5, transposed.get(2, 2), Constants.EPSILON);
        assertEquals(5, transposed.get(2, 3), Constants.EPSILON);
        assertEquals(0, transposed.get(3, 0), Constants.EPSILON);
        assertEquals(8, transposed.get(3, 1), Constants.EPSILON);
        assertEquals(3, transposed.get(3, 2), Constants.EPSILON);
        assertEquals(8, transposed.get(3, 3), Constants.EPSILON);
    }

    @Test
    public void testDeterminant2x2() {
        Matrix matrix = factory.create(2, 2);
        matrix.set(0, 0, 1);
        matrix.set(0, 1, 5);
        matrix.set(1, 0, -3);
        matrix.set(1, 1, 2);
        assertEquals(17, matrix.determinant(), Constants.EPSILON);
    }

    @Test
    public void testDeterminant3x3() {
        Matrix matrix = factory.create(3, 3);
        matrix.set(0, 0, 1);
        matrix.set(0, 1, 2);
        matrix.set(0, 2, 6);
        matrix.set(1, 0, -5);
        matrix.set(1, 1, 8);
        matrix.set(1, 2, -4);
        matrix.set(2, 0, 2);
        matrix.set(2, 1, 6);
        matrix.set(2, 2, 4);
        assertEquals(-196, matrix.determinant(), Constants.EPSILON);
    }

    @Test
    public void testDeterminant4x4() {
        Matrix matrix = factory.create(4, 4);
        matrix.set(0, 0, -2);
        matrix.set(0, 1, -8);
        matrix.set(0, 2, 3);
        matrix.set(0, 3, 5);
        matrix.set(1, 0, -3);
        matrix.set(1, 1, 1);
        matrix.set(1, 2, 7);
        matrix.set(1, 3, 3);
        matrix.set(2, 0, 1);
        matrix.set(2, 1, 2);
        matrix.set(2, 2, -9);
        matrix.set(2, 3, 6);
        matrix.set(3, 0, -6);
        matrix.set(3, 1, 7);
        matrix.set(3, 2, 7);
        matrix.set(3, 3, -9);
        assertEquals(-4071, matrix.determinant(), Constants.EPSILON);
    }

    @Test
    public void testIsInvertible() {
        Matrix matrix = factory.create(4, 4);
        matrix.set(0, 0, 6);
        matrix.set(0, 1, 4);
        matrix.set(0, 2, 4);
        matrix.set(0, 3, 4);
        matrix.set(1, 0, 5);
        matrix.set(1, 1, 5);
        matrix.set(1, 2, 7);
        matrix.set(1, 3, 6);
        matrix.set(2, 0, 4);
        matrix.set(2, 1, -9);
        matrix.set(2, 2, 3);
        matrix.set(2, 3, -7);
        matrix.set(3, 0, 9);
        matrix.set(3, 1, 1);
        matrix.set(3, 2, 7);
        matrix.set(3, 3, -6);
        assertEquals(-2120, matrix.determinant(), Constants.EPSILON);
        assertTrue(matrix.isInvertible());

        matrix = factory.create(4, 4);
        matrix.set(0, 0, -4);
        matrix.set(0, 1, 2);
        matrix.set(0, 2, -2);
        matrix.set(0, 3, -3);
        matrix.set(1, 0, 9);
        matrix.set(1, 1, 6);
        matrix.set(1, 2, 2);
        matrix.set(1, 3, 6);
        matrix.set(2, 0, 0);
        matrix.set(2, 1, -5);
        matrix.set(2, 2, 1);
        matrix.set(2, 3, -5);
        matrix.set(3, 0, 0);
        matrix.set(3, 1, 0);
        matrix.set(3, 2, 0);
        matrix.set(3, 3, 0);
        assertEquals(0, matrix.determinant(), Constants.EPSILON);
        assertFalse(matrix.isInvertible());
    }

    @Test
    public void testSubmatrix1() {
        Matrix matrix = factory.create(3, 3);
        matrix.set(0, 0, 1);
        matrix.set(0, 1, 5);
        matrix.set(0, 2, 0);
        matrix.set(1, 0, -3);
        matrix.set(1, 1, 2);
        matrix.set(1, 2, 7);
        matrix.set(2, 0, 0);
        matrix.set(2, 1, 6);
        matrix.set(2, 2, -3);
        Matrix submatrix = matrix.submatrix(0, 2);
        assertEquals(2, submatrix.width());
        assertEquals(2, submatrix.height());
        assertEquals(-3, submatrix.get(0, 0), Constants.EPSILON);
        assertEquals(2, submatrix.get(0, 1), Constants.EPSILON);
        assertEquals(0, submatrix.get(1, 0), Constants.EPSILON);
        assertEquals(6, submatrix.get(1, 1), Constants.EPSILON);
    }

    @Test
    public void testSubmatrix2() {
        Matrix matrix = factory.create(4, 4);
        matrix.set(0, 0, -6);
        matrix.set(0, 1, 1);
        matrix.set(0, 2, 1);
        matrix.set(0, 3, 6);
        matrix.set(1, 0, -8);
        matrix.set(1, 1, 5);
        matrix.set(1, 2, 8);
        matrix.set(1, 3, 6);
        matrix.set(2, 0, -1);
        matrix.set(2, 1, 0);
        matrix.set(2, 2, 8);
        matrix.set(2, 3, 2);
        matrix.set(3, 0, -7);
        matrix.set(3, 1, 1);
        matrix.set(3, 2, -1);
        matrix.set(3, 3, 1);
        Matrix submatrix = matrix.submatrix(2, 1);
        assertEquals(3, submatrix.width());
        assertEquals(3, submatrix.height());
        assertEquals(-6, submatrix.get(0, 0), Constants.EPSILON);
        assertEquals(1, submatrix.get(0, 1), Constants.EPSILON);
        assertEquals(6, submatrix.get(0, 2), Constants.EPSILON);
        assertEquals(-8, submatrix.get(1, 0), Constants.EPSILON);
        assertEquals(8, submatrix.get(1, 1), Constants.EPSILON);
        assertEquals(6, submatrix.get(1, 2), Constants.EPSILON);
        assertEquals(-7, submatrix.get(2, 0), Constants.EPSILON);
        assertEquals(-1, submatrix.get(2, 1), Constants.EPSILON);
        assertEquals(1, submatrix.get(2, 2), Constants.EPSILON);
    }

    @Test
    public void testMinor() {
        Matrix matrix = factory.create(3, 3);
        matrix.set(0, 0, 3);
        matrix.set(0, 1, 5);
        matrix.set(0, 2, 0);
        matrix.set(1, 0, 2);
        matrix.set(1, 1, -1);
        matrix.set(1, 2, -7);
        matrix.set(2, 0, 6);
        matrix.set(2, 1, -1);
        matrix.set(2, 2, 5);
        assertEquals(25, matrix.minor(1, 0), Constants.EPSILON);
        Matrix sub = matrix.submatrix(1, 0);
        assertEquals(25, sub.determinant(), Constants.EPSILON);
    }

    @Test
    public void testCofactor() {
        Matrix matrix = factory.create(3, 3);
        matrix.set(0, 0, 3);
        matrix.set(0, 1, 5);
        matrix.set(0, 2, 0);
        matrix.set(1, 0, 2);
        matrix.set(1, 1, -1);
        matrix.set(1, 2, -7);
        matrix.set(2, 0, 6);
        matrix.set(2, 1, -1);
        matrix.set(2, 2, 5);
        assertEquals(-12, matrix.cofactor(0, 0), Constants.EPSILON);
        assertEquals(-25, matrix.cofactor(1, 0), Constants.EPSILON);
        assertEquals(-12, matrix.minor(0, 0), Constants.EPSILON);
        assertEquals(25, matrix.minor(1, 0), Constants.EPSILON);
    }

    @Test
    public void testInverse() {
        Matrix matrix = factory.create(4, 4);
        matrix.set(0, 0, -5);
        matrix.set(0, 1, 2);
        matrix.set(0, 2, 6);
        matrix.set(0, 3, -8);
        matrix.set(1, 0, 1);
        matrix.set(1, 1, -5);
        matrix.set(1, 2, 1);
        matrix.set(1, 3, 8);
        matrix.set(2, 0, 7);
        matrix.set(2, 1, 7);
        matrix.set(2, 2, -6);
        matrix.set(2, 3, -7);
        matrix.set(3, 0, 1);
        matrix.set(3, 1, -3);
        matrix.set(3, 2, 7);
        matrix.set(3, 3, 4);
        Matrix inverse = matrix.inverse();
        assertEquals(532, matrix.determinant(), Constants.EPSILON);
        assertEquals(-160, matrix.cofactor(2, 3), Constants.EPSILON);
        assertEquals(-160.0 / 532.0, inverse.get(3, 2), Constants.EPSILON);
        assertEquals(105, matrix.cofactor(3, 2), Constants.EPSILON);
        assertEquals(105.0 / 532.0, inverse.get(2, 3), Constants.EPSILON);
        Matrix expected = factory.create(4, 4);
        expected.set(0, 0, 0.21805f);
        expected.set(0, 1, 0.45113f);
        expected.set(0, 2, 0.24060f);
        expected.set(0, 3, -0.04511f);
        expected.set(1, 0, -0.80827f);
        expected.set(1, 1, -1.45677f);
        expected.set(1, 2, -0.44361f);
        expected.set(1, 3, 0.52068f);
        expected.set(2, 0, -0.07895f);
        expected.set(2, 1, -0.22368f);
        expected.set(2, 2, -0.05263f);
        expected.set(2, 3, 0.19736f);
        expected.set(3, 0, -0.52256f);
        expected.set(3, 1, -0.81391f);
        expected.set(3, 2, -0.30075f);
        expected.set(3, 3, 0.30639f);
        assertTrue(expected.equals(inverse));

        matrix = factory.create(4, 4);
        matrix.set(0, 0, 8);
        matrix.set(0, 1, -5);
        matrix.set(0, 2, 9);
        matrix.set(0, 3, 2);
        matrix.set(1, 0, 7);
        matrix.set(1, 1, 5);
        matrix.set(1, 2, 6);
        matrix.set(1, 3, 1);
        matrix.set(2, 0, -6);
        matrix.set(2, 1, 0);
        matrix.set(2, 2, 9);
        matrix.set(2, 3, 6);
        matrix.set(3, 0, -3);
        matrix.set(3, 1, 0);
        matrix.set(3, 2, -9);
        matrix.set(3, 3, -4);
        inverse = matrix.inverse();
        expected = factory.create(4, 4);
        expected.set(0, 0, -0.15385f);
        expected.set(0, 1, -0.15385f);
        expected.set(0, 2, -0.28205f);
        expected.set(0, 3, -0.53846f);
        expected.set(1, 0, -0.07692f);
        expected.set(1, 1, 0.12308f);
        expected.set(1, 2, 0.02564f);
        expected.set(1, 3, 0.03077f);
        expected.set(2, 0, 0.35897f);
        expected.set(2, 1, 0.35897f);
        expected.set(2, 2, 0.43590f);
        expected.set(2, 3, 0.92308f);
        expected.set(3, 0, -0.69231f);
        expected.set(3, 1, -0.69231f);
        expected.set(3, 2, -0.76923f);
        expected.set(3, 3, -1.92308f);
        assertTrue(expected.equals(inverse));

        matrix = factory.create(4, 4);
        matrix.set(0, 0, 9);
        matrix.set(0, 1, 3);
        matrix.set(0, 2, 0);
        matrix.set(0, 3, 9);
        matrix.set(1, 0, -5);
        matrix.set(1, 1, -2);
        matrix.set(1, 2, -6);
        matrix.set(1, 3, -3);
        matrix.set(2, 0, -4);
        matrix.set(2, 1, 9);
        matrix.set(2, 2, 6);
        matrix.set(2, 3, 4);
        matrix.set(3, 0, -7);
        matrix.set(3, 1, 6);
        matrix.set(3, 2, 6);
        matrix.set(3, 3, 2);
        inverse = matrix.inverse();
        expected = factory.create(4, 4);
        expected.set(0, 0, -0.04074f);
        expected.set(0, 1, -0.07778f);
        expected.set(0, 2, 0.14444f);
        expected.set(0, 3, -0.22222f);
        expected.set(1, 0, -0.07778f);
        expected.set(1, 1, 0.03333f);
        expected.set(1, 2, 0.36667f);
        expected.set(1, 3, -0.33333f);
        expected.set(2, 0, -0.02901f);
        expected.set(2, 1, -0.14630f);
        expected.set(2, 2, -0.10926f);
        expected.set(2, 3, 0.12963f);
        expected.set(3, 0, 0.17778f);
        expected.set(3, 1, 0.06667f);
        expected.set(3, 2, -0.26667f);
        expected.set(3, 3, 0.33333f);
        assertTrue(expected.equals(inverse));

        Matrix matrix1 = factory.create(4, 4);
        matrix1.set(0, 0, 3);
        matrix1.set(0, 1, -9);
        matrix1.set(0, 2, 7);
        matrix1.set(0, 3, 3);
        matrix1.set(1, 0, 3);
        matrix1.set(1, 1, -8);
        matrix1.set(1, 2, 2);
        matrix1.set(1, 3, -9);
        matrix1.set(2, 0, -4);
        matrix1.set(2, 1, 4);
        matrix1.set(2, 2, 4);
        matrix1.set(2, 3, 1);
        matrix1.set(3, 0, -6);
        matrix1.set(3, 1, 5);
        matrix1.set(3, 2, -1);
        matrix1.set(3, 3, 1);
        Matrix matrix2 = factory.create(4, 4);
        matrix2.set(0, 0, 8);
        matrix2.set(0, 1, 2);
        matrix2.set(0, 2, 2);
        matrix2.set(0, 3, 2);
        matrix2.set(1, 0, 3);
        matrix2.set(1, 1, -1);
        matrix2.set(1, 2, 7);
        matrix2.set(1, 3, 0);
        matrix2.set(2, 0, 7);
        matrix2.set(2, 1, 0);
        matrix2.set(2, 2, 5);
        matrix2.set(2, 3, 4);
        matrix2.set(3, 0, 6);
        matrix2.set(3, 1, -2);
        matrix2.set(3, 2, 0);
        matrix2.set(3, 3, 5);
        Matrix matrix3 = matrix1.multiply(matrix2);
        Matrix inverseMatrix2 = matrix2.inverse();
        assertTrue(matrix1.equals(matrix3.multiply(inverseMatrix2)));
    }
}