package com.racer.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import java.awt.Button


class MainMenu(gam: Racer): Screen {

    var game = gam
    private val camera = OrthographicCamera()
    private val play = Rectangle(44f, 120f, 272f, 110f)
    private val playTexture = Texture("play.png")
    private val bg = Rectangle(0f, 0f, 360f, 720f)
    private val bgTexture = Texture("font.jpg")
    private val playbutton = Button("play.png")


    init {

        camera.setToOrtho(false, 360f, 720f)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.6f, 0.4f, 0.5f, 0.3f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()

        game.batch.projectionMatrix = camera.combined
        game.batch.begin()
        game.batch.draw(bgTexture, bg.x, bg.y, bg.width, bg.height)
        game.batch.draw(playTexture, play.x, play.y, play.width, play.height)
        game.batch.end()

        if (Gdx.input.justTouched()) {
            game.screen = GameScreen(game)
            dispose()
        }

    }

    override fun hide() { }

    override fun show() { }

    override fun pause() { }

    override fun resume() { }

    override fun resize(width: Int, height: Int) { }

    override fun dispose() { }
}