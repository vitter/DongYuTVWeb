package xyz.jdynb.tv.model

import androidx.databinding.BaseObservable
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import xyz.jdynb.tv.enums.LiveSource

@Serializable
data class LiveItem(
  var num: Int = 0,
  val name: String = "", // 频道名称
  val url: String = "", // 播放地址,
  val logo: String = "",
  val source: String = LiveSource.CCTV.source, // 频道来源
): BaseObservable() {

  @Transient
  val sourceEnum = LiveSource.getForSource(source)

}
