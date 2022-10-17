package com.shopiroller.fragments.filter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import butterknife.ButterKnife
import com.shopiroller.R
import com.shopiroller.activities.FilterActivity
import com.shopiroller.adapter.FilterMultiChoiceAdapter
import com.shopiroller.models.MultiChoiceItem
import kotlinx.android.synthetic.main.fragment_filter_multi_choice.*

private const val MULTI_CHOICE_DATA = "multiChoiceData"

class FilterMultiChoiceFragment : Fragment(),
    FilterMultiChoiceAdapter.FilterMultiChoiceItemSelectedListener {

    interface FilterMultiChoiceCallBack {
        fun onMultiChoiceConfirmed(id: String?, selectedItems: ArrayList<MultiChoiceItem?>?)
    }

    companion object {
        @JvmStatic
        fun newInstance(
            id: String?,
            data: ArrayList<MultiChoiceItem?>?,
            listener: FilterMultiChoiceCallBack
        ) =
            FilterMultiChoiceFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(MULTI_CHOICE_DATA, data)
                }
                this.listener = listener
                this.id = id
            }
    }

    private var data: ArrayList<MultiChoiceItem?>? = null
    private var dataCopy: ArrayList<MultiChoiceItem?>? = null
    private var listener: FilterMultiChoiceCallBack? = null
    private var id: String? = null

    val observableDataList = MutableLiveData<ArrayList<MultiChoiceItem?>?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { it ->
            (it.getSerializable(MULTI_CHOICE_DATA) as ArrayList<MultiChoiceItem?>).also {
                this.data = it
            }
            (it.getSerializable(MULTI_CHOICE_DATA) as ArrayList<MultiChoiceItem?>).also {
                this.dataCopy = arrayListOf()
                this.dataCopy!!.addAll(it)
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
        val view = inflater.inflate(R.layout.fragment_filter_multi_choice, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        multi_choice_recycler_view.adapter = FilterMultiChoiceAdapter(data, this)
        search_edit_text.addTextChangedListener(EditTextChangedListener())

        apply_button.setOnClickListener {
            val selectedItems: ArrayList<MultiChoiceItem?> = arrayListOf()
            data?.forEach {
                if (it?.isChecked == true)
                    selectedItems.add(it)
            }
            listener?.onMultiChoiceConfirmed(id, selectedItems)
            requireActivity().supportFragmentManager.popBackStack()
        }

        observeData()

    }

    private fun observeData() {
        observableDataList.observe(viewLifecycleOwner, Observer {
            multi_choice_recycler_view.adapter = FilterMultiChoiceAdapter(data, this)
        })
    }

    override fun onItemSelected(position: Int, isChecked: Boolean) {
        data?.get(position)?.isChecked = isChecked
        dataCopy?.get(position)?.isChecked = isChecked
    }

    fun filter(s: String) {
        data?.clear()

        if (s.isEmpty()) {
            if (dataCopy != null)
                data?.addAll(dataCopy!!)
        } else {
            dataCopy?.let {
                for (i in it.indices) {
                    if (it[i]?.name!!.startsWith(s, true)) {
                        data?.add(it[i])
                    }
                }
            }
        }
        observableDataList.value = data
    }

    private inner class EditTextChangedListener() : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            filter(p0.toString())
        }
    }
}