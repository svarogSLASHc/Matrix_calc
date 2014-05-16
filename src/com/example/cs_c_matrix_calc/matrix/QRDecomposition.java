package com.example.cs_c_matrix_calc.matrix;

import java.io.Serializable;

import com.example.cs_c_matrix_calc.matrix.utils.Maths;

/**
 * QR Decomposition.
 * <p/>
 * For an rows-by-columns matrix A with rows >= columns, the QR decomposition is an rows-by-columns
 * orthogonal matrix Q and an columns-by-columns upper triangular matrix R so that
 * A = Q*R.
 * <p/>
 * The QR decompostion always exists, even if the matrix does not have
 * full rank, so the constructor will never fail.  The primary use of the
 * QR decomposition is in the least squares solution of nonsquare systems
 * of simultaneous linear equations.  This will fail if isFullRank()
 * returns false.
 */
public class QRDecomposition implements Serializable {

    private double[][] QR;
    private int rows, columns;

    /**
     * Array for internal storage of diagonal of R.
     *
     * @serial diagonal of R.
     */
    private double[] Rdiag;

/* ------------------------
   Constructor
 * ------------------------ */

    /**
     * QR Decomposition, computed by Householder reflections.
     * Structure to access R and the Householder vectors and compute Q.
     *
     * @param A Rectangular matrix
     */

    public QRDecomposition(Matrix A) {
        // Initialize.
        QR = A.matrixCopy();
        rows = A.rows();
        columns = A.columns();
        Rdiag = new double[columns];

        // Main loop.
        for (int k = 0; k < columns; k++) {
            // Compute 2-norm of k-th column without under/overflow.
            double nrm = 0;
            for (int i = k; i < rows; i++) {
                nrm = Maths.hypot(nrm, QR[i][k]);
            }

            if (nrm != 0.0) {
                // Form k-th Householder vector.
                if (QR[k][k] < 0) {
                    nrm = -nrm;
                }
                for (int i = k; i < rows; i++) {
                    QR[i][k] /= nrm;
                }
                QR[k][k] += 1.0;

                // Apply transformation to remaining columns.
                for (int j = k + 1; j < columns; j++) {
                    double s = 0.0;
                    for (int i = k; i < rows; i++) {
                        s += QR[i][k] * QR[i][j];
                    }
                    s = -s / QR[k][k];
                    for (int i = k; i < rows; i++) {
                        QR[i][j] += s * QR[i][k];
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
        double[][] X = B.matrixCopy();

        // Compute Y = transpose(Q)*B
        for (int k = 0; k < columns; k++) {
            for (int j = 0; j < nx; j++) {
                double s = 0.0;
                for (int i = k; i < rows; i++) {
                    s += QR[i][k] * X[i][j];
                }
                s = -s / QR[k][k];
                for (int i = k; i < rows; i++) {
                    X[i][j] += s * QR[i][k];
                }
            }
        }
        // Solve R*X = Y;
        for (int k = columns - 1; k >= 0; k--) {
            for (int j = 0; j < nx; j++) {
                X[k][j] /= Rdiag[k];
            }
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < nx; j++) {
                    X[i][j] -= X[k][j] * QR[i][k];
                }
            }
        }
        return (new Matrix(X, columns, nx).subMatrix(0, columns - 1, 0, nx - 1));
    }
}
