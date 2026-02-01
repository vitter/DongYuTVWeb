package xyz.jdynb.tv.model

import kotlinx.serialization.Serializable

/**
 * Live 配置，对应 /assets/live-3.jsonc 的实体类
 */
@Serializable
data class LiveModel(
  /**
   * 播放器配置列表
   */
  val player: List<Player> = listOf(),
  /**
   * 所有频道配置列表
   */
  val channel: List<LiveChannelTypeModel> = listOf()
) {

  /**
   * 播放器配置
   */
  @Serializable
  data class Player(
    /**
     * 播放器 id，唯一，与 LiveChannelModel 中 player 字段相对应
     * @see LiveChannelModel.player
     */
    val id: String = "",
    /**
     * 播放器名称，与 LivePlayer 中的配置相对应
     *
     * @see xyz.jdynb.tv.enums.LivePlayer
     */
    val name: String = "",
    /**
     * 实际网页的播放地址
     *
     * @sample
     * ```https://www.baidu.com/player/1.html```
     */
    val url: String? = null,
    /**
     * 此播放器对应的脚本配置
     */
    val script: Script = Script(),
    /**
     * 排除配置
     */
    val exclude: Exclude? = Exclude(),
  ) {

    /**
     * 资源排除配置
     */
    @Serializable
    data class Exclude(
      /**
       * 禁用访问指定的地址资源
       */
      val url: List<String>? = listOf(),
      /**
       * 禁用某个地址包含 suffix 后缀的资源
       */
      val suffix: List<String>? = listOf()
    )

    /**
     * 脚本配置
     */
    @Serializable
    data class Script(
      /**
       * 是否异步执行
       */
      val async: Boolean = true,
      /**
       * 初始化执行的脚本
       */
      val init: List<String> = listOf(),
      /**
       * 播放或切换播放时执行的脚本
       */
      val play: List<String> = listOf(),
      /**
       * 暂停或恢复播放执行的脚本
       */
      val resumePause: List<String> = listOf()
    )
  }
}