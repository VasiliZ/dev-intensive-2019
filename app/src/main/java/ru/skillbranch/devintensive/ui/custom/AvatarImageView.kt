package ru.skillbranch.devintensive.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.core.animation.doOnRepeat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.dpToPx
import kotlin.math.max
import kotlin.math.truncate

class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet,
    defStyle: Int = 0
) : ImageView(context, attrSet, defStyle) {
    @Px
    var borderWidth: Float = context.dpToPx(DEFAULT_BORDER_WIDTH)
    @ColorInt
    private var borderColor = Color.WHITE
    private var initials: String = "??"

    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintBorder = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()
    private var isAvatarMode: Boolean = true

    private var size = 0

    private val bgColors = arrayOf(
        Color.parseColor("#7BC862"),
        Color.parseColor("#E17076"),
        Color.parseColor("#FAA774"),
        Color.parseColor("#6EC9CE"),
        Color.parseColor("#EE7AAE"),
        Color.parseColor("#2169F3")
    )


    init {
        if (attrSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attrSet, R.styleable.AvatarImageView)
            borderWidth = typedArray.getDimension(
                R.styleable.AvatarImageView_ai_border_width,
                context.dpToPx(DEFAULT_BORDER_WIDTH)
            )
            borderColor = typedArray.getColor(
                R.styleable.AvatarImageView_ai_border_color,
                DEFAULT_BORDER_COLOR

            )
            initials = typedArray.getString(R.styleable.AvatarImageView_ai_initials) ?: "??"
            typedArray.recycle()
        }

        scaleType = ScaleType.CENTER_CROP
        setup()
        setOnLongClickListener { handleLongClick() }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val saveState = SavedState(super.onSaveInstanceState())
        saveState.isAvatarMode = isAvatarMode
        saveState.borderWidth = borderWidth
        saveState.borderColor = borderColor
        return saveState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)

        if (state is SavedState) {
            super.onRestoreInstanceState(state)
            isAvatarMode = state.isAvatarMode
            borderWidth = state.borderWidth
            borderColor = state.borderColor

            with(borderPaint) {
                color = borderColor
                strokeWidth = borderWidth
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val initSize = resolveDefaultSize(widthMeasureSpec)
        setMeasuredDimension(max(initSize, size), max(initSize, size))

        Log.e("M_AvatarImageView", "ofter set dimension $measuredWidth $measuredHeight")
    }

    override fun onDraw(canvas: Canvas) {
        //   super.onDraw(canvas)
        Log.d("M_AvatarImageView", "onDraw")

        if (drawable != null && isAvatarMode) {
            drawAvatar(canvas)
        } else {
            drawInitials(canvas)
        }

        val half = (borderWidth / 2).toInt()
        viewRect.inset(half, half)
        canvas.drawOval(viewRect.toRectF(), paintBorder)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("M_AvatarImageView", "onSizeChanged")
        if (w == 0) return
        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }

        prepareShaders(w, h)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (isAvatarMode) prepareShaders(width, height)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (isAvatarMode) prepareShaders(width, height)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (isAvatarMode) prepareShaders(width, height)
    }

    private fun resolveDefaultSize(spec: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> {
                context.dpToPx(DEFAULT_SIZE).toInt()

            }
            MeasureSpec.AT_MOST
            -> MeasureSpec.getSize(spec)
            MeasureSpec.EXACTLY
            -> MeasureSpec.getSize(spec)

            else -> MeasureSpec.getSize(spec)

        }
    }

    companion object {

        const val DEFAULT_SIZE = 40
        private const val DEFAULT_BORDER_WIDTH = 2
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
    }

    private fun setup() {
        with(paintBorder) {
            style = Paint.Style.STROKE
            color = borderColor
            strokeWidth = borderWidth

        }
    }

    private fun prepareShaders(w: Int, h: Int) {
        if (w == 0 || drawable == null) return
        val srcBitmap = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        avatarPaint.shader = BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun drawAvatar(canvas: Canvas) {
        canvas.drawOval(viewRect.toRectF(), avatarPaint)
    }

    private fun drawInitials(canvas: Canvas) {
        initialsPaint.color = initialsToColor(initials)
        canvas.drawOval(viewRect.toRectF(), initialsPaint)
        with(initialsPaint) {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33f
        }

        val offsetY = (initialsPaint.descent() + initialsPaint.ascent()) / 2
        canvas.drawText(
            initials,
            viewRect.exactCenterX(),
            viewRect.exactCenterY() - offsetY,
            initialsPaint
        )
    }

    private fun handleLongClick(): Boolean {

        val va = ValueAnimator.ofInt(width, width * 2).apply {
            duration = 500
            interpolator = LinearInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 1
        }

        va.addUpdateListener {
            size = it.animatedValue as Int
            requestLayout()
        }
        va.doOnRepeat {
            toogleMod()
        }

        va.start()
        return true
    }

    private fun toogleMod() {
        isAvatarMode = !isAvatarMode
        invalidate()
    }

    private fun initialsToColor(letters: String): Int {
        val b = letters[0].toByte()
        val len = bgColors.size
        val d = b / len.toDouble()
        val index = ((d - truncate(d)) * len).toInt()
        return bgColors[index]
    }

    fun setInitials(initials: String) {
        this.initials = initials
        if (!isAvatarMode) invalidate()
    }

    fun setBorderColor(@ColorInt color: Int) {
        borderColor = color
        borderPaint.color = borderColor
        invalidate()
    }

    fun setBorderWidth(@Dimension with: Int) {
        borderWidth = context.dpToPx(with)
        borderPaint.strokeWidth = borderWidth
        invalidate()
    }
}

private class SavedState : View.BaseSavedState, Parcelable {

    var isAvatarMode: Boolean = true
    var borderWidth: Float = 0f
    var borderColor: Int = 0

    constructor(parcel: Parcel) : super(parcel) {
        isAvatarMode = parcel.readInt() == 1
        borderWidth = parcel.readFloat()
        borderColor = parcel.readInt()
    }

    constructor(superState: Parcelable?) : super(superState)

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(if (isAvatarMode) 1 else 0)
        out.writeFloat(borderWidth)
        out.writeInt(borderColor)
    }

    override fun describeContents(): Int = 0

    companion object Creator : Parcelable.Creator<SavedState> {
        override fun createFromParcel(source: Parcel) = SavedState(source)

        override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
    }

}