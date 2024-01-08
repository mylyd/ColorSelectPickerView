package com.example.colorselectpickerview.color_select

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.colorselectpickerview.R


/**
 * 颜色选择预览
 */
class ColorSelectPreviewView : View {

    private var bitmap: Bitmap? = null
    private var porterDuffXfermode: PorterDuffXfermode? = null
    private var paint: Paint? = null
    private var circlePaint: Paint? = null
    private var rectF: RectF? = null
    private var width = 0
    private var height = 0
    private var dstBitmap: Bitmap? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (isInEditMode) return
        initView()
    }

    private fun initView() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        bitmap = BitmapFactory.decodeResource(resources, R.drawable.color_select_bg_trans_01)
        paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

        porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        circlePaint?.color = Color.argb(255, 255, 0, 0)
        circlePaint?.style = Paint.Style.FILL
        circlePaint?.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = MeasureSpec.getSize(widthMeasureSpec)
        height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        dstBitmap?.let { canvas?.drawBitmap(it, 0f, 0f, paint) }
        //circlePaint?.let { canvas?.drawCircle(width / 2f, height / 2f, width / 2f, it) }
        previewView(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        drawBottom()
    }

    fun setColor(color: Int) {
        circlePaint?.color = color
        invalidate()
    }

    private fun drawBottom() {
        dstBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(dstBitmap!!)
        val layerAlpha = canvas.saveLayerAlpha(rectF, 255)
        bitmap?.let { canvas.drawBitmap(it, 0f, 0f, paint) }
        val srcBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvasSrc = Canvas(srcBitmap)
        previewView(canvasSrc)
        circlePaint?.xfermode = porterDuffXfermode
        canvas.drawBitmap(srcBitmap, 0f, 0f, circlePaint)
        canvas.restoreToCount(layerAlpha)
        circlePaint?.xfermode = null
    }

    private fun previewView(canvas: Canvas?) {
        //圆形
        //circlePaint?.let { canvas?.drawCircle(width / 2f, height / 2f, width / 2f, it) }
        //圆角矩形
        circlePaint?.let { canvas?.drawRoundRect(rectF!!, height / 8f, height / 8f, it) }
        //circlePaint?.let { canvas?.drawRect(rectF!!, it) }
    }

}