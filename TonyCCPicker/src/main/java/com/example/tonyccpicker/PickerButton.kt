package com.example.tonyccpicker

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

class PickerButton : FrameLayout {
    private lateinit var iv : ImageView
    private lateinit var tv : TextView
    private lateinit var currentCountry : CountryInfo

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

    override fun onFinishInflate() {
        super.onFinishInflate()
        inflateView()
        initContents()
    }

    private fun inflateView() {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view  = inflater.inflate(R.layout.layout_button, this, false)
        iv = view.findViewById(R.id.iv_flag)
        tv = view.findViewById(R.id.tv_code)
        view.setOnClickListener {
            val dialog = PhonePickDialog.newInstance()
            dialog.setOnItemClickListener {countryInfo ->
                countryInfo?.let{
                    applyCountryInfo(it)
                    dialog.dismiss()
                }
            }
            val activity = context as FragmentActivity
            dialog.show(activity.supportFragmentManager, "TonyPicker")
        }
        addView(view)
    }

    private fun initContents() {
        currentCountry = CountryInfo.getDefaultCountry(context!!, "korean")!!
        currentCountry?.let {
            applyCountryInfo(it)
        }
    }

    private fun applyCountryInfo(countryInfo: CountryInfo){
        iv.setImageResource(CountryInfo.getFlagMasterResID(countryInfo))
        tv.text = "+" + countryInfo.phoneCode
        currentCountry = countryInfo
    }

    fun getSelectedCountry() : CountryInfo{
        return currentCountry
    }
}