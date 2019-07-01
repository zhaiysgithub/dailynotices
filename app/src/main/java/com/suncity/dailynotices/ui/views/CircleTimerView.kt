package com.suncity.dailynotices.ui.views

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.animation.AnimatorSet
import com.suncity.dailynotices.R
import com.suncity.dailynotices.utils.UIUtils
import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Rect
import android.view.animation.LinearInterpolator
import android.graphics.RectF








/**
 * @ProjectName:    dailynotices
 * @Package:        com.suncity.dailynotices.ui.views
 * @ClassName:      CircleTimerView
 * @Description:    倒计时view
 */
class CircleTimerView : View {

    private var mContext: Context? = null

    //里面实心圆颜色
    private var mSolidCircleColor: Int = 0
    //里面圆的半径
    private var mSolidCircleRadius: Int = 0
    //外面圆弧的颜色
    private var mEmptyCircleColor: Int = 0
    //外面圆弧的半径(可以使用画笔的宽度来实现)
    private var mEmptyCircleRadius: Int = 0
    //文字大小
    private var mTextSize: Int = 0
    //文字颜色
    private var mTextColor: Int = 0
    //文字
    private var mText: String? = null
    //绘制的方向
    private var mDrawOrientation: Int = 0
    //圆的画笔
    private var mPaintCircle: Paint? = null
    //圆弧的画笔
    private var mPaintArc: Paint? = null
    //绘制文字的画笔
    private var mPaintText: Paint? = null
    //时长
    private var mTimeLength: Int = 0

    //默认值
    private var defaultSolidCircleColor: Int = 0
    private var defaultEmptyCircleColor: Int = 0
    private var defaultSolidCircleRadius: Int = 0
    private var defaultEmptyCircleRadius: Int = 0
    private var defaultTextColor: Int = 0
    private var defaultTextSize: Int = 0
    private var defaultTimeLength: Int = 0
    private var defaultDrawOritation: Int = 0

    //当前扇形的角度
    private var startProgress: Int = 0
    private var endProgress: Int = 0
    private var currProgress: Int = 0

    //动画集合
    private var set: AnimatorSet? = null

    //回调
    private var onCountDownFinish: OnCountDownFinish? = null


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStylrAttr: Int) : super(context, attrs, defStylrAttr){
        this.mContext = context

        //初始化默认值
        defaultSolidCircleColor = UIUtils.getColor(R.color.colorPrimary)
        defaultEmptyCircleColor = UIUtils.getColor(R.color.colorAccent)
        defaultTextColor = UIUtils.getColor(R.color.color_black)

        defaultSolidCircleRadius = resources.getDimension(R.dimen.dimen_20).toInt()
        defaultEmptyCircleRadius = resources.getDimension(R.dimen.dimen_25).toInt()
        defaultTextSize = resources.getDimension(R.dimen.dimen_16).toInt()

        defaultTimeLength = 3
        defaultDrawOritation = 1

        //获取自定义属性
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleTimerView)
        mSolidCircleColor = a.getColor(R.styleable.CircleTimerView_solid_circle_color, defaultSolidCircleColor)
        mSolidCircleRadius =
            a.getDimensionPixelOffset(R.styleable.CircleTimerView_solid_circle_radius, defaultSolidCircleRadius)

        mEmptyCircleColor = a.getColor(R.styleable.CircleTimerView_empty_circle_color, defaultEmptyCircleColor)
        mEmptyCircleRadius =
            a.getDimensionPixelOffset(R.styleable.CircleTimerView_empty_circle_radius, defaultEmptyCircleRadius)

        mTextColor = a.getColor(R.styleable.CircleTimerView_circle_text_color, defaultTextColor)
        mTextSize = a.getDimensionPixelOffset(R.styleable.CircleTimerView_circle_text_size, defaultTextSize)

        mDrawOrientation = a.getInt(R.styleable.CircleTimerView_circle_draw_orientation, defaultDrawOritation)
        mTimeLength = a.getInt(R.styleable.CircleTimerView_time_length, defaultTimeLength)

        a.recycle()

        init()
    }

    private fun init() {
        //初始化画笔
        mPaintCircle = Paint()
        mPaintCircle?.style = Paint.Style.FILL
        mPaintCircle?.isAntiAlias = true
        mPaintCircle?.color = mSolidCircleColor

        mPaintArc = Paint()
        mPaintArc?.style = Paint.Style.STROKE
        mPaintArc?.isAntiAlias = true
        mPaintArc?.color = mEmptyCircleColor
        mPaintArc?.strokeWidth = (mEmptyCircleRadius - mSolidCircleRadius).toFloat()

        mPaintText = Paint()
        mPaintText?.style = Paint.Style.STROKE
        mPaintText?.isAntiAlias = true
        mPaintText?.textSize = mTextSize.toFloat()
        mPaintText?.color = mTextColor

        mText = mTimeLength.toString()
        if (defaultDrawOritation == 1) {
            startProgress = 360
            endProgress = 0
        } else {
            startProgress = 0
            endProgress = 360
        }
        currProgress = startProgress
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //设置宽高
        setMeasuredDimension(mEmptyCircleRadius * 2, mEmptyCircleRadius * 2)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制背景圆
        canvas.drawCircle(mEmptyCircleRadius.toFloat(), mEmptyCircleRadius.toFloat()
            , mSolidCircleRadius.toFloat(), mPaintCircle)

        val ovalLeft = ((mEmptyCircleRadius - mSolidCircleRadius) / 2).toFloat()
        val ovalTop = ((mEmptyCircleRadius - mSolidCircleRadius) / 2).toFloat()
        val ovalRight = (mEmptyCircleRadius + (mEmptyCircleRadius - mSolidCircleRadius) / 2 + mSolidCircleRadius).toFloat()
        val ovalBottom = (mEmptyCircleRadius + (mEmptyCircleRadius - mSolidCircleRadius) / 2 + mSolidCircleRadius).toFloat()
        //绘制圆弧
        val oval = RectF(ovalLeft,ovalTop,ovalRight,ovalBottom) // 用于定义的圆弧的形状和大小的界限

        canvas.drawArc(oval, -90f, currProgress.toFloat(), false, mPaintArc!!) // 根据进度画圆弧

        val tvLength = mText?.length ?: return

        //绘制文字
        val mBound = Rect()
        mPaintText?.getTextBounds(mText, 0, tvLength, mBound)
        val tvX = (width / 2 - mBound.width() / 2).toFloat()
        val tvY = (height / 2 + mBound.height() / 2).toFloat()
        canvas.drawText(mText!!,tvX ,tvY, mPaintText!!)
    }

    fun setOnCountDownFinish(onCountDownFinish: OnCountDownFinish) {
        this.onCountDownFinish = onCountDownFinish
    }


    /**
     * 通过外部开关控制
     */
    fun start() {

        val animator1 = ValueAnimator.ofFloat(startProgress.toFloat(), endProgress.toFloat())
        animator1.interpolator = LinearInterpolator()
        animator1.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue
            if(value is Float){
                currProgress = value.toInt()
            }
            invalidate()
        }

        val animator2 = ValueAnimator.ofInt(mTimeLength, 0)
        animator2.interpolator = LinearInterpolator()
        animator2.addUpdateListener(ValueAnimator.AnimatorUpdateListener { valueAnimator ->
            mTimeLength = valueAnimator.animatedValue as Int
            if (mTimeLength == 0)
                return@AnimatorUpdateListener
            mText = mTimeLength.toString() + ""
        })

        set = AnimatorSet()
        set?.playTogether(animator1, animator2)
        set?.duration = (mTimeLength * 1000).toLong()

        set?.start()

        set?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {
                onCountDownFinish?.onFinish()
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })


    }

    fun cancelAnim() {
        set?.pause()
    }

    interface OnCountDownFinish {
        fun onFinish()
    }
}