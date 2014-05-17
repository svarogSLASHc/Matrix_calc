package com.example.cs_c_matrix_calc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.cs_c_matrix_calc.apapter.MatrixGridAdapter;
import com.example.cs_c_matrix_calc.apapter.ResultMatrixAdapter;
import com.example.cs_c_matrix_calc.matrix.Matrix;
import com.example.cs_c_matrix_calc.matrix.utils.MatrixOperation;

/**
 * Created by cs_c on 5/15/14.
 */
public class MatrixActivity extends Activity implements View.OnClickListener {
    private Spinner mMatrixSize;
    private GridView mGvMatrixFirst;
    private GridView mGvMatrixSecond;
    private MatrixGridAdapter mFirstAdapter;
    private MatrixGridAdapter mSecondAdapter;
    private int size = 2;
    private MatrixOperation mOperation = MatrixOperation.NONE;
    double[][] result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_activity);
        mOperation = (MatrixOperation) getIntent().getExtras().get("operation");
        initView();
        setData();
    }

    private void initView() {
        findViewById(R.id.btn_clear_matrix_activity).setOnClickListener(this);
        findViewById(R.id.btn_calc_matrix_activity).setOnClickListener(this);
        mMatrixSize = (Spinner) findViewById(R.id.sp_matrix_size_matrix_activity);
        mGvMatrixFirst = (GridView) findViewById(R.id.gv_matrix_first_matrix_activity);
        mGvMatrixSecond = (GridView) findViewById(R.id.gv_matrix_second_matrix_activity);
    }

    private void setData() {
        ArrayAdapter<String> adapterSp = new ArrayAdapter<String>(MatrixActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sp_size));
        mMatrixSize.setAdapter(adapterSp);
        mMatrixSize.setPrompt("Matrix size");
        mMatrixSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MatrixActivity.this,
                        "Selected  " + adapterView.getItemAtPosition(i),
                        Toast.LENGTH_SHORT).show();
                size = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                setMatrixSize();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // use default size
//                setMatrixSize();
            }
        });
    }

    private void setMatrixSize() {
        mGvMatrixFirst.setNumColumns(size);
        mGvMatrixFirst.setColumnWidth(50);
        mFirstAdapter = new MatrixGridAdapter(MatrixActivity.this, size);
        mGvMatrixFirst.setAdapter(mFirstAdapter);
        mGvMatrixSecond.setNumColumns(size);
        mGvMatrixSecond.setColumnWidth(50);
        mSecondAdapter = new MatrixGridAdapter(MatrixActivity.this, size);
        mGvMatrixSecond.setAdapter(mSecondAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear_matrix_activity:
                setMatrixSize();
                break;
            case R.id.btn_calc_matrix_activity:
                Toast.makeText(this, "Calc", Toast.LENGTH_SHORT).show();
                doCalc();
                break;
        }
        ShowResult showResult = new ShowResult(MatrixActivity.this);
        showResult.show();

    }

    private void doCalc() {
        double[][] matrixA = mFirstAdapter.mNumberArray;
        double[][] matrixB = mSecondAdapter.mNumberArray;
        Matrix A = new Matrix(matrixA);
        Matrix B = new Matrix(matrixB);

        switch (mOperation) {
            case SUM:
                result = A.plus(B).matrix();
                break;
            case MINUS:
                result = A.minus(B).matrix();
                break;
            case MULTIPLIED:
                result = A.mult(B).matrix();
                break;
            case DIVIDED:
                result = A.mult(B.inverse()).matrix();
                break;
            case INVERSE:
                result = A.inverse().matrix();
                break;
            default:
                Toast.makeText(MatrixActivity.this, "Wrong operation passed!", Toast.LENGTH_SHORT).show();
        }
    }

    private class ShowResult extends Dialog {
        GridView resultGrid;

        public ShowResult(Context context) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
            setTitle("Result");
            setContentView(R.layout.dialog_result);
            resultGrid = (GridView) findViewById(R.id.gv_matrix_result_dialog);
            resultGrid.setNumColumns(size);
        }

        @Override
        public void show() {
            super.show();
            resultGrid.setAdapter(new ResultMatrixAdapter(MatrixActivity.this, size, result));
        }
    }
}
