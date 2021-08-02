package cga.exercise.components.spaceObjects

import cga.exercise.components.geometry.mesh.Mesh
import cga.exercise.components.geometry.mesh.RenderableBase
import cga.exercise.components.geometry.transformable.Transformable
import org.joml.Matrix4f
import org.lwjgl.opengl.ARBCullDistance

abstract class SpaceObject(val distanceToParent: Float,
                           renderable : RenderableBase) : RenderableBase(renderable.meshes, renderable.modelMatrix, renderable.parent), IOrbit {



}