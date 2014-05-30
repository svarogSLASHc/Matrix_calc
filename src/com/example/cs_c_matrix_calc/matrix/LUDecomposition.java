package com.example.cs_c_matrix_calc.matrix;

import java.io.Serializable;

public class LUDecomposition implements Serializable {

    private static final double EPS = 1e-6;
    private Matrix LU;
    private int rows, columns, pivotSign; // pivot sign
    private int[] pivot; // Internal storage of pivot vector.

    /**
     * LU Decomposition
     * Structure to access L, U and pivot.
     *
     * @param A Rectangular matrix
     */
    public LUDecomposition(Matrix A) {

        LU = A.copy();
        rows = A.rows();
        columns = A.columns();
        pivot = new int[rows];
        for (int i = 0; i < rows; i++) {
            pivot[i] = i;
        }
        pivotSign = 1;
        double[] rowI;
        double[] columnJ = new double[rows];

        for (int j = 0; j < columns; j++) {

            for (int i = 0; i < rows; i++) {
                columnJ[i] = LU.get(i, j);
            }

            for (int i = 0; i < rows; i++) {
                rowI = LU.matrix()[i];

                int kmax = Math.min(i, j);
                double s = 0.0;
                for (int k = 0; k < kmax; k++) {
                    s += rowI[k] * columnJ[k];
                }

                rowI[j] = columnJ[i] -= s;
            }

            int p = j;
            for (int i = j + 1; i < rows; i++) {
                if (Math.abs(columnJ[i]) > Math.abs(columnJ[p])) {
                    p = i;
                }
            }
            if (p != j) {
                for (int k = 0; k < columns; k++) {
                    double t = LU.get(p, k);
                    LU.set(p, k, LU.get(j, k));
                    LU.set(j, k, t);
                }
                int k = pivot[p];
                pivot[p] = pivot[j];
                pivot[j] = k;
                pivotSign = -pivotSign;
            }

            if (j < rows & LU.get(j, j) != 0.0) {
                for (int i = j + 1; i < rows; i++) {
                    LU.set(i, j, LU.get(i, j) / LU.get(j, j));
                }
            }
        }
    }

    public boolean isNonSingular() {
        for (int j = 0; j < columns; j++) {
            if (Math.abs(LU.get(j, j)) < EPS)
                return false;
        }
        return true;
    }

    /**
     * Return lower triangular matrix
     *
     * @return L
     */
    public Matrix getL() {
        Matrix L = new Matrix(rows, columns);
        double[][] matrixL = L.matrix();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (i > j) {
                    matrixL[i][j] = LU.get(i, j);
                } else if (i == j) {
                    matrixL[i][j] = 1.0;
                } else {
                    matrixL[i][j] = 0.0;
                }
            }
        }
        return L;
    }

    /**
     * Return upper triangular factor
     *
     * @return U
     */
    public Matrix getU() {
        Matrix U = new Matrix(columns, columns);
        double[][] matrixU = U.matrix();
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < columns; j++) {
                if (i <= j) {
                    matrixU[i][j] = LU.get(i, j);
                } else {
                    matrixU[i][j] = 0.0;
                }
            }
        }
        return U;
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
            d *= LU.get(j, j);
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
                    X[i][j] -= X[k][j] * LU.get(i, k);
                }
            }
        }
        // Solve U*X = Y;
        for (int k = columns - 1; k >= 0; k--) {
            for (int j = 0; j < nx; j++) {
                X[k][j] /= LU.get(k, k);
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < nx; j++) {
                    X[i][j] -= X[k][j] * LU.get(i, k);
                }
            }
        }
        return Xmat;
    }
}