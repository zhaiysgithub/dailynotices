package com.suncity.dailynotices.ui.views

import android.animation.Animator
import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.database.DataSetObserver
import android.os.Build
import android.support.annotation.AnimatorRes
import android.support.annotation.DrawableRes
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import android.widget.LinearLayout
import com.suncity.dailynotices.R

/**
 * @ProjectName: dailynotices
 * @Package: com.suncity.dailynotices.ui.views
 * @ClassName: CircleIndicator
 * @Description: 作用描述
 * @UpdateDate: 14/7/2019
 */
class CircleIndicator : LinearLayout {
    private var mViewpager: ViewPager? = null
    private var mIndicatorMargin = -1
    private var mIndicatorWidth = -1
    private var mIndicatorHeight = -1

    private var mIndicatorBackgroundResId: Int = 0
    private var mIndicatorUnselectedBackgroundResId: Int = 0

    private var mAnimatorOut: Animator? = null
    private var mAnimatorIn: Animator? = null
    private var mImmediateAnimatorOut: Animator? = null
    private var mImmediateAnimatorIn: Animator? = null

    private var mLastPosition = -1

    private val mInternalPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(
            position: Int, positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            val adapter = mViewpager?.adapter
            val count = adapter?.count ?: 0
            if (adapter == null || count <= 0) {
                return
            }

            if (mAnimatorIn?.isRunning == true) {
                mAnimatorIn?.end()
                mAnimatorIn?.cancel()
            }

            if (mAnimatorOut?.isRunning == true) {
                mAnimatorOut?.end()
                mAnimatorOut?.cancel()
            }


            if (mLastPosition >= 0) {
                val currentIndicator: View = getChildAt(mLastPosition)
                currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId)
                mAnimatorIn?.setTarget(currentIndicator)
                mAnimatorIn?.start()
            }

            val selectedIndicator = getChildAt(position)
            if (selectedIndicator != null) {
                selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId)
                mAnimatorOut?.setTarget(selectedIndicator)
                mAnimatorOut?.start()
            }
            mLastPosition = position
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    // No change
    val dataSetObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            if (mViewpager == null) {
                return
            }
            val adapter = mViewpager?.adapter
            val newCount = adapter?.count ?: 0
            val currentCount = childCount
            mLastPosition = when {
                newCount == currentCount -> return
                mLastPosition < newCount -> mViewpager!!.currentItem
                else -> -1
            }

            createIndicators()
        }
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val config = handleTypedArray(context, attrs)
        initialize(config)
    }

    class IndicetorConfig {
        var width = -1
        var height = -1
        var margin = -1

        @SuppressLint("ResourceType")
        @AnimatorRes
        var animatorResId = R.anim.scale_with_alpha
        @AnimatorRes
        var animatorReverseResId = 0
        @DrawableRes
        var backgroundResId = R.drawable.white_radius
        @DrawableRes
        var unselectedBackgroundId: Int = 0
        var orientation = LinearLayout.HORIZONTAL
        var gravity = Gravity.CENTER

        fun width(width: Int): IndicetorConfig {
            this.width = width
            return this
        }

        fun height(height: Int): IndicetorConfig {
            this.height = height
            return this
        }

        fun margin(margin: Int): IndicetorConfig {
            this.margin = margin
            return this
        }

        fun animator(@AnimatorRes animatorResId: Int): IndicetorConfig {
            this.animatorResId = animatorResId
            return this
        }

        fun animatorReverse(@AnimatorRes animatorReverseResId: Int): IndicetorConfig {
            this.animatorReverseResId = animatorReverseResId
            return this
        }

        fun drawable(@DrawableRes backgroundResId: Int): IndicetorConfig {
            this.backgroundResId = backgroundResId
            return this
        }

        fun drawableUnselected(@DrawableRes unselectedBackgroundId: Int): IndicetorConfig {
            this.unselectedBackgroundId = unselectedBackgroundId
            return this
        }

        fun orientation(orientation: Int): IndicetorConfig {
            this.orientation = orientation
            return this
        }

        fun gravity(gravity: Int): IndicetorConfig {
            this.gravity = gravity
            return this
        }

        fun build(): IndicetorConfig {
            return this
        }
    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?): IndicetorConfig {
        val config = IndicetorConfig()
        if (attrs == null) {
            return config
        }
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleIndicator)
        val width = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_width, -1)
        config.width(width)
        val height = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_height, -1)
        config.height(height)
        val margin = typedArray.getDimensionPixelSize(R.styleable.CircleIndicator_ci_margin, -1)
        config.margin(margin)
        val animatorResId = typedArray.getResourceId(
            R.styleable.CircleIndicator_ci_animator,
            R.anim.scale_with_alpha
        )
        config.animator(animatorResId)

        val animatorReverseResId = typedArray.getResourceId(R.styleable.CircleIndicator_ci_animator_reverse, 0)
        config.animatorReverse(animatorReverseResId)
        val backgroundResId = typedArray.getResourceId(
            R.styleable.CircleIndicator_ci_drawable,
            R.drawable.white_radius
        )
        config.drawable(backgroundResId)
        val unselectedBackgroundId = typedArray.getResourceId(
            R.styleable.CircleIndicator_ci_drawable_unselected,
            config.backgroundResId
        )
        config.drawableUnselected(unselectedBackgroundId)

        config.orientation = typedArray.getInt(R.styleable.CircleIndicator_ci_orientation, -1)
        config.gravity = typedArray.getInt(R.styleable.CircleIndicator_ci_gravity, -1)
        typedArray.recycle()
        return config.build()
    }

    private fun initialize(indicetorConfig: IndicetorConfig) {
        val miniSize = (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            DEFAULT_INDICATOR_WIDTH.toFloat(), resources.displayMetrics
        ) + 0.5f).toInt()
        mIndicatorWidth = if (indicetorConfig.width < 0) miniSize else indicetorConfig.width
        mIndicatorHeight = if (indicetorConfig.height < 0) miniSize else indicetorConfig.height
        mIndicatorMargin = if (indicetorConfig.margin < 0) miniSize else indicetorConfig.margin

        mAnimatorOut = createAnimatorOut(indicetorConfig)
        mImmediateAnimatorOut = createAnimatorOut(indicetorConfig)
        mImmediateAnimatorOut?.duration = 0

        mAnimatorIn = createAnimatorIn(indicetorConfig)
        mImmediateAnimatorIn = createAnimatorIn(indicetorConfig)
        mImmediateAnimatorIn?.duration = 0

        mIndicatorBackgroundResId = if (indicetorConfig.backgroundResId == 0) R.drawable.white_radius else indicetorConfig.backgroundResId
        mIndicatorUnselectedBackgroundResId = if (indicetorConfig.unselectedBackgroundId == 0)
            indicetorConfig.backgroundResId
        else
            indicetorConfig.unselectedBackgroundId

        orientation =
            if (indicetorConfig.orientation == LinearLayout.VERTICAL) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL
        gravity = if (indicetorConfig.gravity >= 0) indicetorConfig.gravity else Gravity.CENTER
    }

    private fun createAnimatorOut(indicetorConfig: IndicetorConfig): Animator {
        return AnimatorInflater.loadAnimator(context, indicetorConfig.animatorResId)
    }

    private fun createAnimatorIn(indicetorConfig: IndicetorConfig): Animator {

        val animatorIn: Animator
        if (indicetorConfig.animatorReverseResId == 0) {
            animatorIn = AnimatorInflater.loadAnimator(context, indicetorConfig.animatorResId)
            animatorIn.interpolator = ReverseInterpolator()
        } else {
            animatorIn = AnimatorInflater.loadAnimator(context, indicetorConfig.animatorReverseResId)
        }
        return animatorIn
    }

    fun setViewPager(viewPager: ViewPager) {
        mViewpager = viewPager
        if (mViewpager != null && mViewpager?.adapter != null) {
            mLastPosition = -1
            createIndicators()
            mViewpager?.removeOnPageChangeListener(mInternalPageChangeListener)
            mViewpager?.addOnPageChangeListener(mInternalPageChangeListener)
            mInternalPageChangeListener.onPageSelected(mViewpager?.currentItem ?: 0)
        }
    }


    @Deprecated("User ViewPager addOnPageChangeListener")
    fun setOnPageChangeListener(
        onPageChangeListener: ViewPager.OnPageChangeListener
    ) {
        if (mViewpager == null) {
            throw NullPointerException("can not find Viewpager , setViewPager first")
        }
        mViewpager?.removeOnPageChangeListener(onPageChangeListener)
        mViewpager?.addOnPageChangeListener(onPageChangeListener)
    }

    private fun createIndicators() {
        removeAllViews()
        val adapter = mViewpager?.adapter
        val count: Int = adapter?.count ?: 0
        if (adapter == null || count <= 0) {
            return
        }
        val currentItem = mViewpager?.currentItem
        val orientation = orientation

        if(mImmediateAnimatorOut == null) return
        for (i in 0 until count) {
            if (currentItem == i) {
                addIndicator(orientation, mIndicatorBackgroundResId, mImmediateAnimatorOut!!)
            } else {
                addIndicator(
                    orientation, mIndicatorUnselectedBackgroundResId,
                    mImmediateAnimatorIn!!
                )
            }
        }
    }

    private fun addIndicator(
        orientation: Int, @DrawableRes backgroundDrawableId: Int,
        animator: Animator
    ) {

        if (animator.isRunning) {
            animator.end()
            animator.cancel()
        }

        val indicator = View(context)
        indicator.setBackgroundResource(backgroundDrawableId)
        addView(indicator, mIndicatorWidth, mIndicatorHeight)
        val lp = indicator.layoutParams as LinearLayout.LayoutParams

        if (orientation == LinearLayout.HORIZONTAL) {
            lp.leftMargin = mIndicatorMargin
            lp.rightMargin = mIndicatorMargin
        } else {
            lp.topMargin = mIndicatorMargin
            lp.bottomMargin = mIndicatorMargin
        }

        indicator.layoutParams = lp

        animator.setTarget(indicator)
        animator.start()
    }

    private inner class ReverseInterpolator : Interpolator {
        override fun getInterpolation(value: Float): Float {
            return Math.abs(1.0f - value)
        }
    }

    companion object {

        private const val DEFAULT_INDICATOR_WIDTH = 5
    }
}