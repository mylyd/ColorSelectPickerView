package com.example.colorselectpickerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.colorselectpickerview.color_select.ColorSelectPickerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvTest: TextView = findViewById(R.id.tv_text)
        findViewById<ColorSelectPickerView>(R.id.color_picker).colorChanged = {
            tvTest.setTextColor(it)
        }
    }
}