package com.racer.game.objects

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

abstract class GameObject(texture: Sprite, x: Float, y: Float, width: Float, height: Float) {


    val obj = Sprite(texture)
    val bounds = Rectangle(x, y, width, height)

    init {
        obj.setOrigin(width / 2f, height / 2f)
        obj.setSize(width * 1.1f, height * 1.1f)
        obj.setPosition(x, y)
        bounds.setPosition(x, y)
    }

    fun draw(batch: SpriteBatch) {
        obj.setPosition(bounds.x, bounds.y)
        obj.draw(batch)
    }
}