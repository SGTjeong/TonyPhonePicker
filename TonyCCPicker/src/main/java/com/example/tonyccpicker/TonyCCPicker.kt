package com.example.tonyccpicker

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class TonyCCPicker : FrameLayout {
    private lateinit var layout : ConstraintLayout
    private lateinit var tvSearch : TextView
    private lateinit var etSearch : EditText
    private lateinit var btnCancel : Button
    private lateinit var countries : List<CountryInfo>
    private lateinit var adapter : CountryInfoAdapter

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    init {

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        inflateView()
        setUpListener()
        initContents()
    }

    private fun inflateView() {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view  = inflater.inflate(R.layout.layout_picker, this, false)
        layout = view.findViewById(R.id.layout)
        tvSearch = layout.findViewById<TextView>(R.id.tv_search)
        etSearch = layout.findViewById<EditText>(R.id.et_search)
        btnCancel = layout.findViewById<Button>(R.id.btn_cancel)
        addView(view)
    }

    private fun initContents() {
        adapter = CountryInfoAdapter()
        countries = CountryInfo.getAllCountries(context!!, "korean")
        adapter.submitList(countries)
        val rec = layout.findViewById<RecyclerView>(R.id.rc)
        rec.layoutManager = LinearLayoutManager(context!!)
        rec.adapter = adapter
    }

    private var isOpen = false

    private fun setUpListener(){
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

        etSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val str = s.toString()
                if(str.isNotEmpty()){
                    tvSearch.visibility = View.INVISIBLE
                    val list = CountryInfo.getFilteredCountries(context, "korean", str)
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


    private fun openEditText(){
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

    private fun closeEditText(){
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