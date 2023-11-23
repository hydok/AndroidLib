package kr.co.hydok.hydoklib.utils

import android.view.View

class OnSingleClickListener(private val onSingleClick: View.OnClickListener) : View.OnClickListener {

    companion object {
        const val CLICK_INTERVAL = 500
    }

    private var lastClickedTime: Long = 0L

    private fun isSafe(): Boolean {
        return System.currentTimeMillis() - lastClickedTime > CLICK_INTERVAL
    }

    override fun onClick(v: View?) {
        if (isSafe() && v != null) {
            //onSingleClick(v)
            onSingleClick.onClick(v)
        }
        lastClickedTime = System.currentTimeMillis()
    }
}
