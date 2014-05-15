package com.example.cs_c_matrix_calc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cs_c_matrix_calc.apapter.MatrixGridAdapter;

/**
 * Created by cs_c on 5/15/14.
 */
public class MatrixActivity extends Activity implements View.OnClickListener {
    private Spinner mMatrixSize;
    private GridView mGvMatrixFirst;
    private GridView mGvMatrixSecond;
    int size = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_activity);
        initView();
        setData();
    }

    private void initView(){
        findViewById(R.id.btn_clear_matrix_activity).setOnClickListener(this);
        mMatrixSize = (Spinner) findViewById(R.id.sp_matrix_size_matrix_activity);
        mGvMatrixFirst = (GridView) findViewById(R.id.gv_matrix_first);
        mGvMatrixSecond = (GridView) findViewById(R.id.gv_matrix_second);
    }

    private void setData(){
        ArrayAdapter<String> adapterSp =new ArrayAdapter<String>(this,
                                        android.R.layout.simple_spinner_item,
                                        getResources().getStringArray(R.array.sp_size));
        mMatrixSize.setAdapter(adapterSp);
        mMatrixSize.setPrompt("Matrix size");
        mMatrixSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),
                        "Selected  " + adapterView.getItemAtPosition(i),
                        Toast.LENGTH_SHORT).show();
                size = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                setMatrixSize();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setMatrixSize(){
        mGvMatrixFirst.setNumColumns(size);
        mGvMatrixFirst.setColumnWidth(50);
        mGvMatrixFirst.setAdapter( new MatrixGridAdapter(this, size));
        mGvMatrixSecond.setNumColumns(size);
        mGvMatrixSecond.setColumnWidth(50);
        mGvMatrixSecond.setAdapter( new MatrixGridAdapter(this, size));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_clear_matrix_activity:
                setMatrixSize();
                break;
        }

    }
}
