package com.example.cs_c_matrix_calc.apapter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cs_c_matrix_calc.R;

/**
 * Created by cs_c on 5/15/14.
 */
public class MatrixGridAdapter extends BaseAdapter implements View.OnClickListener, EditText.OnEditorActionListener {
    private int mMatrixSize;
    private Context mContext;
    public int[][] mNumberArray;


    public MatrixGridAdapter(Context _context, int _size) {
        mContext = _context;
        mMatrixSize = _size;
        mNumberArray = new int[_size][_size];

    }


    @Override
    public int getCount() {
        return mMatrixSize * mMatrixSize;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_grid, null);
            holder.etComment = (EditText) convertView.findViewById(R.id.etItem_item_grid);
            holder.etComment.setOnEditorActionListener(this);
            convertView.setOnClickListener(this);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.etComment.setTag(position);
        holder.etComment.setHint((position/mMatrixSize)+1 + "x" + ((position%mMatrixSize)+1));
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == 6) {

        }
        return true;
    }

    public class ViewHolder {
        EditText etComment;
        int position;
    }

}