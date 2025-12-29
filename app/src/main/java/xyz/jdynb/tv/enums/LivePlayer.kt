package xyz.jdynb.tv.enums

import xyz.jdynb.tv.fragment.YspLivePlayerFragment

/**
 * 播放器配置
 */
enum class LivePlayer(private val channelTypes: Array<String>, val clazz: Class<*>) {
  /**
   * 央视频播放器
   */
  YSP(arrayOf("央视", "卫视"), YspLivePlayerFragment::class.java);

  companion object {

    @JvmStatic
    fun getLivePlayerForChannelType(channelType: String): LivePlayer {
      return LivePlayer.entries.find { it.channelTypes.contains(channelType) } ?: YSP
    }

  }
}