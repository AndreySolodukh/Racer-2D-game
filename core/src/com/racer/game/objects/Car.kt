package com.racer.game.objects

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.g2d.Sprite
import kotlin.math.sign

class Car(private var boost: Float,
          private var maxSpeed: Float,
          texture: Sprite,
          x: Float,
          y: Float,
          private val width: Float,
          height: Float) :
        GameObject(texture, x, y, width, height) {

    private var speed = 0f

    fun controls() {
        val delta = Gdx.graphics.deltaTime
        val left = Gdx.input.isKeyPressed(Input.Keys.A) ||
                setOf(0, 1, 2, 3, 4).any {
                    Gdx.input.isTouched(it) && Gdx.input.getX(it) <= Gdx.graphics.width / 2
                }
        val right = Gdx.input.isKeyPressed(Input.Keys.D) ||
                setOf(0, 1, 2, 3, 4).any {
                    Gdx.input.isTouched(it) && Gdx.input.getX(it) > Gdx.graphics.width / 2
                }
        when {
            // превышение максимальной скорости не допускается
            speed !in (-maxSpeed * delta)..(maxSpeed * delta) ->
                speed = sign(speed) * maxSpeed * delta

            // обработка выхода за края экрана ("удар об стенку")
            bounds.x !in 20f..(980f - width) -> {
                speed = 0f
                obj.rotation = 0f
                if (bounds.x < 20f) bounds.setPosition(20f, bounds.y)
                else
                    bounds.setPosition(980f - width, bounds.y)
            }

            // нажаты A и D - скорость снижается до нуля, машинка выравнивается
            (left && right) -> {
                if (speed in (-boost * delta)..(boost * delta)) speed = 0f
                if (speed < 0) speed += boost * delta
                if (speed > 0) speed -= boost * delta
                if (obj.rotation < 0) obj.rotation += 1f
                if (obj.rotation > 0) obj.rotation -= 1f
            }

            // движение при нажатии
            left && speed > (-maxSpeed * delta) -> {
                if (obj.rotation < 6f) obj.rotation += 2f
                speed -= boost * delta
            }
            right && speed < (maxSpeed * delta) -> {
                if (obj.rotation > -6f) obj.rotation -= 2f
                speed += boost * delta
            }

            // если не нажаты клавиши - снижение скорости
            else -> {
                if (speed in (-boost * delta)..(boost * delta)) speed = 0f
                speed -= sign(speed) * boost * delta
                obj.rotation -= sign(obj.rotation) * 1f
            }
        }
        bounds.setPosition(bounds.x + speed, bounds.y)
    }
}