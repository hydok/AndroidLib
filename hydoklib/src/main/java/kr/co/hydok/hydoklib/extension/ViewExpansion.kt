package kr.co.hydok.hydoklib.extension

import android.animation.Animator
import android.app.Activity
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import kr.co.hydok.hydoklib.utils.OnSingleClickListener

/**
* View 확장 함수 정의
* @author hydok
* @version 1.0.0
* @since 2022/10/21 5:26 오후
**/

fun View.setPaddingLeft(left: Int) = setPadding(left, paddingTop, paddingRight, paddingBottom)

fun View.setPaddingTop(top: Int) = setPadding(paddingLeft, top, paddingRight, paddingBottom)

fun View.setPaddingRight(right: Int) = setPadding(paddingLeft, paddingTop, right, paddingBottom)

fun View.setPaddingBottom(bottom: Int) = setPadding(paddingLeft, paddingTop, paddingRight, bottom)

fun View.rotate(rotate: Float) {
    animate().rotation(rotate).start()
}

fun TextView.textChangeFadeIn(str:String){
    val inAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    this.text = str
    this.startAnimation(inAnim)
}

fun TextView.highlight(pointText: String, color: Int, isBold: Boolean = false, ratio: Float = 1f, lastIndex: Boolean = false) {
    val orgText = this.text
    val spannable = SpannableString(orgText)

    val start = if (lastIndex) {
        orgText.lastIndexOf(pointText)
    } else {
        orgText.indexOf(pointText)
    }
    val end = start + pointText.length

    if (start > -1) {
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, color)),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            RelativeSizeSpan(ratio),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        if (isBold) {
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        this.text = spannable
    }
}

fun View.setSingleClickListener(listener: View.OnClickListener) {
    this.setOnClickListener(OnSingleClickListener(listener))
}

fun View.setHeight(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.height = value
        layoutParams = lp
    }
}

fun View.setWidth(value: Int) {
    val lp = layoutParams
    lp?.let {
        lp.width = value
        layoutParams = lp
    }
}

fun View.getLocationY(): Int {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return location[1]
}

fun View.getLocationX(): Int {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return location[0]
}

fun View.fadeIn(ms: Long) {
    if (this.visibility == View.GONE) {
        visibility = View.VISIBLE
        alpha = 0f
        animate()
            .alpha(1f)
            .setDuration(ms)
            .setListener(null)
    }
}

fun View.fadeOut(ms: Long) {
    if (this.visibility == View.VISIBLE) {
        alpha = 1f
        animate()
            .alpha(0f)
            .setDuration(ms)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {}
                override fun onAnimationEnd(p0: Animator) {
                    visibility = View.GONE
                }

                override fun onAnimationCancel(p0: Animator) {}
                override fun onAnimationRepeat(p0: Animator) {}
            })
    }
}

/**
 *  statusBar, 하단 메뉴바 관계없이 전체 화면으로
 */
fun Activity.setStatusBarTransparent() {
    window.apply {
        setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
    if(Build.VERSION.SDK_INT >= 30) {	// API 30 에 적용
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
fun Window.setStatusBarIconColorLight(isLight :Boolean){
    //프로젝트 초기 설정값은 dark icon
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if(isLight){
            this.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            this.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    } else {
        @Suppress("DEPRECATION")
        this.decorView.systemUiVisibility = if(isLight){
            0
        } else {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}