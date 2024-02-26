package com.supersuman.macronium.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.supersuman.macronium.R
import com.supersuman.macronium.other.Keys
import com.supersuman.macronium.services.socket


class KeyboardFragment : Fragment() {

    private lateinit var keyboardInput: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keyboardInput = view.findViewById(R.id.keyboardInput)
        keyboardInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) {
                when(keyCode) {
                    KeyEvent.KEYCODE_DEL -> socket?.emit("key-press", Keys.Backspace)
                }
            }
            false
        }
        keyboardInput.doOnTextChanged { text, start, before, count ->
            if (count>0) {
                val addedText = text?.substring(start, start+count)
                socket?.emit("keyboard", addedText)
            } else if (before==1){
                socket?.emit("key-press", Keys.Backspace)
            }
        }
        view.findViewById<Button>(R.id.clearButton).setOnClickListener {
            keyboardInput.text.clear()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_keyboard, container, false)
    }

    override fun onResume() {
        super.onResume()
        keyboardInput.requestFocus()
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(keyboardInput, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onPause() {
        super.onPause()
        keyboardInput.clearFocus()
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(keyboardInput.windowToken, 0)
    }
}