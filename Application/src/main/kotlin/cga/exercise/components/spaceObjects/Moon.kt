package cga.exercise.components.spaceObjects

import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.mesh.RenderableBase
import cga.exercise.components.geometry.transformable.Transformable
import org.joml.Matrix4f
import org.joml.Vector3f

class Moon(distanceToParent : Float,
           val speed : Float,
           val rotationAngle : Float,
           renderable : RenderableBase
) : SpaceObject(distanceToParent, renderable) {


    init{
        translateLocal(Vector3f(distanceToParent,0f,0f))
    }

    override fun orbit() {
        rotateAroundPoint(0f ,0.01f * speed ,0f,Vector3f(0f,0f,0f))
        rotateAroundPoint(0f ,0f, rotationAngle,Vector3f(0f,0f,0f))
        //rotateLocal( rotationAngle,0f,0f)
    }


}