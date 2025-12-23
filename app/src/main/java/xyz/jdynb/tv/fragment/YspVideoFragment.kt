package xyz.jdynb.tv.fragment

import android.os.Bundle
import android.view.View
import xyz.jdynb.tv.enums.JsType
import xyz.jdynb.tv.utils.JsManager.execJs

class YspVideoFragment : VideoFragment() {

  private val _blockFiles = listOf<String>("cctvh5-trace.min.js", "vconsole.min.js", "three.js")

  override val blockFiles: List<String>
    get() = _blockFiles

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    webView.settings.apply {
      blockNetworkImage = true
      loadsImagesAutomatically = false
    }
  }

  override fun onPageFinished(url: String) {
    super.onPageFinished(url)
    webView.execJs(JsType.CLEAR_YSP)
    webView.execJs(JsType.FULLSCREEN_YSP)
  }
}