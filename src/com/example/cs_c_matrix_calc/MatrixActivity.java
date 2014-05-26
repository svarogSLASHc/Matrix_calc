package com.example.cs_c_matrix_calc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cs_c_matrix_calc.matrix.Matrix;
import com.example.cs_c_matrix_calc.matrix.utils.MatrixOperation;

import java.util.ArrayList;

/**
 * Created by cs_c on 5/15/14.
 */
public class MatrixActivity extends Activity implements View.OnClickListener {
    private Spinner mMatrixSize;
    private double[][] mFirstNumberArray;
    private double[][] mSecondNumberArray;
    private int size = 2;
    private MatrixOperation mOperation = MatrixOperation.NONE;
    double[][] result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_activity);
        mOperation = (MatrixOperation) getIntent().getExtras().get("operation");
        if (mOperation.equals(MatrixOperation.INVERSE)){
            findViewById(R.id.rg_matrix_switcher).setVisibility(View.INVISIBLE);
        }
        initView();
        setData();
    }

    private void initView() {
        findViewById(R.id.btn_clear_matrix_activity).setOnClickListener(this);
        findViewById(R.id.btn_calc_matrix_activity).setOnClickListener(this);
        findViewById(R.id.rb_check_first_matrix).setOnClickListener(this);
        findViewById(R.id.rb_check_second_matrix).setOnClickListener(this);
        mMatrixSize = (Spinner) findViewById(R.id.sp_matrix_size_matrix_activity);
    }

    private void setData() {
        ArrayAdapter<String> adapterSp = new ArrayAdapter<String>(MatrixActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sp_size));
        mMatrixSize.setAdapter(adapterSp);
        mMatrixSize.setPrompt("Matrix size");
        mMatrixSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                size = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                setMatrixSize();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setMatrixSize() {

        mFirstNumberArray  = new double[size][size];
        mSecondNumberArray = new double[size][size];
        LinearLayout llMatrixTableFirst = (LinearLayout) findViewById(R.id.ll_first_matrix_table_matrix_activity);
        createMatrixTable(llMatrixTableFirst, mFirstNumberArray);
        LinearLayout llMatrixTableSecond = (LinearLayout) findViewById(R.id.ll_second_matrix_table_matrix_activity);
        createMatrixTable(llMatrixTableSecond, mSecondNumberArray);
    }

    private void createMatrixTable(LinearLayout llContainer, final double[][] numberArray){
        float dens = getResources().getDisplayMetrics().density;
        llContainer.removeAllViews();
        for (int row = 0; row < size; row++){
            LinearLayout llRow = new LinearLayout(this);
            llRow.setOrientation(LinearLayout.HORIZONTAL);
            llRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            for (int column = 0; column < size; column++){
                final EditText etItem = new EditText(this);
                etItem.setHint((column + 1) + "x" +(row + 1));
                etItem.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                final int finalRow = row;
                final int finalColumn = column;
                etItem.setTag(new ArrayList<Integer>(){{
                    add(finalRow);
                    add(finalColumn);
                }});
                etItem.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                    }

                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {

                        try {
                            if (etItem.getText().toString().trim().equals("")) {
                                numberArray[finalRow][finalColumn] = 0;
                                return;
                            }
                            numberArray[finalRow][finalColumn] = etItem.getText().toString().trim().equals("-")
                                    ? -1 : Integer.parseInt(etItem.getText().toString());
                        }
                        catch (Exception e){
                            Toast.makeText(MatrixActivity.this, "Wrong number", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                etItem.setLayoutParams(new LinearLayout.LayoutParams((int) (70 * dens), (int) (40 * dens)));
                llRow.addView(etItem);
            }
            llContainer.addView(llRow);
        }
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
            case R.id.rb_check_first_matrix:
                findViewById(R.id.ll_first_matrix_table_matrix_activity).setVisibility(View.VISIBLE);
                findViewById(R.id.ll_second_matrix_table_matrix_activity).setVisibility(View.GONE);
                break;
            case R.id.rb_check_second_matrix:
                findViewById(R.id.ll_first_matrix_table_matrix_activity).setVisibility(View.GONE);
                findViewById(R.id.ll_second_matrix_table_matrix_activity).setVisibility(View.VISIBLE);
                break;
        }


    }

    private void doCalc() {
        Matrix A = new Matrix(mFirstNumberArray);
        Matrix B = new Matrix(mSecondNumberArray);

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
        ShowResult showResult = new ShowResult(MatrixActivity.this);
        showResult.show();
    }

    private class ShowResult extends Dialog {

        public ShowResult(Context context) {
            super(context, android.R.style.Theme_Translucent_NoTitleBar);
            setTitle("Result");
            setContentView(R.layout.dialog_result);
            LinearLayout llResult = (LinearLayout) findViewById(R.id.ll_matrix_result_dialog);
            createMatrixTable(llResult, result);
        }

        @Override
        public void show() {
            super.show();

        }

        private void createMatrixTable(LinearLayout llContainer, final double[][] numberArray){
            float dens = getResources().getDisplayMetrics().density;
            llContainer.removeAllViews();
            for (int row = 0; row < size; row++){
                LinearLayout llRow = new LinearLayout(getApplicationContext());
                llRow.setOrientation(LinearLayout.HORIZONTAL);
                llRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                for (int column = 0; column < size; column++){
                    final TextView etItem = new TextView(getApplicationContext());
                    etItem.setText(String.valueOf(numberArray[row][column]));
                 //   etItem.setGravity(Gravity.CENTER);
                    etItem.setLayoutParams(new LinearLayout.LayoutParams((int) (60 * dens), (int) (20 * dens)));
                    llRow.addView(etItem);
                }
                llContainer.addView(llRow);
            }
        }
    }
}
