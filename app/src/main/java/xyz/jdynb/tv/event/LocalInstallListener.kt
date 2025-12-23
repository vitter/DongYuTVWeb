package xyz.jdynb.tv.event

interface LocalInstallListener {

  fun onSuccess()

  fun onError(msg: String)

}