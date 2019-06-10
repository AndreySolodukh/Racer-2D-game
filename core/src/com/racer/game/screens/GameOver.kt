package com.racer.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.racer.game.*
import com.racer.game.objects.TextGameButton

class GameOver(private val score: Int, val game: Racer) : Screen {

    private val res = Resource()
    private val camera = OrthographicCamera(1000f, 2000f)

    private val again = TextGameButton("play again", "texted", "texted2",
            0.2f * res.width, 0.11f * res.height, 0.6f * res.width, 0.06f * res.height)
    private val menu = TextGameButton("menu", "texted", "texted2",
            0.2f * res.width, 0.03f * res.height, 0.6f * res.width, 0.06f * res.height)

    private val background = Texture("gameover.png")
    private val stage = Stage()
    private val input = InputMultiplexer()

    init {
        camera.position.set(Vector3(500f, 1000f, 0f))
        stage.addActor(again.button)
        stage.addActor(menu.button)
        input.addProcessor(stage)
        Gdx.input.inputProcessor = input
        res.parameter.spaceX = 5
        res.parameter.color = Color.BLACK
        res.parameter.size = 80
    }

    private val font: BitmapFont = res.generator.generateFont(res.parameter)

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        res.batch.projectionMatrix = camera.combined

        res.batch.begin()
        res.batch.draw(background, 0f, 0f, 1000f, 2000f)
        font.draw(res.batch, "Your score - $score",
                500f, 1500f, 0f, 1, false)
        if (score >= res.pref.getInteger("record"))
            font.draw(res.batch, "New record!", 500f, 1300f, 0f, 1, false)
        else
            font.draw(res.batch, "Your record - ${res.pref.getInteger("record")}",
                    500f, 1300f, 0f, 1, false)
        res.batch.end()
        stage.draw()

        if (again.button.isChecked) {
            res.sound("click.wav")
            game.screen = PlayScreen(game)
            dispose()
        }
        if (menu.button.isChecked) {
            res.sound("click.wav")
            game.screen = MainMenu(game)
            dispose()
        }
    }

    override fun dispose() {
        game.dispose()
        res.dispose()
        again.dispose()
        menu.dispose()
        background.dispose()
        stage.dispose()
        font.dispose()
    }

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
    override fun resize(width: Int, height: Int) {}
    override fun hide() {}
}