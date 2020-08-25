package com.example.tonyccpicker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tonyccpicker.databinding.LayoutPickerBinding


class PhonePickDialog : DialogFragment() {
    private lateinit var binding : LayoutPickerBinding
    private lateinit var countries : List<CountryInfo>
    private var adapter : CountryInfoAdapter = CountryInfoAdapter()
    private var isOpen = false

    companion object{
        private var instance : PhonePickDialog? = null

        fun newInstance() : PhonePickDialog{
            if(instance == null) {
                instance = PhonePickDialog()
            }
            return instance!!
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("WONSIK", "onCreateView")
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_picker,
            container,
            false
        )

        isOpen = false
        initContents()
        setUpListeners()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        dialog!!.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }

    private fun initContents() {
        countries = CountryInfo.getAllCountries(context!!, "korean")
        adapter.submitList(countries)
        binding.rc.layoutManager = LinearLayoutManager(context!!)
        binding.rc.adapter = adapter
    }

    private fun setUpListeners(){
        binding.apply {
            layout.setOnClickListener {
                if(!isOpen){
                    openEditText()
                }
            }

            btnCancel.setOnClickListener {
                if(isOpen){
                    closeEditText()
                }
                else{
                    openEditText()
                }
            }

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val str = s.toString()
                    if(str.isNotEmpty()){
                        tvSearch.visibility = View.INVISIBLE
                        val list = CountryInfo.getFilteredCountries(context!!, "korean", str)
                        adapter.submitList(list)
                    }
                    else{
                        tvSearch.visibility = View.VISIBLE
                        adapter.submitList(countries)
                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }


    private fun openEditText(){
        binding.apply {
            layout.post {
                tvSearch.post{
                    val dx = layout.x - tvSearch.x
                    tvSearch.animate().translationX(dx).setDuration(100).withEndAction {
                        etSearch.visibility = View.VISIBLE
                    }.start()
                    btnCancel.animate().alpha(1.0f).setDuration(100).withStartAction {
                        btnCancel.visibility = View.VISIBLE
                        isOpen = true
                    }.start()
                }
            }
        }
    }

    private fun closeEditText(){
        binding.apply {
            tvSearch.animate().translationX(0f).setDuration(100).withStartAction {
                tvSearch.visibility = View.VISIBLE
                etSearch.setText("")
            }.withEndAction {
                etSearch.visibility = View.GONE
            }.start()
            btnCancel.animate().alpha(0.0f).setDuration(100).withStartAction {
                btnCancel.visibility = View.GONE
                isOpen = false
            }.start()
        }
    }

    override fun dismiss() {
        super.dismiss()
        isOpen = false
        adapter?.submitList(countries)
        binding.etSearch.setText("")
    }

    fun setOnItemClickListener(listener : (CountryInfo) -> Unit){
        adapter.setOnItemClickListener(listener)
    }

}
