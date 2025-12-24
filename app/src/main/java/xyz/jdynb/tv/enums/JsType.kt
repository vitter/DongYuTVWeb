package xyz.jdynb.tv.enums

enum class JsType(val filename: String, val liveSource: LiveSource) {
  FULLSCREEN_YSP("auto_fullscreen", LiveSource.YSP),

  CLEAR_YSP("clear", LiveSource.YSP),

  PLAY_YSP("play", LiveSource.YSP),

  PLAY_PAUSE("play_pause", LiveSource.YSP)

  ;

  val typeName = filename + liveSource.source
}