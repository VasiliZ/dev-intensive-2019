package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import ru.skillbranch.devintensive.R

fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.isKeyboardOpen(): Boolean {
    return checkKeyboard(this)
}

fun Activity.isKeyboardClosed(): Boolean {
    return !checkKeyboard(this)
}

private fun checkKeyboard(activity: Activity): Boolean {
    var status = false
    val rootView: View = activity.findViewById(R.id.activity_root)

    rootView.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        val heightWindow = rootView.height
        val keyboardHeight = rootView.height - rect.height()
        status = keyboardHeight > heightWindow * 0.15
    }
    return status
}

