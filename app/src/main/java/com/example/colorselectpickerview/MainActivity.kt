package com.example.colorselectpickerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.select.color_picker.ColorSelectPickerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvTest: TextView = findViewById(R.id.tv_text)
        val view: View = findViewById(R.id.view)
        findViewById<ColorSelectPickerView>(R.id.color_picker).colorChanged = {
            tvTest.setTextColor(it)
            view.setBackgroundColor(it)
        }
    }
}