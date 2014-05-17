package com.example.cs_c_matrix_calc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.cs_c_matrix_calc.matrix.utils.MatrixOperation;

public class MyActivity extends Activity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        findViewById(R.id.bntAdd_main).setOnClickListener(this);
        findViewById(R.id.bntMinus_main).setOnClickListener(this);
        findViewById(R.id.bntDivided_main).setOnClickListener(this);
        findViewById(R.id.bntMultiplied_main).setOnClickListener(this);
        findViewById(R.id.bntInverse_main).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bntAdd_main:
                startActivity(new Intent(getApplicationContext(),
                        MatrixActivity.class).putExtra("operation", MatrixOperation.SUM));
                break;
            case R.id.bntMinus_main:
                startActivity(new Intent(getApplicationContext(),
                        MatrixActivity.class).putExtra("operation", MatrixOperation.MINUS));
                break;
            case R.id.bntDivided_main:
                startActivity(new Intent(getApplicationContext(),
                        MatrixActivity.class).putExtra("operation", MatrixOperation.DIVIDED));
                break;
            case R.id.bntMultiplied_main:
                startActivity(new Intent(getApplicationContext(),
                        MatrixActivity.class).putExtra("operation", MatrixOperation.MULTIPLIED));
                break;
            case R.id.bntInverse_main:
                startActivity(new Intent(getApplicationContext(),
                        MatrixActivity.class).putExtra("operation", MatrixOperation.INVERSE));
                break;
            default:
                startActivity(new Intent(getApplicationContext(),
                        MatrixActivity.class).putExtra("operation", MatrixOperation.NONE));
        }
    }
}
