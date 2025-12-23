package xyz.jdynb.tv.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import xyz.jdynb.tv.BR

class MainModel: BaseObservable() {

  @get:Bindable
  var showStatus: Boolean = false
    set(value) {
      field = value
      notifyPropertyChanged(BR.showStatus)
    }

  @get:Bindable
  var currentLiveItem: LiveItem = LiveItem()
    set(value) {
      field = value
      notifyPropertyChanged(BR.currentLiveItem)
    }

}