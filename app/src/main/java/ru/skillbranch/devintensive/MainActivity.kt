package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardClosed
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var benderImage: ImageView
    lateinit var textMessage: EditText
    lateinit var sendMessage: ImageView
    lateinit var text: TextView
    lateinit var bender: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("M_MainActivity", "OnCrete")
        benderImage = im_bender
        textMessage = ed_message
        sendMessage = iv_send_message
        text = tv_text

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        val restoreText =
            savedInstanceState?.getString("TEXT_MESSAGE") ?: textMessage.text.toString()
        bender = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        val (r, g, b) = bender.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        textMessage.setText(restoreText)
        this.isKeyboardOpen()
        this.isKeyboardClosed()
        text.text = bender.askQuestion()
        iv_send_message.setOnClickListener(this)
        textMessage.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    applyInputData()
                    this.hideKeyboard()
                    true
                }
                else -> false
            }
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send_message) {
            applyInputData()
        }
    }

    private fun applyInputData() {
        val (phrase, color) = bender.listenAnswer(textMessage.text.toString())
        textMessage.setText("")
        val (r, g, b) = color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)
        text.text = phrase
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString("STATUS", bender.status.name)
        outState?.putString("QUESTION", bender.question.name)
        outState?.putString("TEXT_MESSAGE", textMessage.text.toString())
        Log.d("M_MainActivity", bender.question.question)
        Log.d("M_MainActivity", bender.status.name)
    }
}
