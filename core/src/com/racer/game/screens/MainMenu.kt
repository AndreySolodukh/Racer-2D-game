package com.racer.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.racer.game.*
import com.racer.game.objects.GameButton
import com.racer.game.objects.TextGameButton

class MainMenu(val game: Racer) : Screen {

    private val res = Resource()
    private val camera = OrthographicCamera(1000f, 2000f)

    private val play = GameButton("play", "play2", 0.15f * res.width,
            0.4f * res.height, 0.7f * res.width, 0.14f * res.height)
    private val cars = TextGameButton("cars", "texted", "texted2",
            0.2f * res.width, 0.32f * res.height, 0.6f * res.width, 0.06f * res.height)
    private val settings = TextGameButton("settings", "texted", "texted2",
            0.2f * res.width, 0.25f * res.height, 0.6f * res.width, 0.06f * res.height)
    private val exit = TextGameButton("exit", "texted", "texted2",
            0.2f * res.width, 0.18f * res.height, 0.6f * res.width, 0.06f * res.height)

    private val stage = Stage()
    private val input = InputMultiplexer()
    private val background = Texture("background.png")

    init {
        background.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
        camera.position.set(Vector3(500f, 1000f, 0f))
        stage.addActor(exit.button)
        stage.addActor(play.button)
        stage.addActor(cars.button)
        stage.addActor(settings.button)
        input.addProcessor(stage)
        Gdx.input.inputProcessor = input
        game.music = res.pref.getBoolean("music", true)
    }

    override fun show() {
        if (game.music) {
            game.theme.play()
            game.theme.volume = 0.6f
            game.theme.isLooping = true
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        res.batch.projectionMatrix = camera.combined

        res.batch.begin()
        res.batch.draw(background, 0f, 0f, 1000f, 2000f)
        res.batch.end()
        stage.draw()

        if (play.button.isChecked) {
            res.sound("click.wav")
            game.theme.stop()
            game.screen = PlayScreen(game)
            dispose()
        }
        if (cars.button.isChecked) {
            res.sound("click.wav")
            game.screen = CarMenu(game)
        }
        if (settings.button.isChecked) {
            res.sound("click.wav")
            game.screen = Settings(game)
        }
        if (exit.button.isChecked) {
            Gdx.app.exit()
            dispose()
        }
    }

    override fun dispose() {
        game.dispose()
        res.dispose()
        play.dispose()
        cars.dispose()
        settings.dispose()
        exit.dispose()
        stage.dispose()
        background.dispose()
    }

    override fun hide() {}
    override fun pause() {}
    override fun resume() {}
    override fun resize(width: Int, height: Int) {}
}