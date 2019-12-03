package ru.skillbranch.devintensive.models

import android.graphics.*
import android.graphics.drawable.Drawable
import ru.skillbranch.devintensive.utils.Utils

data class Profile(


    val firstName: String,
    val lastName: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0
) {

    private val nickName: String = nickName()
    val rank: String = "Junior android developer"
    fun map(): Map<String, Any> = mapOf(
        "nickName" to nickName,
        "rank" to rank,
        "firstName" to firstName,
        "lastName" to lastName,
        "about" to about,
        "repository" to repository,
        "rating" to rating,
        "respect" to respect
    )

    private fun nickName(): String {
        val nickName = "$firstName $lastName"
        return Utils.transliteration(nickName, " ")
    }

    fun prepareInitials(): String? {
        return Utils.toInitials(firstName, lastName)
    }


}

class TextDrawable(initials: String?, color: Int) : Drawable() {
    private val paint: Paint = Paint()
    private val text: String? = initials

    init {
        paint.color = color
        paint.textSize = 22F
        paint.isAntiAlias = true
        paint.setShadowLayer(6f, 0f, 0f, Color.BLACK)
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER

    }

    override fun draw(canvas: Canvas) {
        canvas.drawText(text, 0.toFloat(), 0.toFloat(), paint)
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

}