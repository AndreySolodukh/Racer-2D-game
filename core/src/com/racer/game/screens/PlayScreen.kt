package com.racer.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.TimeUtils
import com.racer.game.objects.Prop
import com.racer.game.Racer
import com.racer.game.Resource
import com.racer.game.objects.TextGameButton
import com.racer.game.objects.Car
import com.racer.game.objects.GameButton


class PlayScreen(val game: Racer) : Screen {

    private val res = Resource()
    private val camera = OrthographicCamera(1000f, 2000f)

    private val pause = GameButton("pause", "pause2", 0.88f * res.width,
            0.94f * res.height, 0.12f * res.width, 0.06f * res.height)
    private val menu = TextGameButton("menu", "texted", "texted2", 0.2f * res.width,
            0.32f * res.height, 0.6f * res.width, 0.06f * res.height)

    private val stage = Stage()
    private val pauseStage = Stage()
    private val input = InputMultiplexer()

    init {
        camera.position.set(Vector3(500f, 1000f, 0f))
        stage.addActor(pause.button)
        pauseStage.addActor(menu.button)
        input.addProcessor(stage)
        Gdx.input.inputProcessor = input
        res.parameter.spaceX = 5
        res.parameter.borderColor = Color.BLACK
        res.parameter.size = 70
        res.parameter.borderWidth = 6f
        game.theme = Gdx.audio.newMusic(Gdx.files.internal("GameTheme.mp3"))
    }

    private val font = res.generator.generateFont(res.parameter)
    private val car = Car(300f, 700f,
            Sprite(res.atlas.findRegion("car${res.pref.getInteger("car", 0)}")),
            415f, 100f, 127.3f, 272.7f)
    private val props = mutableSetOf<Prop>()
    private val shield = Rectangle(-200f, 380f, 200f, 60f)
    private val shieldTexture = res.atlas.findRegion("barrier")
    private val road = Rectangle(0f, 0f, 1000f, 2000f)
    private val roadTexture = Texture("road.jpg")

    init {
        roadTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
    }

    private var time = TimeUtils.millis()
    private var lastObs = 0
    private var obSpeed = 1000
    private var doubler = 1
    private var score = 0
    private var bonusScore = 0
    private var gameOver = false
    private var barrier = false
    private var paused = false

    private fun make(type: String): Prop =
            if (type == "obs") {
                val x = setOf(random(80f, 400f),
                        random(505f, 770f)).shuffled().first()
                val texture = Sprite(res.atlas.findRegion("car${random(0, 5)}"))
                if (x > 500f) {
                    Prop(texture, x, 2000f, 127.3f, 272.7f, type, 0.85f)
                } else {
                    texture.rotation = 180f
                    Prop(texture, x, 2000f, 127.3f, 272.7f, type, 1.15f)
                }
            } else {
                val x = random(80f, 820f)
                val texture = Sprite(res.atlas.findRegion(type))
                Prop(texture, x, 2000f, 91f, 91f, type, 1f)
            }

    private fun spawnObstacle() {
        props.removeAll(props.filter { it.y < -it.height })
        if (score - bonusScore >= 100) {
            val bonus = make(setOf("shield", "doubler").shuffled().first())
            props.add(bonus)
            bonusScore = score
            lastObs += 10 * doubler
        }
        if ((score - lastObs > (4 - (score / 750)) * doubler) && (score - bonusScore < 95)) {
            val obstacle = make("obs")
            props.add(obstacle)
            obSpeed += 15
            lastObs = score
        }
    }

    override fun show() {
        if (game.music) {
            game.theme.play()
            game.theme.volume = 0.5f
            game.theme.isLooping = true
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        res.batch.projectionMatrix = camera.combined

        res.batch.begin()
        if (road.y > -2000f) {
            res.batch.draw(roadTexture, road.x, road.y, 1000f, 2000f)
            res.batch.draw(roadTexture, road.x, road.y + 2000f, 1000f, 2000f)
            road.y -= obSpeed * delta
        } else {
            road.y += 2000f
            res.batch.draw(roadTexture, road.x, road.y, 1000f, 2000f)
            res.batch.draw(roadTexture, road.x, road.y + 2000f, 1000f, 2000f)
        }
        for (elem in props) {
            if (!gameOver) elem.y -= obSpeed * delta * elem.boost
            elem.bounds.y = elem.y
            elem.bounds.x = elem.x
            elem.draw(res.batch)
        }
        car.draw(res.batch)

        if (!gameOver) {
            res.batch.draw(Texture("board.jpg"), 0f, 1880f, 1000f, 120f)
            if (!paused) {
                spawnObstacle()
                font.draw(res.batch, "Score - $score", 300f, 1960f, 0f, 1, false)
                if (TimeUtils.millis() - time > 200) {
                    score += doubler
                    time = TimeUtils.millis()
                }
                // чтобы при нажатии pause.button машинка не отклонилась вправо
                if (!pause.button.isPressed) car.controls()

                if (doubler == 2 && bonusScore - score > 0) {
                    res.batch.draw(Sprite(res.atlas.findRegion("doubler")),
                            20f, 1710f, 150f, 150f)
                    res.batch.draw(Sprite(res.atlas.findRegion("duration")), 20f, 1670f,
                            (bonusScore - score) * 1.25f, 20f)
                } else {
                    if (doubler == 2) res.sound("unbeep.wav")
                    doubler = 1
                }
                if (barrier && bonusScore - score > 0) {
                    res.batch.draw(Sprite(res.atlas.findRegion("shield")),
                            20f, 1710f, 132f, 150f)
                    res.batch.draw(Sprite(res.atlas.findRegion("duration")), 20f, 1670f,
                            (bonusScore - score) * 2.2f, 20f)
                    shield.x = car.bounds.x - 33f
                    res.batch.draw(shieldTexture, shield.x, shield.y, shield.width, shield.height)
                } else {
                    if (shield.x != -200f) res.sound("unbeep.wav")
                    shield.x = -200f
                }
                if (props.any { it.bounds.overlaps(shield) }) {
                    props.remove(props.first { it.bounds.overlaps(shield) })
                    res.sound("unbeep.wav")
                    barrier = false
                    shield.x = -200f
                    bonusScore = score
                }
                if (props.any { it.bounds.overlaps(car.bounds) }) {
                    val prop = props.first { it.bounds.overlaps(car.bounds) }
                    when (prop.type) {
                        "obs" -> {
                            res.sound("crush.wav")
                            obSpeed = 0
                            game.theme.stop()
                            gameOver = true
                        }
                        "doubler" -> {
                            res.sound("beep.wav")
                            doubler = 2
                            bonusScore += 120
                            props.remove(prop)
                        }
                        "shield" -> {
                            res.sound("beep.wav")
                            barrier = true
                            bonusScore += 60
                            props.remove(prop)
                        }
                    }
                }
            } else {
                font.draw(res.batch, "Score - $score", 300f, 1960f, 0f, 1, false)
                font.draw(res.batch, "paused", 500f, 1250f, 0f, 1, false)
            }
        } else {
            if (score > res.pref.getInteger("record", 0)) {
                res.pref.putInteger("record", score)
                res.pref.flush()
            }
            font.draw(res.batch,
                    "game over\n\n\nclick anywhere\nor press enter\nto continue",
                    500f, 1400f, 0f, 1, false)
        }
        res.batch.end()
        if (!gameOver) stage.draw()
        else
            if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                game.theme = Gdx.audio.newMusic(Gdx.files.internal("title.mp3"))
                game.screen = GameOver(score, game)
                dispose()
            }
        if (paused) {
            pauseStage.draw()
            if (menu.button.isChecked) {
                res.sound("click.wav")
                game.theme = Gdx.audio.newMusic(Gdx.files.internal("title.mp3"))
                game.screen = MainMenu(game)
                dispose()
            }
        }
        if (pause.button.isPressed && TimeUtils.millis() - res.pressed > 200) {
            res.sound("click.wav")
            res.pressed = TimeUtils.millis()
            if (!paused) {
                input.addProcessor(pauseStage)
                game.theme.pause()
                paused = true
                time = obSpeed.toLong()
                obSpeed = 0
            } else {
                input.removeProcessor(pauseStage)
                game.theme.play()
                paused = false
                obSpeed = time.toInt()
                time = TimeUtils.millis()
            }
        }
    }

    override fun dispose() {
        game.dispose()
        res.dispose()
        pause.dispose()
        menu.dispose()
        stage.dispose()
        pauseStage.dispose()
        font.dispose()
        roadTexture.dispose()
    }

    override fun pause() {}
    override fun resume() {}
    override fun resize(width: Int, height: Int) {}
    override fun hide() {
    }
}