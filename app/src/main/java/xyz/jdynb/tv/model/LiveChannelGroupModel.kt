package xyz.jdynb.tv.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import xyz.jdynb.tv.BR

data class LiveChannelGroupModel(
  val channelType: String = "",
  val channelList: List<LiveChannelModel> = listOf()
): BaseObservable() {

  @get:Bindable
  var isSelected: Boolean = false
    set(value) {
      field = value
      notifyPropertyChanged(BR.selected)
    }

}