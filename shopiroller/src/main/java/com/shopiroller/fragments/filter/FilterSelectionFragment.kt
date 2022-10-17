package com.shopiroller.fragments.filter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import com.shopiroller.R
import com.shopiroller.activities.FilterActivity
import com.shopiroller.adapter.FilterSelectionAdapter
import com.shopiroller.models.CategoriesItem
import kotlinx.android.synthetic.main.fragment_filter_selection.*

private const val SELECTION_DATA = "selectionData"

class FilterSelectionFragment : Fragment(),
    FilterSelectionAdapter.FilterSelectionItemSelectedListener {

    interface FilterSelectionCallBack {
        fun onSelectionConfirmed(selectedItem: CategoriesItem?)
    }

    companion object {
        @JvmStatic
        fun newInstance(data: ArrayList<CategoriesItem?>?, listener: FilterSelectionCallBack?) =
            FilterSelectionFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(SELECTION_DATA, data)
                }
                this.listener = listener
            }
    }

    private var dataList: ArrayList<CategoriesItem?>? = null
    private var adapter: FilterSelectionAdapter? = null
    private var listener: FilterSelectionCallBack? = null

    private var selectedData: CategoriesItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            (it.getSerializable(SELECTION_DATA) as ArrayList<CategoriesItem?>?).also {
                dataList = it
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as FilterActivity).setClearButtonVisibility(View.GONE)
    }

    override fun onDetach() {
        super.onDetach()
        (requireActivity() as FilterActivity).setClearButtonVisibility(View.VISIBLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter_selection, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FilterSelectionAdapter(dataList, this)
        selection_recycler_view.adapter = adapter

        apply_button.setOnClickListener {
            listener?.onSelectionConfirmed(selectedData)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onItemSelected(id: String) {
        dataList?.forEach { data ->

            val isSelected = data?.categoryId == id
            data?.isSelected = isSelected

            if (isSelected)
                selectedData = data

            if (data?.subCategories?.isNotEmpty() == true) {
                data.subCategories.forEach { subCategory ->

                    val isSelected = subCategory?.categoryId == id
                    subCategory?.isSelected = isSelected

                    if (isSelected)
                        selectedData = subCategory
                }
            }
        }
        adapter?.notifyDataSetChanged()
    }
}