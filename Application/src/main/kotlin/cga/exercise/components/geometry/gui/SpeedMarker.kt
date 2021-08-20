package cga.exercise.components.geometry.gui

import cga.exercise.components.geometry.RenderCategory
import org.joml.Vector2f

class SpeedMarker(
    var state : Int = 0,
    path: String,
    zAxisPosition : Int,
    shouldRender: List<RenderCategory>,
    scale: Vector2f = Vector2f(1f),
    translate: Vector2f = Vector2f(0f),
    roll: Float = 0f,
    parent: GuiElement? = null
) : GuiElement(path, zAxisPosition, shouldRender, scale, translate, roll, parent) {

    init {
        when(state){
            0 -> setPosition( Vector2f(-0.67f,0f))
            1 -> setPosition( Vector2f(0f,0f))
            2 -> setPosition( Vector2f(0.67f,0f))
        }
    }

    fun addToState() {
        state = (state + 1) % 3

        when(state){
            0 -> setPosition( Vector2f(-0.67f,0f))
            1 -> setPosition( Vector2f(0f,0f))
            2 -> setPosition( Vector2f(0.67f,0f))
        }

    }


}