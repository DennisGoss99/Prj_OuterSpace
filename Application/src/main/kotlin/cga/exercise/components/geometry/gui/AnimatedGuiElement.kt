package cga.exercise.components.geometry.gui

import cga.exercise.components.geometry.RenderCategory
import org.joml.Vector2f

class AnimatedGuiElement(
    var animator: Animator,
    path: String,
    zAxisPosition : Int,
    shouldRender: List<RenderCategory>,
    scale: Vector2f = Vector2f(1f),
    roll: Float = 0f,
    parent: GuiElement? = null
) : GuiElement(path, zAxisPosition, shouldRender, scale, animator.positions[animator.currentLocationState], roll, parent) {

    fun update(dt : Float, t: Float){
        val currentState = animator.currentLocationState
        val nextState = (animator.currentLocationState + 1) % animator.positions.size

        val lastPoint = animator.positions[currentState]
        val nextPoint = animator.positions[nextState]

        val direction = Vector2f(lastPoint.x - nextPoint.x,lastPoint.y - nextPoint.y)

        translateLocal(direction.negate().normalize().mul(dt * animator.speed))

        val currentPosition = getPosition()

        var changeState = false

        when{
            direction.x >= 0 && direction.y >= 0 ->
                if( currentPosition.x >= nextPoint.x && currentPosition.y >= nextPoint.y )
                    changeState = true

            direction.x <= 0 && direction.y <= 0 ->
                if( currentPosition.x <= nextPoint.x && currentPosition.y <= nextPoint.y )
                    changeState = true

            direction.x >= 0 && direction.y <= 0 ->
                if( currentPosition.x >= nextPoint.x && currentPosition.y <= nextPoint.y )
                    changeState = true

            direction.x <= 0 && direction.y >= 0 ->
                if( currentPosition.x <= nextPoint.x && currentPosition.y >= nextPoint.y )
                    changeState = true

        }

        if(changeState){
            setPosition(nextPoint)
            animator.currentLocationState = nextState
        }
    }

}