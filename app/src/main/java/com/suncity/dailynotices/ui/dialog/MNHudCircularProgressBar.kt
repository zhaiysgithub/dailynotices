package com.suncity.dailynotices.ui.dialog

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.suncity.dailynotices.R

/**
 * @ProjectName: dailynotices
 * @Package: com.suncity.dailynotices.ui.dialog
 * @ClassName: MNHudCircularProgressBar
 * @Description: MNHudCircularProgressBar
 * @UpdateDate: 24/7/2019
 * 引用：https://github.com/lopspower/CircularProgressBar
 */
class MNHudCircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Properties
    private var progress = 0f
    private var lastProgress = 0f
    private var strokeWidth = 10f
    private var backgroundStrokeWidth = 10f
    private var color = Color.BLACK
    private var backgroundColor = Color.GRAY
    //动画时长
    private val mDuration: Long = 300

    // Object used to draw
    private val startAngle = -90
    private var rectF: RectF? = null
    private var backgroundPaint: Paint? = null
    private var foregroundPaint: Paint? = null

    //Because it should recalculate its bounds
    var progressBarWidth: Float
        get() = strokeWidth
        set(strokeWidth) {
            this.strokeWidth = strokeWidth
            foregroundPaint?.strokeWidth = strokeWidth
            requestLayout()
            invalidate()
        }

    //Because it should recalculate its bounds
    var backgroundProgressBarWidth: Float
        get() = backgroundStrokeWidth
        set(backgroundStrokeWidth) {
            this.backgroundStrokeWidth = backgroundStrokeWidth
            backgroundPaint?.strokeWidth = backgroundStrokeWidth
            requestLayout()
            invalidate()
        }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        rectF = RectF()
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.MNHudCircularProgressBar, 0, 0)
        //Reading values from the XML layout
        try {
            // Value
            progress = typedArray.getFloat(R.styleable.MNHudCircularProgressBar_mn_progress, progress)
            // StrokeWidth
            strokeWidth =
                typedArray.getDimension(R.styleable.MNHudCircularProgressBar_mn_progressbar_width, strokeWidth)
            backgroundStrokeWidth = typedArray.getDimension(
                R.styleable.MNHudCircularProgressBar_mn_background_progressbar_width,
                backgroundStrokeWidth
            )
            // Color
            color = typedArray.getInt(R.styleable.MNHudCircularProgressBar_mn_progressbar_color, color)
            backgroundColor =
                typedArray.getInt(R.styleable.MNHudCircularProgressBar_mn_background_progressbar_color, backgroundColor)
        } finally {
            typedArray.recycle()
        }

        // Init Background
        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint?.color = backgroundColor
        backgroundPaint?.style = Paint.Style.STROKE
        backgroundPaint?.strokeWidth = backgroundStrokeWidth

        // Init Foreground
        foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        foregroundPaint?.color = color
        foregroundPaint?.style = Paint.Style.STROKE
        foregroundPaint?.strokeWidth = strokeWidth
    }
    //endregion

    //region Draw Method
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(rectF != null && backgroundPaint != null){
            canvas.drawOval(rectF!!, backgroundPaint!!)
            val angle = 360 * progress / 100
            canvas.drawArc(rectF!!, startAngle.toFloat(), angle, false, foregroundPaint!!)
        }
    }
    //endregion

    //region Mesure Method
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = Math.min(width, height)
        setMeasuredDimension(min, min)
        val highStroke = if (strokeWidth > backgroundStrokeWidth) strokeWidth else backgroundStrokeWidth
        rectF?.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2)
    }

    fun getProgress(): Float {
        return progress
    }

    @JvmOverloads
    fun setProgress(progress: Float, animal: Boolean = true) {
        this.progress = if (progress <= 100f) progress else 100f
        if (animal) {
            //开启动画
            startAnim()
            this.lastProgress = progress
        } else {
            invalidate()
        }

    }

    //动画
    fun startAnim() {
        val mAngleAnim = ValueAnimator.ofFloat(lastProgress, progress)
        mAngleAnim.interpolator = AccelerateDecelerateInterpolator()
        mAngleAnim.duration = mDuration
        mAngleAnim.addUpdateListener { valueAnimator ->
            progress = valueAnimator.animatedValue as Float
            postInvalidate()
        }
        mAngleAnim.start()
    }

    fun getColor(): Int {
        return color
    }

    fun setColor(color: Int) {
        this.color = color
        foregroundPaint?.color = color
        invalidate()
        requestLayout()
    }

    fun getBackgroundColor(): Int {
        return backgroundColor
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
        backgroundPaint?.color = backgroundColor
        invalidate()
        requestLayout()
    }

    /**
     * Set the progress with an animation.
     *
     * @param progress The progress it should animate to it.
     * @param duration The length of the animation, in milliseconds.
     */
    @JvmOverloads
    fun setProgressWithAnimation(progress: Float, duration: Int = 1500) {
        val objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress)
        objectAnimator.duration = duration.toLong()
        objectAnimator.interpolator = DecelerateInterpolator()
        objectAnimator.start()
    }
}
