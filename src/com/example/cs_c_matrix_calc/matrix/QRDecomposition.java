package com.example.cs_c_matrix_calc.matrix;

import com.example.cs_c_matrix_calc.matrix.utils.Maths;

import java.io.Serializable;

public class QRDecomposition implements Serializable {

    private Matrix QR;
    private int rows, columns;

    /**
     * Array for internal storage of diagonal of R.
     *
     * @serial diagonal of R.
     */
    private double[] Rdiag;

    /**
     * QR Decomposition, computed by Householder reflections.
     * Structure to access R and the Householder vectors and compute Q.
     *
     * @param A Rectangular matrix
     */
    public QRDecomposition(Matrix A) {
        // Initialize.
        QR = A.copy();
        rows = A.rows();
        columns = A.columns();
        Rdiag = new double[columns];

        // Main loop.
        for (int k = 0; k < columns; k++) {
            // Compute 2-norm of k-th column without under/overflow.
            double nrm = 0;
            for (int i = k; i < rows; i++) {
                nrm = Maths.hypot(nrm, QR.get(i, k));
            }

            if (nrm != 0.0) {
                // Form k-th Householder vector.
                if (QR.get(k, k) < 0.0) {
                    nrm = -nrm;
                }
                for (int i = k; i < rows; i++) {
                    QR.set(i, k, QR.get(i, k) / nrm);
                }
                QR.set(k, k, QR.get(k, k) + 1.0);

                // Apply transformation to remaining columns.
                for (int j = k + 1; j < columns; j++) {
                    double s = 0.0;
                    for (int i = k; i < rows; i++) {
                        s += QR.get(i, k) * QR.get(i, j);
                    }
                    s = -s / QR.get(k, k);
                    for (int i = k; i < rows; i++) {
                        QR.set(i, j, QR.get(i, j) + s * QR.get(i, k));
                    }
                }
            }
            Rdiag[k] = -nrm;
        }
    }

    /**
     * Is the matrix full rank?
     *
     * @return true if R, and hence A, has full rank.
     */
    public boolean isFullRank() {
        for (int j = 0; j < columns; j++) {
            if (Rdiag[j] == 0)
                return false;
        }
        return true;
    }

    /**
     * Least squares solution of A*X = B
     *
     * @param B A Matrix with as many rows as A and any number of columns.
     * @return X that minimizes the two norm of Q*R*X-B.
     * @throws IllegalArgumentException Matrix row dimensions must agree.
     * @throws RuntimeException         Matrix is rank deficient.
     */
    public Matrix solve(Matrix B) {
        if (B.rows() != rows) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        }
        if (!this.isFullRank()) {
            throw new RuntimeException("Matrix is rank deficient.");
        }

        // Copy right hand side
        int nx = B.columns();
        Matrix X = B.copy();

        // Compute Y = transpose(Q)*B
        for (int k = 0; k < columns; k++) {
            for (int j = 0; j < nx; j++) {
                double s = 0.0;
                for (int i = k; i < rows; i++) {
                    s += QR.get(i, k) * X.get(i, j);
                }
                s = -s / QR.get(k, k);
                for (int i = k; i < rows; i++) {
                    X.set(i, j, X.get(i, j) + s * QR.get(i, k));
                }
            }
        }
        // Solve R*X = Y;
        for (int k = columns - 1; k >= 0; k--) {
            for (int j = 0; j < nx; j++) {
                X.set(k, j, X.get(k, j) / Rdiag[k]);
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < nx; j++) {
                    X.set(i, j, X.get(i, j) - X.get(k, j) * QR.get(i, k));
                }
            }
        }
        return (new Matrix(X.matrix(), columns, nx).subMatrix(0, columns - 1, 0, nx - 1));
    }
}