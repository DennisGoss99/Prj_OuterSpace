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


    fun addToState() {
        state = (state + 1) % 3

        translateLocal(translate.negate())

        when(state){
            0 -> translate.x = -0.67f
            1 -> translate.x = 0f
            2 -> translate.x = 0.67f
        }

        translateLocal(translate)
    }


}