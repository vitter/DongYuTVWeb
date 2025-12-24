package xyz.jdynb.tv.model

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import xyz.jdynb.tv.BR

class LivePlayerModel: BaseObservable() {

  @Bindable
  var progress: Int = 0
    set(value) {
      field = value
      notifyPropertyChanged(BR.progress)
    }

  @get:Bindable("progress")
  val progressText: String get() = "正在加载中...(${progress}%)"

  @get:Bindable("progress")
  val showLoading: Int get() = if (progress < 100) View.VISIBLE else View.INVISIBLE
}