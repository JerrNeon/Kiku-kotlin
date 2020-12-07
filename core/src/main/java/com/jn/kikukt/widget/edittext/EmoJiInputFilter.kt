package com.jn.kikukt.widget.edittext

import android.content.Context
import android.text.InputFilter
import android.text.Spanned
import android.widget.Toast
import java.util.regex.Pattern

/**
 * Author：Stevie.Chen Time：2019/7/12
 * Class Comment：EditText过滤EmoJi表情
 */
class EmoJiInputFilter(context: Context) : InputFilter {
    private var mContext: Context = context

    private val mEmojiPattern = Pattern.compile(
        "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
        Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
    )

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        source?.let {
            val emoJiMatcher = mEmojiPattern.matcher(it)
            if (emoJiMatcher.find()) {
                Toast.makeText(mContext.applicationContext, "不支持输入表情", Toast.LENGTH_SHORT).show()
                return ""
            }
        }
        return ""
    }
}