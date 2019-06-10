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
import com.racer.game.*
import com.racer.game.objects.GameButton
import com.racer.game.objects.TextGameButton

class CarMenu(val game: Racer) : Screen {

    private val res = Resource()
    private val camera = OrthographicCamera(1000f, 2000f)

    private val cars = listOf(
            GameButton("car0no", "car0yes", 0.0475f * res.width, 0.7f * res.height,
                    0.27f * res.width, 0.27f * res.height),
            GameButton("car1no", "car1yes", 0.365f * res.width, 0.7f * res.height,
                    0.27f * res.width, 0.27f * res.height),
            GameButton("car2no", "car2yes", 0.6825f * res.width, 0.7f * res.height,
                    0.27f * res.width, 0.27f * res.height),
            GameButton("car3no", "car3yes", 0.0475f * res.width, 0.4f * res.height,
                    0.27f * res.width, 0.27f * res.height),
            GameButton("car4no", "car4yes", 0.365f * res.width, 0.4f * res.height,
                    0.27f * res.width, 0.27f * res.height),
            GameButton("car5no", "car5yes", 0.6825f * res.width, 0.4f * res.height,
                    0.27f * res.width, 0.27f * res.height)
    )
    private val back = TextGameButton("back", "texted", "texted2",
            0.2f * res.width, 0.18f * res.height, 0.6f * res.width, 0.06f * res.height)

    private val stage = Stage()
    private val input = InputMultiplexer()
    private val background = Texture("settings.jpg")

    init {
        camera.position.set(Vector3(500f, 1000f, 0f))
        for (elem in cars) stage.addActor(elem.button)
        stage.addActor(back.button)
        input.addProcessor(stage)
        Gdx.input.inputProcessor = input
        cars[res.pref.getInteger("car", 0)].button.toggle()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        res.batch.projectionMatrix = camera.combined

        res.batch.begin()
        res.batch.draw(background, 0f, 0f, 1000f, 2000f)
        res.batch.end()

        stage.draw()

        if (cars.any { it.button.isPressed } && TimeUtils.millis() - res.pressed > 200) {
            res.sound("click.wav")
            val picked = cars.first { it.button.isPressed }
            cars.map { if (it != picked && it.button.isChecked) it.button.toggle() }
            if (picked.button.isChecked) picked.button.toggle()
            res.pref.putInteger("car", cars.indexOf(picked))
            res.pref.flush()
            res.pressed = TimeUtils.millis()
        }
        if (back.button.isChecked) {
            res.sound("click.wav")
            game.screen = MainMenu(game)
            dispose()
        }
    }

    override fun dispose() {
        res.dispose()
        for (elem in cars) elem.dispose()
        back.dispose()
        background.dispose()
        stage.dispose()
    }

    override fun hide() {}
    override fun show() {}
    override fun pause() {}
    override fun resume() {}
    override fun resize(width: Int, height: Int) {}


}