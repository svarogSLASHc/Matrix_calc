package com.example.cs_c_matrix_calc.matrix.utils;

/**
 * Created by cs_c on 5/15/14.
 */
public enum MatrixOperation {
    SUM("A + B"),
    MINUS("A - B"),
    MULTIPLIED("A * B"),
    DIVIDED("A * inverse(B)"),
    INVERSE("inverse(A)"),
    NONE("Do nothing");

    private String operation;

    MatrixOperation(String operation) {
        this.operation = operation;
    }

    String operation() {
        return operation;
    }


}
