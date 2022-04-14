package com.eyepax.newsapp.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.eyepax.newsapp.R


class TextViewWithFont : androidx.appcompat.widget.AppCompatTextView {
    var customFont: String? = null

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        setCustomFont(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setCustomFont(context, attrs)
    }

    private fun setCustomFont(context: Context, attrs: AttributeSet?) {
        setTypeface(Typeface.createFromAsset(context.assets, "fonts/Montserrat-Light.ttf"));
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView)
        val customFontTextView = typedArray.getInteger(R.styleable.CustomFontTextView_fontName, 0)
        customFont = when (customFontTextView) {
            1 -> resources.getString(R.string.Montserrat_Light)
            2 -> resources.getString(R.string.Montserrat_Medium)
            3 -> resources.getString(R.string.Montserrat_Regular)
            4 -> resources.getString(R.string.Montserrat_SemiBold)
            else -> resources.getString(R.string.Montserrat_Regular)
        }
        val typeface = Typeface.createFromAsset(
            context.getAssets(),
            "fonts/$customFont.ttf"
        )
        setTypeface(typeface)
        typedArray.recycle()
    }

}