package com.racer.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.TimeUtils
import com.racer.game.Racer
import com.racer.game.Resource
import com.racer.game.objects.TextGameButton

class Settings(val game: Racer) : Screen {

    private val res = Resource()
    private val camera = OrthographicCamera(1000f, 2000f)

    private val back = TextGameButton("back", "texted", "texted2",
            0.2f * res.width, 0.18f * res.height, 0.6f * res.width, 0.06f * res.height)
    private val music = TextGameButton("music", "enabled", "disabled",
            0.2f * res.width, 0.3f * res.height, 0.6f * res.width, 0.06f * res.height)
    private val sound = TextGameButton("sound", "enabled", "disabled",
            0.2f * res.width, 0.37f * res.height, 0.6f * res.width, 0.06f * res.height)
    private val reset = TextGameButton("reset record", "texted", "texted2",
            0.2f * res.width, 0.44f * res.height, 0.6f * res.width, 0.06f * res.height)

    private val stage = Stage()
    private val input = InputMultiplexer()
    private val background = Texture("settings.jpg")

    init {
        camera.position.set(Vector3(500f, 1000f, 0f))
        stage.addActor(sound.button)
        stage.addActor(reset.button)
        stage.addActor(music.button)
        stage.addActor(back.button)
        input.addProcessor(stage)
        Gdx.input.inputProcessor = input
        if (!res.pref.getBoolean("sound", true)) sound.button.toggle()
        if (!game.music) music.button.toggle()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        res.batch.projectionMatrix = camera.combined

        res.batch.begin()
        res.batch.draw(background, 0f, 0f, 1000f, 2000f)
        res.batch.end()

        stage.draw()

        if (music.button.isPressed && TimeUtils.millis() - res.pressed > 200) {
            res.sound("click.wav")
            res.pressed = TimeUtils.millis()
            game.switchmusic()
            res.pref.putBoolean("music", game.music)
            res.pref.flush()
        }

        if (sound.button.isPressed && TimeUtils.millis() - res.pressed > 200) {
            res.sound("click.wav")
            res.pressed = TimeUtils.millis()
            res.pref.putBoolean("sound", !res.pref.getBoolean("sound", true))
            res.pref.flush()
        }

        if (reset.button.isChecked) reset.button.toggle()
        if (reset.button.isPressed && TimeUtils.millis() - res.pressed > 200) {
            res.sound("click.wav")
            res.pressed = TimeUtils.millis()
            res.pref.putInteger("record", 0)
            res.pref.flush()
        }
        if (back.button.isChecked) {
            res.sound("click.wav")
            game.screen = MainMenu(game)
            dispose()
        }
    }

    override fun dispose() {
        res.dispose()
        back.dispose()
        music.dispose()
        sound.dispose()
        reset.dispose()
        background.dispose()
        stage.dispose()
    }

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
    override fun resize(width: Int, height: Int) {}
    override fun hide() {}
}