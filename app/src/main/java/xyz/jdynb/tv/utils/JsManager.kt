package xyz.jdynb.tv.utils

import android.content.Context
import android.util.Log
import android.webkit.WebView
import xyz.jdynb.tv.enums.JsType
import java.io.IOException
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws

object JsManager {

  private val jsMap = mutableMapOf<String, String>()

  @Throws(IOException::class)
  fun init(context: Context) {
    JsType.entries.forEach { type ->
      context.assets.open("js/${type.liveSource.source}/${type.filename}.js").use {
        jsMap[type.typeName] =
          it.readBytes().toString(StandardCharsets.UTF_8)
      }
    }
  }

  fun getJs(type: JsType) = jsMap[type.typeName]

  fun WebView.execJs(type: JsType) {
    getJs(type)?.let {
      Log.i("JsManager", "execJs: $it")
      evaluateJavascript(it) { i ->
        Log.i("JsManager", i)
      }
    }
  }
}