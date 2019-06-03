package com.racer.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Racer: Game() {

    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont

    override fun create() {
        font = BitmapFont()
        batch = SpriteBatch()
        this.setScreen(MainMenu(this))
    }

    override fun dispose() {
        super.dispose()
        batch.dispose()
        font.dispose()
    }
}

