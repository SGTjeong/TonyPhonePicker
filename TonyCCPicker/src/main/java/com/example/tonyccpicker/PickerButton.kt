package com.example.tonyccpicker

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.layout_picker.view.*
import java.lang.Exception

class PickerButton : FrameLayout {
    private lateinit var iv : ImageView
    private lateinit var tv : TextView
    private lateinit var currentCountry : CountryInfo
    private var attribute : PhonePickAttribute? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        loadAttributes(context, attrs)
    }

    private fun loadAttributes(context : Context, attrs: AttributeSet?) {
        attrs?:return
        val attrArr = context.obtainStyledAttributes(attrs, R.styleable.PickerButton, 0, 0)
        try{
            val title = attrArr.getString(R.styleable.PickerButton_dialogTitle)
            val searchButtonTitle = attrArr.getString(R.styleable.PickerButton_dialogSearchButtonTitle)
            val colorStateList = attrArr.getColorStateList(R.styleable.PickerButton_dialogBackgroundColor) as ColorStateList
            attribute = PhonePickAttribute(title, colorStateList, searchButtonTitle)
        } catch (e : Exception){
            Log.e("TonyPhonePicker", e.toString())
        } finally {
            attrArr.recycle()
        }
    }

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
            val dialog = PhonePickDialog.newInstance(attribute)
            dialog.setOnItemClickListener {countryInfo ->
                countryInfo?.let{
                    applyCountryInfo(it)
                    listener?.onSelect(it)
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

    private var listener : OnSelectCountryListener? = null

    fun setOnSelectCountryListener(listener : OnSelectCountryListener){
        this.listener = listener
        currentCountry?.let {
            listener?.onSelect(it)
        }
    }

    fun setOnSelectCountryListener(callback : (CountryInfo) -> Unit){
        this.listener = object : OnSelectCountryListener{
            override fun onSelect(country: CountryInfo) {
                callback(country)
            }
        }
        currentCountry?.let {
            listener?.onSelect(it)
        }
    }

    interface OnSelectCountryListener{
        fun onSelect(country : CountryInfo)
    }
}