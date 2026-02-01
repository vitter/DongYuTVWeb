package xyz.jdynb.tv.enums

/**
 * 直播源
 */
enum class LiveSource(val source: String) {
  /**
   * 通用
   */
  COMMON("common"),

  /**
   * CCTV
   */
  CCTV("cctv"),

  /**
   * 央视频
   */
  YSP("ysp");


  companion object {

    @JvmStatic
    fun getForSource(source: String): LiveSource {
      return entries.find { it.source == source } ?: CCTV
    }

  }
}