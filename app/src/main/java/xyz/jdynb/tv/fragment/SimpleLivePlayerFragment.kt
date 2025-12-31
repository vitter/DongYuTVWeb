package xyz.jdynb.tv.fragment

import android.util.Log
import android.webkit.WebResourceResponse
import xyz.jdynb.tv.enums.JsType
import xyz.jdynb.tv.model.LiveChannelModel

class SimpleLivePlayerFragment : LivePlayerFragment() {

  companion object {

    private const val TAG = "SimpleLivePlayerFragment"

  }

  override fun shouldInterceptRequest(url: String): WebResourceResponse? {
    val shouldIntercept = super.shouldInterceptRequest(url)
    if (url.endsWith("dy-crypto-js.min.js")) {
      // 注入 CRYPTO.JS
      return createCryptoJsResponse()
    }
    return shouldIntercept
  }

  override fun onLoadUrl(url: String?) {
    webView.loadUrl("file:///android_asset/html/simple_player.html")
  }

  override fun play(channel: LiveChannelModel) {
    Log.i(TAG, "play: $channel")
    requireContext().assets.open("js/henan/play.js").use {
      it.readBytes().toString(Charsets.UTF_8)
    }.let {
      webView.evaluateJavascript(it, null)
    }
  }

  override fun onPageFinished(url: String) {
    Log.i(TAG, "init: $url")
    requireContext().assets.open("js/henan/init.js").use {
      it.readBytes().toString(Charsets.UTF_8)
    }.let {
      webView.evaluateJavascript(it, null)
    }
  }

  override fun resumeOrPause() {
    webView.evaluateJavascript("resumeOrPause()", null)
  }
}