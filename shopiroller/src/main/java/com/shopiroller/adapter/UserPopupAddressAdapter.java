package com.shopiroller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.shopiroller.R;
import com.shopiroller.models.user.BaseAddressModel;
import com.shopiroller.models.user.UserBillingAddressModel;
import com.shopiroller.models.user.UserShippingAddressModel;
import com.shopiroller.viewholders.PopupAddressViewHolder;

import java.util.List;

public class UserPopupAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity context;
    List<Object> dataList;
    private int selectedPosition = -1;

    public UserPopupAddressAdapter(AppCompatActivity context, List<Object> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_address_popup_item, parent, false);
        return new PopupAddressViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PopupAddressViewHolder viewHolder = (PopupAddressViewHolder) holder;
        if (dataList.get(position) instanceof UserShippingAddressModel)
            viewHolder.bindShipping((UserShippingAddressModel) dataList.get(position));
        else
            viewHolder.bindBilling((UserBillingAddressModel) dataList.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public Object getSelectedAddress(int position) {
        if (position != -1)
            return dataList.get(position);
        return null;
    }

    public void setSelectedPosition(int position) {
        if (selectedPosition == position)
            return;
        if (selectedPosition >= 0)
            ((BaseAddressModel) dataList.get(selectedPosition)).isSelected = false;
        ((BaseAddressModel) dataList.get(position)).isSelected = true;
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public void setSelectedPosition(BaseAddressModel baseAddressModel) {
        for (int i = 0; i < dataList.size(); i++) {
            if ((dataList.get(i) instanceof UserBillingAddressModel && ((UserBillingAddressModel) dataList.get(i)).id.equals(baseAddressModel.id))) {
                ((UserBillingAddressModel) dataList.get(i)).isSelected = true;
                if (selectedPosition != -1)
                    ((UserBillingAddressModel) dataList.get(selectedPosition)).isSelected = false;
                selectedPosition = i;
                notifyItemChanged(selectedPosition);
                notifyItemChanged(i);
            } else if (((dataList.get(i) instanceof UserShippingAddressModel && ((UserShippingAddressModel) dataList.get(i)).id.equals
                    (baseAddressModel.id)))) {
                ((UserShippingAddressModel) dataList.get(i)).isSelected = true;
                if (selectedPosition != -1)
                    ((UserShippingAddressModel) dataList.get(selectedPosition)).isSelected = true;
                notifyItemChanged(selectedPosition);
                notifyItemChanged(i);
                selectedPosition = i;
            }
        }
    }
}
