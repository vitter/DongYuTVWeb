package xyz.jdynb.tv.enums

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