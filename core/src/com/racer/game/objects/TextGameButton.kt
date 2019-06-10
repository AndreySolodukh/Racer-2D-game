package com.racer.game.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton

class TextGameButton(text: String, up: String, down: String, x: Float,
                     y: Float, width: Float, height: Float) {

    private val atlas = TextureAtlas("pack.atlas")
    private val generator = FreeTypeFontGenerator(Gdx.files.internal("Pixel.ttf"))
    private val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
    private val skin = Skin()

    init {
        skin.addRegions(atlas)
        parameter.color = Color.WHITE
        parameter.size = Gdx.graphics.height / 30
        parameter.spaceX = parameter.size / 15
    }

    private val font = generator.generateFont(parameter)
    private val style = TextButton.TextButtonStyle(skin.getDrawable(up),
            skin.getDrawable(down), skin.getDrawable(down), font)
    val button = TextButton(text, style)

    init {
        button.setBounds(x, y, width, height)
    }

    fun dispose() {
        atlas.dispose()
        generator.dispose()
        skin.dispose()
        font.dispose()
    }
}