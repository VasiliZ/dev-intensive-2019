package ru.skillbranch.mycircle

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import kotlin.random.Random

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrSet: AttributeSet,
    defStyle: Int = 0
) : ImageView(context, attrSet, defStyle) {
    private var color: Int = 0
    private lateinit var initialsText: String
    override fun onDraw(canvas: Canvas?) {
        if (drawable == null) {
            drawTextInCircle(canvas, color)
        } else {
            circledDrawable(canvas)
        }
    }

    init {
        color = getRandomColor()
        while (color == Color.WHITE) {
            color = getRandomColor()
        }
    }

    private fun drawTextInCircle(canvas: Canvas?, colorCircle: Int) {
        val height = height
        val width = width
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = colorCircle
        canvas?.drawCircle(this.height / 2f, this.width / 2f, height / 2.toFloat(), paint)
        background = CircleDrawable()
        paintText.color = Color.WHITE
        paintText.textSize = 92f
        paintText.isFakeBoldText = true
        val rectText = Rect()
        paintText.getTextBounds(initialsText, 0, initialsText.length, rectText)
        val xForText = (width - rectText.width()).toFloat() / 2f
        val yForText = (height + rectText.height()) / 2f
        canvas?.drawText(initialsText, xForText, yForText, paintText)
    }

    private fun circledDrawable(canvas: Canvas?) {
        val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap
        val bitmapScaled: Bitmap = scaledImage(bitmap)
        val cropedMap = getCircleBitmap(bitmapScaled)

        canvas?.drawBitmap(cropedMap, 0f, 0f, null)
    }

    private fun scaledImage(bitmap: Bitmap): Bitmap {
        val maxSize = Math.min(width, height)
        val resultWidth: Int
        val resultHeigth: Int

        if (width > height) {
            resultWidth = maxSize
            resultHeigth = (height * maxSize) / width
        } else {
            resultHeigth = maxSize
            resultWidth = (width * maxSize) / height
        }
        return Bitmap.createScaledBitmap(bitmap, resultWidth, resultHeigth, false)

    }

    private fun getCircleBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color

        canvas.drawCircle(
            bitmap.width / 2f, bitmap.height / 2f,
            bitmap.width / 2f, paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    private fun getRandomColor(): Int {
        val random = Random
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

    fun setText(initials: String) {
        initialsText = initials
    }
}

class CircleDrawable : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var radius: Float = 0f

    override fun draw(canvas: Canvas) {
        val rect: Rect = bounds
        paint.color = Color.RED
        canvas.drawCircle(rect.centerX().toFloat(), rect.centerY().toFloat(), radius, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        radius = Math.min(bounds!!.width(), bounds.height()) / 2f
    }

}