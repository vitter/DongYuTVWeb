package xyz.jdynb.tv.utils

import android.content.Context
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * 判断是否是电视设备
 */
fun isTv(context: Context): Boolean {
  // 判断手机和平板（通过屏幕尺寸和密度）
  val metrics = context.resources.displayMetrics
  val widthInches = metrics.widthPixels / metrics.xdpi
  val heightInches = metrics.heightPixels / metrics.ydpi
  val diagonalInches = sqrt(widthInches.toDouble().pow(2.0) + heightInches.toDouble().pow(2.0))
  return diagonalInches >= 7.0
}