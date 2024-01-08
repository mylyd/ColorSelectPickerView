package com.example.colorselectpickerview.color_select

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.colorselectpickerview.R


/**
 * 颜色选择预览
 */
class ColorSelectPickerView : LinearLayout {

    private lateinit var llColorProgress: View
    private lateinit var vColorBar: View
    private lateinit var rlTransBar: View
    private lateinit var vTransBar: View
    private lateinit var vLocation: View
    private lateinit var vBgColor: View
    private lateinit var vTransPreview: ImageView

    private lateinit var colorBarLayoutParams: FrameLayout.LayoutParams
    private lateinit var transBarLayoutParams: FrameLayout.LayoutParams
    private lateinit var vLocationLayoutParams: FrameLayout.LayoutParams

    private fun isInitialized(): Boolean {
        return this::llColorProgress.isInitialized &&
                this::vColorBar.isInitialized &&
                this::rlTransBar.isInitialized &&
                this::vTransBar.isInitialized &&
                this::vLocation.isInitialized &&
                this::vBgColor.isInitialized &&
                this::vTransPreview.isInitialized &&
                this::colorBarLayoutParams.isInitialized &&
                this::transBarLayoutParams.isInitialized &&
                this::vLocationLayoutParams.isInitialized
    }

    private var red = 255
    private var green: Int = 0
    private var blue: Int = 0
    private var index = 0
    private var cpvColorPreview: ColorSelectPreviewView? = null
    private var transValue = 255 //透明度

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (isInEditMode) return
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.view_color_picker, this)
        vBgColor = view.findViewById(R.id.fl_color)
        vLocation = view.findViewById(R.id.view_location)
        vLocationLayoutParams = vLocation.layoutParams as FrameLayout.LayoutParams

        llColorProgress = findViewById(R.id.ll_color_progress)
        cpvColorPreview = view.findViewById(R.id.cpv_color_preview)
        vColorBar = view.findViewById(R.id.view_color_bar)
        colorBarLayoutParams = vColorBar.layoutParams as FrameLayout.LayoutParams

        rlTransBar = view.findViewById(R.id.rl_trans_bar)
        vTransBar = view.findViewById(R.id.view_trans_bar)
        transBarLayoutParams = vTransBar.layoutParams as FrameLayout.LayoutParams
        vTransPreview = view.findViewById(R.id.view_trans_preview)

        /*调整颜色*/
        llColorProgress.setOnTouchListener { _, event ->
            val width = llColorProgress.width
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {}
                MotionEvent.ACTION_MOVE -> {}
                MotionEvent.ACTION_UP -> {}
            }
            val leftMargin = event.x
            var x = 0f
            if (leftMargin < vColorBar.width / 2.0f) {
                colorBarLayoutParams.leftMargin = 0
            } else if (leftMargin > width - vColorBar.width / 2.0f) {
                x = 100f
                colorBarLayoutParams.leftMargin = width - vColorBar.width
            } else {
                x = event.x / width * 100
                colorBarLayoutParams.leftMargin = (leftMargin - vColorBar.width / 2).toInt()
            }
            vColorBar.layoutParams = colorBarLayoutParams
            onProgressChanged(x.toInt())
            true
        }

        /*调整透明度*/

        rlTransBar.setOnTouchListener { _, event ->
            val width = rlTransBar.width
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {}
                MotionEvent.ACTION_MOVE -> {}
                MotionEvent.ACTION_UP -> {}
            }
            val leftMargin = event.x
            var x = 0f
            if (leftMargin < vTransBar.width / 2.0f) {
                transBarLayoutParams.leftMargin = 0
            } else if (leftMargin > width - vTransBar.width / 2.0f) {
                x = 100f
                transBarLayoutParams.leftMargin = width - vTransBar.width
            } else {
                x = event.x / width * 100
                transBarLayoutParams.leftMargin = (leftMargin - vTransBar.width / 2).toInt()
            }
            vTransBar.layoutParams = transBarLayoutParams
            changeTransparency(x.toInt())
            true
        }


        /*调整颜色明暗*/
        vBgColor.setOnTouchListener { _, event ->
            val width = vBgColor.width
            val height = vBgColor.height
            val leftMargin: Int
            val topMargin: Int
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {}
                MotionEvent.ACTION_MOVE -> {
                    //防止越界处理
                    leftMargin = if (event.x > width - vLocation.width / 2f) {
                        width - vLocation.width
                    } else if (event.x < vLocation.width / 2f) {
                        0
                    } else {
                        (event.x - vLocation.width / 2f).toInt()
                    }
                    topMargin = if (event.y > height - vLocation.height / 2f) {
                        height - vLocation.height
                    } else if (event.y <= vLocation.height / 2f) {
                        0
                    } else {
                        (event.y - vLocation.height / 2f).toInt()
                    }
                    vLocationLayoutParams.leftMargin = leftMargin
                    vLocationLayoutParams.topMargin = topMargin
                    vLocation.layoutParams = vLocationLayoutParams
                    changeColor()
                }
                MotionEvent.ACTION_UP -> {}
            }
            true
        }
    }

    /**
     * 颜色值调整
     */
    private fun onProgressChanged(progressColor: Int) {
        red = 0
        green = 0
        blue = 0
        index = (progressColor / (100 / 6f)).toInt()
        val v = progressColor % (100 / 6f) / (100 / 6f)
        when (index) {
            0 -> {
                red = 255
                green = (255 * v).toInt()
            }

            1 -> {
                red = (255 * (1 - v)).toInt()
                green = 255
            }

            2 -> {
                green = 255
                blue = (255 * v).toInt()
            }

            3 -> {
                green = (255 * (1 - v)).toInt()
                blue = 255
            }

            4 -> {
                blue = 255
                red = (255 * v).toInt()
            }

            5 -> {
                blue = (255 * (1 - v)).toInt()
                red = 255
            }

            else -> red = 255
        }
        vBgColor.setBackgroundColor(Color.rgb(red, green, blue))
        changeColor()
    }

    /**
     * 颜色明暗度调整
     */
    private fun changeColor() {
        var tempRed = red
        var tempGreen = green
        var tempBlue = blue
        val hPercent = 1 - vLocation.x / (vBgColor.width - vLocation.width)
        val vPercent = vLocation.y / (vBgColor.height - vLocation.height)
        when (index) {
            0 -> {
                tempGreen = (green + hPercent * (255 - green)).toInt()
                tempBlue = (blue + hPercent * (255 - blue)).toInt()
            }

            1 -> {
                tempRed = (red + hPercent * (255 - red)).toInt()
                tempBlue = (blue + hPercent * (255 - blue)).toInt()
            }

            2 -> {
                tempRed = (red + hPercent * (255 - red)).toInt()
                tempBlue = (blue + hPercent * (255 - blue)).toInt()
            }

            3 -> {
                tempRed = (red + hPercent * (255 - red)).toInt()
                tempGreen = (green + hPercent * (255 - green)).toInt()
            }

            4 -> {
                tempRed = (red + hPercent * (255 - red)).toInt()
                tempGreen = (green + hPercent * (255 - green)).toInt()
            }

            5, 6 -> {
                tempGreen = (green + hPercent * (255 - green)).toInt()
                tempBlue = (blue + hPercent * (255 - blue)).toInt()
            }
        }
        tempRed = (tempRed - tempRed * vPercent).toInt()
        tempGreen = (tempGreen - tempGreen * vPercent).toInt()
        tempBlue = (tempBlue - tempBlue * vPercent).toInt()
        val color = Color.argb(transValue, tempRed, tempGreen, tempBlue)
        cpvColorPreview?.setColor(color)
        colorChanged?.invoke(color)

        val gradientColor = intArrayOf(Color.argb(0, 0, 0, 0), Color.rgb(tempRed, tempGreen, tempBlue))
        val drawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColor)
        vTransPreview.background = drawable
    }

    /**
     * 透明度
     */
    private fun changeTransparency(progress: Int) {
        transValue = (progress / 100f * 255).toInt()
        val color = Color.argb(transValue, red, green, blue)
        cpvColorPreview?.setColor(color)
        colorChanged?.invoke(color)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (!isInitialized()) return
        val layoutParams = vLocation.layoutParams as FrameLayout.LayoutParams
        layoutParams.leftMargin = vBgColor.width - vLocation.width
        vLocation.layoutParams = layoutParams

        colorBarLayoutParams.leftMargin = llColorProgress.width - vColorBar.width
        vColorBar.layoutParams = colorBarLayoutParams

        transBarLayoutParams.leftMargin = rlTransBar.width - vTransBar.width
        vTransBar.layoutParams = transBarLayoutParams

        val color = intArrayOf(Color.argb(0, 0, 0, 0), Color.rgb(255, 0, 0))
        val drawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color)
        vTransPreview.background = drawable
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }

    var colorChanged: ((Int) -> Unit)? = null
}