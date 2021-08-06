package cga.exercise.components.spaceObjects

import cga.exercise.components.geometry.material.IMaterial
import cga.exercise.components.geometry.mesh.RenderableBase
import cga.exercise.components.geometry.transformable.Transformable
import org.joml.*

class Moon(size: Float,
           distanceToParent : Float,
           speed : Float,
           rotationAngle : Float,
           selfRotation : Vector3f,
           material : IMaterial,
           renderable : RenderableBase
) : SpaceObject(size, distanceToParent, speed, rotationAngle, selfRotation, material, null, renderable)