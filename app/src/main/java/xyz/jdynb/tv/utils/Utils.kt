package xyz.jdynb.tv.utils

import android.os.Bundle
import androidx.core.os.bundleOf

fun Any?.toBundle(): Bundle {
  return this?.javaClass?.let { clazz ->
    val fields = clazz.declaredFields
    val bundle = bundleOf()
    fields.forEach {  field ->
      field.isAccessible = true
      val name = field.name
      when (val value = field.get(this)) {
        is String -> bundle.putString(name, value)
        is Int -> bundle.putInt(name, value)
        is Boolean -> bundle.putBoolean(name, value)
      }
    }
    bundle
  } ?: Bundle.EMPTY
}

inline fun <reified T> Bundle.toObj(): T? {
  val objClass = T::class.java
  val obj = objClass.getDeclaredConstructor().newInstance()
  objClass.declaredFields.forEach {
    it.isAccessible = true
    val value = get(it.name)
    it.set(obj, value)
  }
  return obj
}