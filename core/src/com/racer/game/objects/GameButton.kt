package com.racer.game.objects

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class GameButton(up: String, down: String, x: Float, y: Float, width: Float, height: Float) {

    private val skin = Skin()
    private val atlas = TextureAtlas("pack.atlas")

    init {
        skin.addRegions(atlas)
    }

    private val style = Button.ButtonStyle(skin.getDrawable(up), skin.getDrawable(down),
            skin.getDrawable(down))
    val button = Button(style)

    init {
        button.setBounds(x, y, width, height)
    }

    fun dispose() {
        skin.dispose()
        atlas.dispose()
    }
}