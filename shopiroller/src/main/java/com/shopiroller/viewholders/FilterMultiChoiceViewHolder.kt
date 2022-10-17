package com.shopiroller.viewholders

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.shopiroller.Shopiroller
import com.shopiroller.adapter.FilterMultiChoiceAdapter
import kotlinx.android.synthetic.main.e_commerce_item_filter_multi_choice.view.*

class FilterMultiChoiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var listener: FilterMultiChoiceAdapter.FilterMultiChoiceItemSelectedListener? = null

    fun bind(
        item: com.shopiroller.models.MultiChoiceItem?,
        listener: FilterMultiChoiceAdapter.FilterMultiChoiceItemSelectedListener
    ) {

        this.listener = listener
        itemView.name_text_view.text = item?.name

        itemView.check_box.buttonTintList = ColorStateList.valueOf(Shopiroller.getTheme().primaryColor)

        setCheckbox(item?.isChecked ?: false)
    }

    private fun setCheckbox(isChecked: Boolean) {
        if (isChecked)
            itemView.name_text_view.typeface = Typeface.DEFAULT_BOLD

        itemView.check_box.setOnCheckedChangeListener(null)
        itemView.check_box.isChecked = isChecked
        itemView.check_box.setOnCheckedChangeListener { _, isChecked ->
            listener?.onItemSelected(adapterPosition, isChecked)
        }
    }

}