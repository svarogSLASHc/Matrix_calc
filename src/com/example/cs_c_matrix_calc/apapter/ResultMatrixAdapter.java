package com.example.cs_c_matrix_calc.apapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import com.example.cs_c_matrix_calc.R;

/**
 * Created by cs_c on 5/15/14.
 */
public class ResultMatrixAdapter extends BaseAdapter {
    private int mMatrixSize;
    private Context mContext;
    public double[][] mNumberArray;


    public ResultMatrixAdapter(Context _context, int _size, double[][] _numArray) {
        mContext = _context;
        mMatrixSize = _size;
        mNumberArray = _numArray;

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
            holder.etComment.setEnabled(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.etComment.setText(mNumberArray[position / mMatrixSize][position % mMatrixSize] + "");
        return convertView;
    }

    public class ViewHolder {
        EditText etComment;
    }

}