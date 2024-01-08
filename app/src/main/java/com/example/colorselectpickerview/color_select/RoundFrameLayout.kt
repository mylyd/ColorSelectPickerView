package com.example.colorselectpickerview.color_select

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.colorselectpickerview.R

/**
 * @author lyd
 * @time 2024/1/8 18:34:34
 */
class RoundFrameLayout : FrameLayout {
    private var path: Path? = null
    private var rectF: RectF? = null
    private var canvasRadius: Float = 0f //半径

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (isInEditMode) return
        initView(context,attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout)
        canvasRadius = typeArray.getDimension(R.styleable.RoundFrameLayout_radius,0f)
            typeArray.recycle()
        path = Path()
        rectF = RectF()
        setWillNotDraw(false) //保证 onDraw 方法被调用
    }

    override fun onDraw(canvas: Canvas?) {
        path?.reset()
        rectF?.set(0f, 0f, width.toFloat(), height.toFloat())
        rectF?.let { path?.addRoundRect(it, canvasRadius, canvasRadius, Path.Direction.CW) }
        path?.let { canvas?.clipPath(it) }
        // 绘制子视图
        super.onDraw(canvas)
    }
}