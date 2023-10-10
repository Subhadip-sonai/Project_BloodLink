package com.sonai.bloodlink.utilityClasses

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText

class GoesToNextEditTextOTP {
    fun setEditTextListeners(et1: EditText, et2: EditText, et3: EditText, et4: EditText, et5: EditText, et6: EditText) {
        val editTexts = arrayOf(et1, et2, et3, et4, et5, et6)

        for (i in editTexts.indices) {
            val currentEditText = editTexts[i]
            val nextEditText = if (i < editTexts.size - 1) editTexts[i + 1] else null
            val previousEditText = if (i > 0) editTexts[i - 1] else null

            currentEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && nextEditText != null) {
                        nextEditText.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            currentEditText.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_DEL && currentEditText.text.isEmpty() && previousEditText != null) {
                    // Handle backspace to move to the previous EditText
                    currentEditText.clearFocus()
                    previousEditText.requestFocus()
                    true
                } else {
                    false
                }
            }
        }
    }

}