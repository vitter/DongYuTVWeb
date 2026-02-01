package xyz.jdynb.tv.utils

import android.util.Log
import android.webkit.WebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import xyz.jdynb.tv.BuildConfig
import xyz.jdynb.tv.DongYuTVApplication
import xyz.jdynb.tv.enums.JsType
import xyz.jdynb.tv.model.LiveModel
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/**
 * 操作 JS 脚本
 */
object JsManager {

  /**
   * js 脚本表，用于缓存已加载的 js 脚本文件，提升性能
   */
  private val jsMap = mutableMapOf<String, List<String>>()

  private fun createConnection(url: String): HttpURLConnection {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.connectTimeout = 3000
    connection.readTimeout = 3000
    connection.requestMethod = "GET"
    // connection.setRequestProperty("Content-Type", "application/json")
    connection.setRequestProperty("Accept", "application/json")
    return connection
  }

  private fun createJsFile(name: String, type: JsType): File {
    val file = File(DongYuTVApplication.context.filesDir, "js/${name}")
    if (!file.exists()) {
      file.mkdirs()
    }
    val jsFile = File(
      file, when (type) {
        JsType.INIT -> "init.js"
        JsType.PLAY -> "play.js"
        JsType.RESUME_PAUSE -> "resume_pause.js"
      }
    )
    return jsFile
  }

  /**
   * 写入 js 文件到本地目录中
   */
  suspend fun writeJsToLocal(name: String, type: JsType, content: String) = withContext(Dispatchers.IO) {
    createJsFile(name, type).writeText(content)
  }

  /**
   * 从本地获取 js 脚本文件
   */
  suspend fun getJsFromLocal(name: String, type: JsType) =
    withContext(Dispatchers.IO) {
      val jsFile = createJsFile(name, type)
      if (jsFile.exists()) {
        jsFile.readText()
      } else {
        null
      }
    }

  /**
   * 从 assets 中的文件获取 js 脚本
   *
   * @param name 目录名称，和 player 配置文件中的 id 相对应
   * @param type js 类型
   *
   * @see LiveModel
   */
  suspend fun getJsFromAsset(name: String, type: JsType) = withContext(Dispatchers.IO) {
    DongYuTVApplication.context.assets.open("js/${name}/${type.type}.js")
      .readBytes().toString(Charsets.UTF_8)
  }

  private suspend fun getOrWriteJs(url: String, name: String, type: JsType): String? {
    return NetworkUtils.getResponseBody(url)?.also { content ->
      writeJsToLocal(name, type, content)
    } ?: getJsFromLocal(name, type)
  }

  /**
   * 加载 js 列表
   *
   * 加载策略：
   * 1. 先从网络加载，加载成功后保存到 本地
   * 2. 如果网络加载失败，则使用本地已保存的
   *
   * @param playerConfig 播放器配置
   * @param type js 类型
   */
  @Suppress("UNCHECKED_CAST")
  suspend fun getJsList(playerConfig: LiveModel.Player, type: JsType) =
    withContext(Dispatchers.IO) {
      try {
        if (BuildConfig.DEBUG) {
          return@withContext listOf(getJsFromAsset(playerConfig.id, type))
        }
        val scripts = when (type) {
          JsType.INIT -> playerConfig.script.init
          JsType.PLAY -> playerConfig.script.play
          JsType.RESUME_PAUSE -> playerConfig.script.resumePause
        }
        if (playerConfig.script.async) {
          scripts.map {
            async {
              getOrWriteJs(it, playerConfig.id, type)
            }
          }.map {
            it.await()
          }
        } else {
          scripts.map {
            getOrWriteJs(it, playerConfig.id, type)
          }
        }
      } catch (e: Exception) {
        Log.e("JsManager", e.message, e)
        null
      }
    } as List<String>?

  /**
   * 通过 player 配置和 js类型 获取对应需要执行的 js 脚本列表
   *
   * @param playerConfig 播放配置
   * @param type js 类型
   *
   * @see LiveModel
   */
  suspend fun getJs(playerConfig: LiveModel.Player, type: JsType): List<String>? {
    var jsList = jsMap[playerConfig.id + "-" + type.type]
    if (jsList.isNullOrEmpty()) {
      jsList = getJsList(playerConfig, type)?.also {
        jsMap[playerConfig.id + "-" + type.type] = it
      }
    }
    return jsList
  }

  /**
   * 扩展WebView执行JS脚本
   *
   * 详细的配置请查看文件 /assets/live-3.jsonc
   *
   * @param playerConfig 播放配置
   * @param type js类型配置
   * @param args 所需注入的参数
   *
   * @see LiveModel
   */
  suspend fun WebView.execJs(
    playerConfig: LiveModel.Player,
    type: JsType,
    vararg args: Pair<String, Any?>
  ) {
    Log.i("JsManager", "playConfig: $playerConfig, $type")
    getJs(playerConfig, type)?.let {
      var index = 0
      it.forEach { jsStr ->
        var result = jsStr
        for ((key, value) in args.slice(index until args.size)) {
          index++
          if (value != null) {
            result = result.replace("{{${key}}}", value.toString())
          }
        }
        Log.i("JsManager", "js: $result")
        evaluateJavascript(result) { i ->
          // Log.i("JsManager", i)
        }
      }
    }
  }
}