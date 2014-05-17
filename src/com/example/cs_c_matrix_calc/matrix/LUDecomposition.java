package com.example.cs_c_matrix_calc.matrix;

import java.io.Serializable;

/**
 * LU Decomposition.
 * <p/>
 * For an rows-by-columns matrix A with rows >= columns, the LU decomposition is an rows-by-columns
 * unit lower triangular matrix L, an columns-by-columns upper triangular matrix U,
 * and a permutation vector pivot of length rows so that A(pivot,:) = L*U.
 * If rows < columns, then L is rows-by-rows and U is rows-by-columns.
 * <p/>
 * The LU decompostion with pivoting always exists, even if the matrix is
 * singular, so the constructor will never fail.  The primary use of the
 * LU decomposition is in the solution of square systems of simultaneous
 * linear equations.  This will fail if isNonsingular() returns false.
 */

public class LUDecomposition implements Serializable {

    private double[][] LU;
    private int rows, columns, pivotSign; // pivot sign
    private int[] pivot; // Internal storage of pivot vector.

    /**
     * LU Decomposition
     * Structure to access L, U and pivot.
     *
     * @param A Rectangular matrix
     */
    public LUDecomposition(Matrix A) {

        // Use a "left-looking", dot-product, Crout/Doolittle algorithm.

        LU = A.matrixCopy();
        rows = A.rows();
        columns = A.columns();
        pivot = new int[rows];
        for (int i = 0; i < rows; i++) {
            pivot[i] = i;
        }
        pivotSign = 1;
        double[] LUrowi;
        double[] LUcolj = new double[rows];

        // Outer loop.

        for (int j = 0; j < columns; j++) {

            // Make a copy of the j-th column to localize references.

            for (int i = 0; i < rows; i++) {
                LUcolj[i] = LU[i][j];
            }

            // Apply previous transformations.

            for (int i = 0; i < rows; i++) {
                LUrowi = LU[i];

                // Most of the time is spent in the following dot product.

                int kmax = Math.min(i, j);
                double s = 0.0;
                for (int k = 0; k < kmax; k++) {
                    s += LUrowi[k] * LUcolj[k];
                }

                LUrowi[j] = LUcolj[i] -= s;
            }

            // Find pivot and exchange if necessary.

            int p = j;
            for (int i = j + 1; i < rows; i++) {
                if (Math.abs(LUcolj[i]) > Math.abs(LUcolj[p])) {
                    p = i;
                }
            }
            if (p != j) {
                for (int k = 0; k < columns; k++) {
                    double t = LU[p][k];
                    LU[p][k] = LU[j][k];
                    LU[j][k] = t;
                }
                int k = pivot[p];
                pivot[p] = pivot[j];
                pivot[j] = k;
                pivotSign = -pivotSign;
            }

            // Compute multipliers.

            if (j < rows & LU[j][j] != 0.0) {
                for (int i = j + 1; i < rows; i++) {
                    LU[i][j] /= LU[j][j];
                }
            }
        }
    }

    /**
     * Is the matrix nonsingular?
     *
     * @return true if U, and hence A, is nonsingular.
     */
    public boolean isNonSingular() {
        for (int j = 0; j < columns; j++) {
            if (LU[j][j] == 0)
                return false;
        }
        return true;
    }

    /**
     * Return lower triangular factor
     *
     * @return L
     */
    public Matrix getL() {
        Matrix X = new Matrix(rows, columns);
        double[][] L = X.matrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i > j) {
                    L[i][j] = LU[i][j];
                } else if (i == j) {
                    L[i][j] = 1.0;
                } else {
                    L[i][j] = 0.0;
                }
            }
        }
        return X;
    }

    /**
     * Return upper triangular factor
     *
     * @return U
     */
    public Matrix getU() {
        Matrix X = new Matrix(columns, columns);
        double[][] U = X.matrix();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < columns; j++) {
                if (i <= j) {
                    U[i][j] = LU[i][j];
                } else {
                    U[i][j] = 0.0;
                }
            }
        }
        return X;
    }

    /**
     * Return pivot permutation vector
     *
     * @return pivot
     */
    public int[] getPivot() {
        int[] p = new int[rows];
        for (int i = 0; i < rows; i++) {
            p[i] = pivot[i];
        }
        return p;
    }

    /**
     * Return pivot permutation vector as a one-dimensional double array
     *
     * @return (double) pivot
     */
    public double[] getDoublePivot() {
        double[] vals = new double[rows];
        for (int i = 0; i < rows; i++) {
            vals[i] = (double) pivot[i];
        }
        return vals;
    }

    /**
     * Determinant
     *
     * @return det(A)
     * @throws IllegalArgumentException Matrix must be square
     */
    public double det() {
        if (rows != columns) {
            throw new IllegalArgumentException("Matrix must be square.");
        }
        double d = (double) pivotSign;
        for (int j = 0; j < columns; j++) {
            d *= LU[j][j];
        }
        return d;
    }

    /**
     * Solve A*X = B
     *
     * @param B A Matrix with as many rows as A and any number of columns.
     * @return X so that L*U*X = B(pivot,:)
     * @throws IllegalArgumentException Matrix row dimensions must agree.
     * @throws RuntimeException         Matrix is singular.
     */
    public Matrix solve(Matrix B) {
        if (B.rows() != rows) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        }
        if (!this.isNonSingular()) {
            throw new RuntimeException("Matrix is singular.");
        }

        // Copy right hand side with pivoting
        int nx = B.columns();
        Matrix Xmat = B.subMatrix(pivot, 0, nx - 1);
        double[][] X = Xmat.matrix();

        // Solve L*Y = B(pivot,:)
        for (int k = 0; k < columns; k++) {
            for (int i = k + 1; i < columns; i++) {
                for (int j = 0; j < nx; j++) {
                    X[i][j] -= X[k][j] * LU[i][k];
                }
            }
        }
        // Solve U*X = Y;
        for (int k = columns - 1; k >= 0; k--) {
            for (int j = 0; j < nx; j++) {
                X[k][j] /= LU[k][k];
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < nx; j++) {
                    X[i][j] -= X[k][j] * LU[i][k];
                }
            }
        }
        return Xmat;
    }
}