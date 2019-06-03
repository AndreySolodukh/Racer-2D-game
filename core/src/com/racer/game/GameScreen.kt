package com.racer.game

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.TimeUtils

class GameScreen(gam: Racer) : Screen {

    var game = gam
    private val camera = OrthographicCamera()
    private val car = Rectangle(153.25f, 15f, 53.5f, 115.5f)
    private val carTexture = Texture("car.png")
    private val obstacleTexture = Texture("obstacle.png")
    private var obstacles = mutableListOf<Rectangle>()
    private val soundtrack = Gdx.audio.newMusic(Gdx.files.internal("MainMenuBG.mp3"))
    private var lastObstacleTime: Long = 0
    private var carSpeed = 360f
    private var obstacleSpeed = 300f
    private var obstacleCount = 0
    private var spawnTime: Float = 2f
    private val touchPos = Vector3()
    private var passedDistance = 0L
    private val startValue = TimeUtils.millis()
    private var gameOver = false



    private lateinit var obstacle: Rectangle
    private fun spawnObstacle() {
        obstacle = Rectangle(random(0f, 300f), 720f, 60f, 60f)
        obstacles.add(obstacle)
        lastObstacleTime = TimeUtils.nanoTime()
    }

    init {
        camera.setToOrtho(false, 360f, 720f)
        spawnObstacle()
    }



    override fun render(delta: Float) {

        if (gameOver && Gdx.input.justTouched()) {
            game.screen = MainMenu(game)
            dispose()
        }

        if (!gameOver) Gdx.gl.glClearColor(0.6f, 0.4f, 0.5f, 0.3f)
        else Gdx.gl.glClearColor(0.6f, 0f, 0f, 0.3f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()

        game.font.draw(game.batch, "Points: $passedDistance", 20f, 500f)

        game.batch.draw(carTexture, car.x, car.y, car.width, car.height)
        if (!gameOver)
            for (elem in obstacles)
                game.batch.draw(obstacleTexture, elem.x, elem.y, elem.width, elem.height)
        game.batch.end()

        when {
            Gdx.input.isKeyPressed(Input.Keys.A) -> car.x -= carSpeed * Gdx.graphics.deltaTime
            Gdx.input.isKeyPressed(Input.Keys.D) -> car.x += carSpeed * Gdx.graphics.deltaTime
            else -> if (Gdx.input.isTouched) {
                touchPos.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
                camera.unproject(touchPos)
                if (car.x > touchPos.x - car.width / 2)
                    car.x -= carSpeed * Gdx.graphics.deltaTime
                if (car.x < touchPos.x - car.width / 2)
                    car.x += carSpeed * Gdx.graphics.deltaTime
            }
        }

        if (car.x < 15f) car.x = 15f
        if (car.x > 291.5f) car.x = 291.5f
        if (TimeUtils.nanoTime() - lastObstacleTime > 1000000000 * spawnTime) spawnObstacle()
        if (!gameOver) passedDistance = (TimeUtils.millis() - startValue) / 100

        val iterator = obstacles.iterator()
        while (iterator.hasNext()) {
            obstacle = iterator.next()
            obstacle.y -= obstacleSpeed * Gdx.graphics.deltaTime
            if (obstacle.y + 60 < 0) {
                iterator.remove()
                obstacleCount++
                if (obstacleCount % 5 == 0) {
                    obstacleSpeed *= 1.1f
                    spawnTime /= 1.1f
                }
            }
            if (obstacle.overlaps(car)) {
                gameOver = true
                carSpeed = 0f
                obstacleSpeed = 0f
                soundtrack.stop()
            }
        }
    }

    override fun show() {
        soundtrack.play()
        soundtrack.volume = 0.2f
        soundtrack.isLooping = true
    }

    override fun dispose() {
        carTexture.dispose()
        obstacleTexture.dispose()
        soundtrack.dispose()
    }

        override fun hide() { }

        override fun pause() { }

        override fun resume() { }

        override fun resize(width: Int, height: Int) { }
}
