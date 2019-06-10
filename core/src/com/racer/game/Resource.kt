package com.racer.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.Skin

class Resource {

    val pref: Preferences = Gdx.app.getPreferences("settings-car-and-record")

    val batch = SpriteBatch()
    val atlas = TextureAtlas("pack.atlas")
    val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
    val generator = FreeTypeFontGenerator(Gdx.files.internal("Pixel.ttf"))
    private val skin = Skin()

    init {
        skin.addRegions(atlas)
        parameter.size = 80
        parameter.color = Color.WHITE
    }

    private val font: BitmapFont = generator.generateFont(parameter)

    var pressed = 0L
    val width = Gdx.graphics.width.toFloat()
    val height = Gdx.graphics.height.toFloat()

    fun sound(name: String) {
        if (pref.getBoolean("sound", true))
            Gdx.audio.newSound(Gdx.files.internal(name)).play(2f)
    }

    fun dispose() {
        batch.dispose()
        atlas.dispose()
        generator.dispose()
        font.dispose()
    }
}