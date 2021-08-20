package cga.exercise.components.spaceObjects

import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.material.Material
import cga.exercise.components.geometry.mesh.Renderable
import cga.exercise.components.geometry.mesh.RenderableBase
import cga.exercise.components.texture.Texture2D
import cga.framework.ModelLoader
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import kotlin.random.Random

class Asteroid (size: Float,
                distanceToParent : Float,
                speed : Float,
                rotationAngle : Float,
                selfRotation : Vector3f,
                renderable : RenderableBase
) : SpaceObject(size, distanceToParent, speed, rotationAngle, selfRotation, null, null, renderable){

    companion object{


        private val asteroidMaterial = Material(
            Texture2D("assets/textures/planets/asteroid_diff.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/asteroid_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/asteroid_diff.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            10f
        )

        fun getRandomAsteroid(distanceNear : Int , distanceFar : Int) : Asteroid {

            val asteroidRenderables = listOf(
                Renderable( RenderCategory.values().toList() ,ModelLoader.loadModel("assets/models/asteroid/asteroid1.obj",0f,0f,0f)!!),
                Renderable( RenderCategory.values().toList() ,ModelLoader.loadModel("assets/models/asteroid/asteroid2.obj",0f,0f,0f)!!),
                Renderable( RenderCategory.values().toList() ,ModelLoader.loadModel("assets/models/asteroid/asteroid3.obj",0f,0f,0f)!!)
            )

            asteroidRenderables.forEach { it.meshes[0]?.material = asteroidMaterial }

            val size = Random.nextInt(1,30).toFloat() / 25f
            val distance = Random.nextInt(distanceNear,distanceFar).toFloat() * 10f

            val asteroidNumber = Random.nextInt(0,asteroidRenderables.size)

            val speed = (Random.nextFloat() / 1000f + 0.0001f) * 0.1f
            val rotationAngle = Random.nextInt(0,45).toFloat()
            val selfRotationAngle = Vector3f(Random.nextInt(-30,30).toFloat(),Random.nextInt(-30,30).toFloat(),Random.nextInt(-30,30).toFloat())


            return Asteroid(size,distance,speed,rotationAngle, selfRotationAngle, Renderable( RenderCategory.values().toList(),asteroidRenderables[asteroidNumber]))
        }


    }


}