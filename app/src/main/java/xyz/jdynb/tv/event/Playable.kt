package xyz.jdynb.tv.event

import xyz.jdynb.tv.model.LiveChannelModel

/**
 * 可播放的接口
 */
interface Playable {

  /**
   * 播放
   *
   * @param channel 频道模型
   */
  fun play(channel: LiveChannelModel)

  /**
   * 恢复或暂停
   */
  fun resumeOrPause()
}