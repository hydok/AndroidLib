package kr.co.hydok.hydoklib.extension

import java.text.DecimalFormat

/**
* Int 확장 함수 정의
* @author hydok
* @version 1.0.0
* @since 2022/10/21 5:26 오후
**/

fun Int.moneyFormat(showWon: Boolean): String {
    val decimalFormat = if (showWon) DecimalFormat("###,###원") else DecimalFormat("###,###")
    return decimalFormat.format(this)
}

/*
fun Int.pixelToDp(): Int {
    val resources = MainApplication.applicationCtx.resources
    val metrics = resources.displayMetrics
    return (this.toFloat() / (metrics.densityDpi / 160f)).toInt()
}

fun Int.dpToPixel(): Int {
    val resources = MainApplication.applicationCtx.resources
    val metrics = resources.displayMetrics
    return (this.toFloat() * (metrics.densityDpi / 160f)).toInt()
}*/
