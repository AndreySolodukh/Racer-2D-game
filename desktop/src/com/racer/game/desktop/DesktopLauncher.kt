package com.racer.game.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.racer.game.Racer

fun main(args: Array<String>) {

    val config = LwjglApplicationConfiguration()
    config.addIcon("icon32.ico", Files.FileType.Internal)
    config.title = "RACER"
    config.width = 300
    config.height = 600
    config.vSyncEnabled = false
    config.foregroundFPS = 60
    config.x = 600
    config.y = 50
    LwjglApplication(Racer(), config)
}