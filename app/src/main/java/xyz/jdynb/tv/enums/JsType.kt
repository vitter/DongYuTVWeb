package xyz.jdynb.tv.enums

/**
 * JS 类型配置
 */
enum class JsType(val type: String) {
  /**
   * 脚本初始化执行
   */
  INIT("init"),

  /**
   * 播放或换台执行脚本
   */
  PLAY("play"),

  /**
   * 恢复或暂停执行脚本
   */
  RESUME_PAUSE("resume_pause")
  ;
}