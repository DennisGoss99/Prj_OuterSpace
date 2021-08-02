package cga.exercise.components.spaceObjects

import cga.exercise.components.geometry.material.IMaterial
import cga.exercise.components.geometry.mesh.RenderableBase
import org.joml.Vector3f

class Sun(size: Float,
          distanceToParent : Float,
          speed : Float,
          rotationAngle : Float,
          selfRotation : Vector3f,
          material : IMaterial,
          renderable : RenderableBase
) : SpaceObject(size, distanceToParent, speed, rotationAngle, selfRotation, material, renderable)