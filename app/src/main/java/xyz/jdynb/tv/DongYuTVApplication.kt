package xyz.jdynb.tv

import android.app.Application
import com.drake.brv.utils.BRV
import com.drake.engine.base.Engine

class DongYuTVApplication: Application() {

  override fun onCreate() {
    super.onCreate()

    Engine.initialize(this)
    BRV.modelId = BR.m
  }

}