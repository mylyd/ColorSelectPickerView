package com.select.color_picker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt

/**
 *
 * @author lyd
 * @time 2024/1/9 09:46:46
 */
class RainbowColorView : View {

    private var paint: Paint? = null
    private var srcPaint: Paint? = null
    private var rectF: RectF? = null

    private var canvasRadius: Float = 0f //半径
    private var canvasType: Int = 0 //模式 见attrs/RainbowColorView/graph_type说明
    private var isPreviewCircle: Boolean = false //是否预览圆形

    private var colors: IntArray? = null //渐变rgb容器
    private var positions: FloatArray? = null //渐变rgb范围容器

    private val alphaBitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.color_select_bg_trans_01)
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        //if (isInEditMode) return
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.RainbowColorView)
        canvasRadius = typeArray.getDimension(R.styleable.RainbowColorView_round, 0f)
        canvasType = typeArray.getInt(R.styleable.RainbowColorView_graph_type, 1)
        canvasType = typeArray.getInt(R.styleable.RainbowColorView_graph_type, 1)
        isPreviewCircle = typeArray.getBoolean(R.styleable.RainbowColorView_preview_circle, false)
        typeArray.recycle()
        paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        rectF = RectF()
        paint?.isAntiAlias = true
        paint?.style = Paint.Style.FILL

        when (canvasType) {
            1 -> {
                colors = intArrayOf(Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED)
                positions = floatArrayOf(0f, 1.0f / 6, 2.0f / 6, 3.0f / 6, 4.0f / 6, 5.0f / 6, 1f)
            }

            2 -> {
                colors = intArrayOf(Color.TRANSPARENT, Color.RED)
                positions = floatArrayOf(0f, 1f)
                srcPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
                srcPaint?.isAntiAlias = true
                srcPaint?.shader = BitmapShader(alphaBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            }

            3 -> {
                paint?.color = Color.RED
                srcPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
                srcPaint?.isAntiAlias = true
                srcPaint?.shader = BitmapShader(alphaBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            }

            4 -> {

            }
        }
        setWillNotDraw(false)
    }

    /**
     * 设置透明度
     *
     * @param rgbColor
     */
    fun setAlphaColor(@ColorInt rgbColor: Int) {
        if (canvasType != 2 && colors?.isNotEmpty() == true && colors?.size == 2) return
        colors!![1] = rgbColor
        paint?.shader = LinearGradient(0f, 0f, width.toFloat(), 0f, colors!!, positions, Shader.TileMode.CLAMP)
        invalidate()
    }

    /**
     * 设置预览颜色
     *
     * @param previewColor
     */
    fun setPreviewColor(@ColorInt previewColor: Int) {
        if (canvasType != 3) return
        paint?.color = previewColor
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (rectF == null || paint == null) return
        when (canvasType) {
            1, 2 -> {
                if (colors == null || positions == null) return
                val gradient = LinearGradient(0f, 0f, width.toFloat(), 0f, colors!!, positions, Shader.TileMode.CLAMP)
                paint?.shader = gradient
                if (canvasType == 2) {
                    srcPaint?.let { canvas.drawRoundRect(rectF!!, canvasRadius, canvasRadius, it) }
                }
                canvas.drawRoundRect(rectF!!, canvasRadius, canvasRadius, paint!!)
            }

            3 -> {
                paint?.shader = null

                if (isPreviewCircle) {
                    srcPaint?.let { canvas.drawCircle(height / 2f, height / 2f, height / 2f, it) }
                    canvas.drawCircle(height / 2f, height / 2f, height / 2f, paint!!)
                } else {
                    srcPaint?.let { canvas.drawRoundRect(rectF!!, canvasRadius, canvasRadius, it) }
                    canvas.drawRoundRect(rectF!!, canvasRadius, canvasRadius, paint!!)
                }
            }

            4 -> {

            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("RainbowColorBarView", "onSizeChanged ")
        if (canvasType == 3 && isPreviewCircle && oldw != oldh) {
            val lp = layoutParams
            lp.width = lp.height
            layoutParams = lp
            Log.d("RainbowColorBarView", "onSizeChanged 1")
        }
        rectF?.set(0f, 0f, width.toFloat(), height.toFloat())
    }

}