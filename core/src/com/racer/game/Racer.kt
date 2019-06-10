package com.racer.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.racer.game.screens.MainMenu

class Racer: Game() {

    var music = true
    lateinit var theme: Music


    fun switchmusic() {
        if (!music) {
            music = true
            theme.play()
        } else {
            music = false
            theme.stop()
            theme.dispose()
        }
    }

    override fun create() {
        theme = Gdx.audio.newMusic(Gdx.files.internal("title.mp3"))
        setScreen(MainMenu(this))
    }
}

