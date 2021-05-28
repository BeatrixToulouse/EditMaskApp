package com.arsinde.editmaskapp

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.arsinde.editmaskapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val phoneTemplate = "+7 "
    private val viewModel: MainViewModel by viewModels()

    private val textChangeListener = object : TextWatcher {
        private var cache = ""
        private var cursor = 0
        private var newText = SpannableStringBuilder("")
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            cursor = binding.etField.selectionStart
            if (cursor in 1..phoneTemplate.length) {
                cache = s?.toString() ?: ""
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            cursor = binding.etField.selectionStart
        }

        override fun afterTextChanged(s: Editable?) {

            val textWatcher = this
            with(binding.etField) {
                removeTextChangedListener(textWatcher)
                val phoneWithPrefix = viewModel.getMaskedPhoneNumber(s, cache, cursor)
                s?.clear()
                if (phoneWithPrefix.isNotEmpty()) {
                    s?.append(phoneWithPrefix)
                    post {
                        setSelection(phoneWithPrefix.length)
                    }
                }
                addTextChangedListener(textWatcher)
            }
        }
    }

    private val focusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            with(binding.etField) {
                if (text.isNullOrBlank()) {
                    setText("+7 ")
                }
                text?.let { post { setSelection(it.length) } }
            }
        }
    }
    private val focusChangeListener2 = View.OnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            with(binding.etField) {
                removeTextChangedListener(textChangeListener)
                if (text.toString() == phoneTemplate) {
                    text = null
                }
                addTextChangedListener(textChangeListener)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        binding.etField.onFocusChangeListener = focusChangeListener
        binding.etField2.onFocusChangeListener = focusChangeListener2
        binding.etField.addTextChangedListener(textChangeListener)
    }
}