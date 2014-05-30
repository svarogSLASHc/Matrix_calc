package com.example.cs_c_matrix_calc.matrix;

import java.io.Serializable;

public class Matrix implements Cloneable, Serializable {

    private double[][] matrix;
    private int rows, columns;

    /**
     * Construct an rows-by-columns matrix.
     *
     * @param rows    Number of rows.
     * @param columns Number of colums.
     */
    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        matrix = new double[rows][columns];
    }

    /**
     * Construct a matrix quickly without checking arguments
     *
     * @param matrix  Two-dimensional array of doubles.
     * @param rows    Number of rows.
     * @param columns Number of colums.
     */
    public Matrix(double[][] matrix, int rows, int columns) {
        this.matrix = matrix;
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * Construct a matrix from a 2-D array.
     *
     * @param array2D Two-dimensional array of doubles.
     * @throws IllegalArgumentException All rows must have the same length
     */
    public Matrix(double[][] array2D) {
        rows = array2D.length;
        columns = array2D[0].length;
        for (int i = 0; i < rows; i++) {
            if (array2D[i].length != columns) {
                throw new IllegalArgumentException("All rows must have the same length.");
            }
        }
        this.matrix = array2D;
    }

    /**
     * Access the internal two-dimensional array.
     *
     * @return Pointer to the two-dimensional array of matrix elements.
     */
    public double[][] matrix() {
        return matrix;
    }

    /**
     * Make a deep copy of a matrix
     */
    public Matrix copy() {
        Matrix copy = new Matrix(rows, columns);
        double[][] matrixX = copy.matrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrixX[i][j] = matrix[i][j];
            }
        }
        return copy;
    }

    /**
     * Clone the Matrix object.
     */
    public Object clone() {
        return this.copy();
    }

    /**
     * Get row dimension.
     *
     * @return rows, the number of rows.
     */
    public int rows() {
        return rows;
    }

    /**
     * Get column dimension.
     *
     * @return columns, the number of columns.
     */
    public int columns() {
        return columns;
    }

    /**
     * Get a single element.
     *
     * @param i Row index.
     * @param j Column index.
     * @return matrix(i, j)
     * @throws ArrayIndexOutOfBoundsException
     */
    public double get(int i, int j) {
        return matrix[i][j];
    }

    /**
     * Get a submatrix.
     *
     * @param i0 Initial row index
     * @param i1 Final row index
     * @param j0 Initial column index
     * @param j1 Final column index
     * @return matrix(i0:i1, j0:j1)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public Matrix subMatrix(int i0, int i1, int j0, int j1) {
        Matrix X = new Matrix(i1 - i0 + 1, j1 - j0 + 1);
        double[][] B = X.matrix();
        try {
            for (int i = i0; i <= i1; i++) {
                for (int j = j0; j <= j1; j++) {
                    B[i - i0][j - j0] = matrix[i][j];
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }

    /**
     * Get a submatrix.
     *
     * @param r  Array of row indices.
     * @param j0 Initial column index
     * @param j1 Final column index
     * @return matrix(r(:), j0:j1)
     * @throws ArrayIndexOutOfBoundsException Submatrix indices
     */
    public Matrix subMatrix(int[] r, int j0, int j1) {
        Matrix X = new Matrix(r.length, j1 - j0 + 1);
        double[][] B = X.matrix();
        try {
            for (int i = 0; i < r.length; i++) {
                for (int j = j0; j <= j1; j++) {
                    B[i][j - j0] = matrix[r[i]][j];
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException("Submatrix indices");
        }
        return X;
    }

    /**
     * Set a single element.
     *
     * @param i Row index.
     * @param j Column index.
     * @param s matrix(i,j).
     * @throws ArrayIndexOutOfBoundsException
     */
    public void set(int i, int j, double s) {
        matrix[i][j] = s;
    }

    /**
     * Matrix transpose.
     *
     * @return matrix'
     */
    public Matrix transpose() {
        Matrix X = new Matrix(columns, rows);
        double[][] C = X.matrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                C[j][i] = matrix[i][j];
            }
        }
        return X;
    }

    /**
     * Unary minus
     *
     * @return -matrix
     */
    public Matrix uminus() {
        Matrix X = new Matrix(rows, columns);
        double[][] C = X.matrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                C[i][j] = -matrix[i][j];
            }
        }
        return X;
    }

    /**
     * C = matrix + other
     *
     * @param other another matrix
     * @return matrix + other
     */
    public Matrix plus(Matrix other) {
        if (!isMatrixEqualTo(other))
            throw new IllegalArgumentException("Matrix dimensions must agree.");

        Matrix X = new Matrix(rows, columns);
        double[][] C = X.matrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                C[i][j] = matrix[i][j] + other.matrix[i][j];
            }
        }
        return X;
    }

    /**
     * C = matrix - other
     *
     * @param other another matrix
     * @return matrix - other
     */
    public Matrix minus(Matrix other) {

        if (!isMatrixEqualTo(other))
            throw new IllegalArgumentException("Matrix dimensions must agree.");
        Matrix X = new Matrix(rows, columns);
        double[][] C = X.matrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                C[i][j] = matrix[i][j] - other.matrix[i][j];
            }
        }
        return X;
    }

    /**
     * Linear algebraic matrix multiplication, matrix * other
     *
     * @param other another matrix
     * @return Matrix product, matrix * other
     * @throws IllegalArgumentException Matrix inner dimensions must agree.
     */
    public Matrix mult(Matrix other) {
        if (other.rows != columns) {
            throw new IllegalArgumentException("Matrix inner dimensions must agree.");
        }
        Matrix X = new Matrix(rows, other.columns);
        double[][] C = X.matrix();
        double[] Bcolj = new double[columns];
        for (int j = 0; j < other.columns; j++) {
            for (int k = 0; k < columns; k++) {
                Bcolj[k] = other.matrix[k][j];
            }
            for (int i = 0; i < rows; i++) {
                double[] Arowi = matrix[i];
                double s = 0;
                for (int k = 0; k < columns; k++) {
                    s += Arowi[k] * Bcolj[k];
                }
                C[i][j] = s;
            }
        }
        return X;
    }

    /**
     * Solve matrix*X = other
     *
     * @param other right hand side
     * @return solution if matrix is square, least squares solution otherwise
     */
    public Matrix solve(Matrix other) {
        return (rows == columns ? (new LUDecomposition(this)).solve(other) :
                (new QRDecomposition(this)).solve(other));
    }

    /**
     * Matrix inverse or pseudoinverse
     *
     * @return inverse(matrix) if matrix is square, pseudoinverse otherwise.
     */
    public Matrix inverse() {
        return solve(identity(rows, rows));
    }

    /**
     * Matrix determinant
     *
     * @return determinant
     */
    public double det() {
        return new LUDecomposition(this).det();
    }

    /**
     * Matrix trace.
     *
     * @return sum of the diagonal elements.
     */
    public double trace() {
        double t = 0;
        for (int i = 0; i < Math.min(rows, columns); i++) {
            t += matrix[i][i];
        }
        return t;
    }

    /**
     * Generate matrix with random elements
     *
     * @param rows    Number of rows.
     * @param columns Number of colums.
     * @return An rows-by-columns matrix with uniformly distributed random elements.
     */
    public static Matrix random(int rows, int columns) {
        Matrix A = new Matrix(rows, columns);
        double[][] X = A.matrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = Math.random();
            }
        }
        return A;
    }

    /**
     * Generate identity matrix
     *
     * @param rows    Number of rows.
     * @param columns Number of colums.
     * @return An rows-by-columns matrix with ones on the diagonal and zeros elsewhere.
     */
    public static Matrix identity(int rows, int columns) {
        Matrix A = new Matrix(rows, columns);
        double[][] X = A.matrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                X[i][j] = (i == j ? 1.0 : 0.0);
            }
        }
        return A;
    }

    /**
     * Check if size(matrix) == size(other) *
     */
    private boolean isMatrixEqualTo(Matrix other) {
        if (other.rows != rows || other.columns != columns) {
            return false;

        }
        return true;
    }
}
