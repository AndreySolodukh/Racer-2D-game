package com.racer.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.racer.game.Racer

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    config.title = "GameScreen"
    config.width = 360
    config.height = 720
    config.vSyncEnabled = false
    config.foregroundFPS = 60
    LwjglApplication(Racer(), config)
}