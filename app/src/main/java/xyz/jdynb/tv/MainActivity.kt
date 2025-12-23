package xyz.jdynb.tv

import android.Manifest
import android.annotation.SuppressLint
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import xyz.jdynb.tv.databinding.ActivityMainBinding
import xyz.jdynb.tv.enums.LiveSource
import xyz.jdynb.tv.fragment.CCTVVideoFragment
import xyz.jdynb.tv.fragment.VideoFragment
import xyz.jdynb.tv.fragment.YspVideoFragment
import xyz.jdynb.tv.model.LiveItem
import xyz.jdynb.tv.model.MainModel
import xyz.jdynb.tv.utils.JsManager
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {

  companion object {

    private const val TAG = "MainActivity"

  }

  private val fragments = mutableListOf<VideoFragment>()

  private lateinit var binding: ActivityMainBinding

  private val mainModel = MainModel()

  private val numberStringBuilder = StringBuilder()

  private val liveItems = mutableListOf<LiveItem>()

  private val handler = Handler(Looper.getMainLooper())

  private val timeRunnable = Runnable {
    mainModel.showStatus = false
  }

  private val numberRunnable = Runnable {
    mainModel.showStatus = false
    val number = numberStringBuilder.toString().toIntOrNull() ?: return@Runnable
    numberStringBuilder.clear()

    if (number <= 0 || number > (liveItems.lastOrNull()?.num ?: 0)) {
      Log.w(TAG, "Invalid number: $number")
      return@Runnable
    }

    Log.i(TAG, "seekTo number: $number")
    val index = liveItems.indexOfFirst { it.num == number }
    if (index != -1) {
      binding.vp.setCurrentItem(index, false)
    }

  }

  private val currentFragment get() = fragments[binding.vp.currentItem]

  private lateinit var audioManager: AudioManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    insetsController.hide(WindowInsetsCompat.Type.systemBars())

    audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    binding.m = mainModel
    val vp = binding.vp

    val config = getSharedPreferences("config", MODE_PRIVATE)

    vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        liveItems.getOrNull(position)?.let {
          showStatus(it)
          config.edit {
            putInt("num", it.num)
          }
        }
      }
    })

    ActivityCompat.requestPermissions(
      this, arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      ), 1
    )

    lifecycleScope.launch {
      withContext(Dispatchers.IO) {
        JsManager.init(this@MainActivity)
        assets.open("lives.json").use {
          val liveJsonContent = it.readBytes().toString(StandardCharsets.UTF_8)
          val json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
          }

          val liveItems = json.decodeFromString<List<LiveItem>>(liveJsonContent)

          this@MainActivity.liveItems.addAll(liveItems)

          fragments.addAll(liveItems.map { item ->
            when (item.sourceEnum) {
              LiveSource.CCTV -> VideoFragment.newInstance<CCTVVideoFragment>(item)
              LiveSource.YSP -> VideoFragment.newInstance<YspVideoFragment>(item)
              else -> throw IllegalStateException()
            }
          })
        }
      }

      initViewPager(vp)

      /*val currentNum = config.getInt("num", 0)
      vp.setCurrentItem(when {
        currentNum == 0 -> 0
        else -> liveItems.indexOfFirst { it.num == currentNum }
      }, false)*/
    }
  }

  private fun showStatus(liveItem: LiveItem) {
    mainModel.showStatus = true
    mainModel.currentLiveItem = liveItem.copy()
    handler.removeCallbacks(timeRunnable)
    handler.postDelayed(timeRunnable, 5000)
  }

  @SuppressLint("GestureBackNavigation")
  override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

    when (keyCode) {
      /**
       * 上
       */
      KeyEvent.KEYCODE_DPAD_UP -> {
        var currentIndex = binding.vp.currentItem
        if (currentIndex == 0) {
          currentIndex = Int.MAX_VALUE
        } else {
          currentIndex--
        }
        binding.vp.setCurrentItem(currentIndex, true)
      }

      /**
       * 下
       */
      KeyEvent.KEYCODE_DPAD_DOWN -> {
        val currentIndex = binding.vp.currentItem
        val count = binding.vp.adapter?.itemCount ?: 0
        if (currentIndex == count - 1) {
          binding.vp.setCurrentItem(0, true)
        } else {
          binding.vp.setCurrentItem(currentIndex + 1, true)
        }
      }

      // ENTER、OK（确认）
      KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
        currentFragment.pauseOrPlay()
      }

      // 静音
      KeyEvent.KEYCODE_MUTE -> {
        audioManager.setStreamVolume(
          AudioManager.STREAM_MUSIC,
          0,
          AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
        )
      }

      KeyEvent.KEYCODE_VOLUME_UP, KeyEvent.KEYCODE_DPAD_LEFT -> {
        try {
          audioManager.adjustStreamVolume(
            AudioManager.STREAM_SYSTEM,
            AudioManager.ADJUST_LOWER,
            AudioManager.FLAG_SHOW_UI
          )
        } catch (_: SecurityException) {
        }
      }

      KeyEvent.KEYCODE_VOLUME_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT -> {
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)
        if (volume < audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)) {
          try {
            audioManager.adjustStreamVolume(
              AudioManager.STREAM_SYSTEM,
              AudioManager.ADJUST_RAISE,
              AudioManager.FLAG_SHOW_UI
            )
          } catch (_: SecurityException) {
          }
        }
      }

      // 返回
      KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_ESCAPE -> {
        if (mainModel.showStatus) {
          mainModel.showStatus = false
          handler.removeCallbacks(numberRunnable)
        }
      }

      // 主页
      KeyEvent.KEYCODE_HOME -> {

      }

      // 菜单
      KeyEvent.KEYCODE_MENU -> {

      }

      // 0
      // 数字
      KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3,
      KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7,
      KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_9,
      KeyEvent.KEYCODE_NUMPAD_0, KeyEvent.KEYCODE_NUMPAD_1, KeyEvent.KEYCODE_NUMPAD_2,
      KeyEvent.KEYCODE_NUMPAD_3, KeyEvent.KEYCODE_NUMPAD_4, KeyEvent.KEYCODE_NUMPAD_5,
      KeyEvent.KEYCODE_NUMPAD_6, KeyEvent.KEYCODE_NUMPAD_7, KeyEvent.KEYCODE_NUMPAD_8,
      KeyEvent.KEYCODE_NUMPAD_9 -> {
        val num = getNumForKeyCode(keyCode)

        Log.i(TAG, "input number: $num")

        numberStringBuilder.append(num)

        if (!mainModel.showStatus) {
          mainModel.showStatus = true
        }

        mainModel.currentLiveItem.num = numberStringBuilder.toString()
          .toIntOrNull() ?: return super.onKeyUp(keyCode, event)

        mainModel.notifyChange()

        handler.removeCallbacks(numberRunnable)
        handler.postDelayed(numberRunnable, 4000)
      }
    }
    return super.onKeyUp(keyCode, event)
  }

  private fun getNumForKeyCode(keyCode: Int): String {
    return when (keyCode) {
      KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_NUMPAD_0 -> "0"
      KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_NUMPAD_1 -> "1"
      KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_NUMPAD_2 -> "2"
      KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_NUMPAD_3 -> "3"
      KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_NUMPAD_4 -> "4"
      KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_NUMPAD_5 -> "5"
      KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_NUMPAD_6 -> "6"
      KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_NUMPAD_7 -> "7"
      KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_NUMPAD_8 -> "8"
      KeyEvent.KEYCODE_9, KeyEvent.KEYCODE_NUMPAD_9 -> "9"
      else -> ""
    }
  }

  private fun initViewPager(vp: ViewPager2) {
    vp.run {
      isUserInputEnabled = false
      offscreenPageLimit = 1
      adapter = MainPageAdapter()
    }
  }

  inner class MainPageAdapter : FragmentStateAdapter(supportFragmentManager, lifecycle) {

    override fun createFragment(position: Int) = fragments[position]

    override fun getItemCount() = fragments.size

  }
}