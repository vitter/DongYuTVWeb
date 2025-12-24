package xyz.jdynb.tv.model

data class LiveChannelGroupModel(
  val channelType: String = "",
  val channelList: List<LiveChannelModel> = listOf()
)