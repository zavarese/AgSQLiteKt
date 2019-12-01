package br.edu.ifsp.scl.agsqlitekt.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import br.edu.ifsp.scl.agsqlitekt.R

class MaskedEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private var mMaskedFormatter: MaskedFormatter? = null
    private var mMaskedWatcher: MaskedWatcher? = null

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    val maskString: String?
        get() = mMaskedFormatter!!.maskString

    val unMaskedText: String
        get() {
            val currentText = text!!.toString()
            val formattedString = mMaskedFormatter!!.formatString(currentText)
            return formattedString.unMaskedString
        }

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaskedEditText)

        if (typedArray.hasValue(R.styleable.MaskedEditText_mask)) {
            val maskStr = typedArray.getString(R.styleable.MaskedEditText_mask)

            if (maskStr != null && maskStr.isNotEmpty()) {
                setMask(maskStr)
            }
        }

        typedArray.recycle()
    }

    fun setMask(mMaskStr: String) {
        mMaskedFormatter = MaskedFormatter(mMaskStr)

        if (mMaskedWatcher != null) {
            removeTextChangedListener(mMaskedWatcher)
        }

        mMaskedWatcher = MaskedWatcher(mMaskedFormatter!!, this)
        addTextChangedListener(mMaskedWatcher)
    }

}