package cga.exercise.components.camera

import cga.exercise.components.geometry.transformable.Transformable
import org.joml.Matrix4f

class ZoomCamera(
    var zoomFactor : Float = 100.0f,
    FieldofView: Float = 90f,
    AspectRatio: Float = 16f / 9f,
    NearPlane: Float = 0.1f,
    FarPlane: Float = 40000f,
    modelMatrix: Matrix4f = Matrix4f(),
    parent: Transformable? = null
    ) : Camera(FieldofView, AspectRatio, NearPlane, FarPlane, modelMatrix, parent)