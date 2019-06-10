package com.racer.game.objects

import com.badlogic.gdx.graphics.g2d.Sprite
import com.racer.game.objects.GameObject

class Prop (texture: Sprite, var x: Float, var y: Float, var width: Float, var height: Float,
           val type: String, val boost: Float) : GameObject(texture, x, y, width, height)