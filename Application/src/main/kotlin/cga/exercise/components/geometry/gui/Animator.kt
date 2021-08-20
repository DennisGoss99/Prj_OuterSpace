package cga.exercise.components.geometry.gui

import org.joml.Vector2f

data class Animator(var speed : Float, var positions : List<Vector2f>, var currentLocationState : Int = 0)