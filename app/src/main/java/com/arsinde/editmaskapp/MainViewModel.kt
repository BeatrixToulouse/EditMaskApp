package com.arsinde.editmaskapp

import android.text.Editable
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val phoneTemplate = "+7 "

    fun getMaskedPhoneNumber(str: Editable?, cache: String, cursor: Int): String {
        val raw = str?.let { it ->

            val tmp = if (cursor < 3) {
                cache
            } else it
            when {
                phoneTemplate == tmp.toString() -> null
                tmp.length == 1 -> tmp
                tmp.length in 4..7 -> {
                    tmp.subSequence(3, tmp.length).filter { ch ->
                        ch != ' '
                    }
                }
                tmp.length in 8..10 -> {
                    tmp.subSequence(3, tmp.length).filter { ch ->
                        ch != ' '
                    }
                }
                tmp.length in 11..15 -> {
                    tmp.subSequence(3, tmp.length).filter { ch ->
                        ch != ' '
                    }
                }
                tmp.length > 15 -> {
                    tmp.subSequence(3, 16).filter { ch ->
                        ch != ' '
                    }
                }
                else -> null
            }
        }
        val phone = raw?.toMutableList()?.apply {
            when (size) {
                in 4..6 -> {
                    add(3, ' ')
                }
                in 7..8 -> {
                    add(3, ' ')
                    add(7, ' ')
                }
                in 9..10 -> {
                    add(3, ' ')
                    add(7, ' ')
                    add(10, ' ')
                }
            }
        }?.joinToString("")
        return phone?.let {
            "+7 $it"
        } ?: phoneTemplate
    }
}