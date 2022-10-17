package com.shopiroller.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.shopiroller.R;
import com.shopiroller.adapter.DialogFilterAdapter;

import java.util.ArrayList;
import java.util.Collection;

public class MaterialListFilterDialog {

    private MaterialDialog mMaterialDialog;
    private DialogFilterAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public MaterialListFilterDialog(Context context, String  titleResource, Collection collection , Integer position) {
        ArrayList<Object> mData = new ArrayList<>();
        mData.addAll(collection);
        mAdapter = new DialogFilterAdapter(mData,position);

        mMaterialDialog = new MaterialDialog.Builder(context)
                .title(titleResource)
                .customView(R.layout.layout_filter_list_dialog, false)
                .negativeText(R.string.cancel)
                .build();

        View view = mMaterialDialog.getCustomView();
        mRecyclerView = view.findViewById(R.id.list);
        EditText mInput = view.findViewById(R.id.input);
        mRecyclerView.setAdapter(mAdapter);

        mInput.addTextChangedListener(new FilterTextWatcher());

    }

    public MaterialListFilterDialog(Context context, int titleResource, Collection collection) {
        ArrayList<Object> mData = new ArrayList<>();
        mData.addAll(collection);
        mAdapter = new DialogFilterAdapter(mData);

        mMaterialDialog = new MaterialDialog.Builder(context)
                .title(titleResource)
                .customView(R.layout.layout_filter_list_dialog, false)
                .negativeText(R.string.cancel)
                .build();

        View view = mMaterialDialog.getCustomView();
        mRecyclerView = view.findViewById(R.id.list);
        EditText mInput = view.findViewById(R.id.input);
        mRecyclerView.setAdapter(mAdapter);

        mInput.addTextChangedListener(new FilterTextWatcher());

    }

    public RecyclerView show() {
        mMaterialDialog.show();
        return mRecyclerView;
    }

    public void dismiss() {
        if (mMaterialDialog != null && mMaterialDialog.isShowing())
            mMaterialDialog.dismiss();
    }

    private class FilterTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mAdapter != null)
                mAdapter.getFilter().filter(s);
        }
    }
}
