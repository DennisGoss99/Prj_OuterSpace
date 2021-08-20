package cga.exercise.components.mapGenerator

import cga.exercise.components.Color
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.atmosphere.Atmosphere
import cga.exercise.components.geometry.atmosphere.AtmosphereMaterial
import cga.exercise.components.geometry.mesh.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.spaceObjects.Moon
import cga.exercise.components.spaceObjects.Planet
import cga.exercise.components.spaceObjects.PlanetRing
import cga.exercise.components.spaceObjects.Sun
import cga.framework.ModelLoader
import org.joml.Vector3f
import kotlin.random.Random

class MapGenerator {



    companion object {
        private val renderAlways = listOf(RenderCategory.FirstPerson, RenderCategory.ThirdPerson)

        fun generateSolarSystem() : SolarSystem{

            return SolarSystem(
                generateSun(),
                generatePlanets(),
                emptyList())

        }


        private fun generateSun() : List<Sun>{

            var amount = 0

            if(Random.nextInt(0,11) == 0)
                amount = 2
            else
                amount = 1



            val suns = mutableListOf<Sun>()

            val size = Random.nextInt(20,30).toFloat()

            val speed = Random.nextFloat() / 180f + 0.001f
            val rotationAngle = Random.nextInt(0,45).toFloat()

            for (i in 1 .. amount){

                val selfRotationAngle = Vector3f(Random.nextInt(-30,30).toFloat(),Random.nextInt(-30,30).toFloat(),Random.nextInt(-30,30).toFloat())

                if(amount == 1)

                    suns.add(Sun(size,0f,0f,0f,selfRotationAngle,MapGeneratorMaterials.sunMaterial, Atmosphere(listOf(RenderCategory.FirstPerson, RenderCategory.ThirdPerson), 1.2f, MapGeneratorMaterials.sunAtmosphereMaterial), Renderable( renderAlways , ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!)))
                else{
                    val atmosphere = Atmosphere(listOf(RenderCategory.FirstPerson, RenderCategory.ThirdPerson), 1.2f, MapGeneratorMaterials.sunAtmosphereMaterial)
                    suns.add(Sun(size, Math.pow(-1.0,i.toDouble()).toFloat() * size * amount,speed, rotationAngle,selfRotationAngle,MapGeneratorMaterials.sunMaterial,atmosphere, Renderable( renderAlways , ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!)))

                }
            }

            return suns
        }

        private fun generatePlanets() : List<Planet>{
            return clusterPlanets(1) + clusterPlanets(3)
        }

        private fun clusterPlanets(index : Int) : List<Planet>{

            val amount = Random.nextInt(1,6)

            val planets = mutableListOf<Planet>()

            for (i in 1 .. amount){

                val size = Random.nextInt(1,14).toFloat() / 2f
                val distance = Random.nextInt(4,95).toFloat() * 10f

                val speed = (Random.nextFloat() / 1000f + 0.0001f) * 0.1f
                val rotationAngle = Random.nextInt(0,45).toFloat()
                val selfRotationAngle = Vector3f(Random.nextInt(-30,30).toFloat(),Random.nextInt(-30,30).toFloat(),Random.nextInt(-30,30).toFloat())

                val materialID = Random.nextInt(0,MapGeneratorMaterials.PlanetMaterials.size)

                var ring : PlanetRing? = null
                var atmosphere : Atmosphere? = null

                if(Random.nextInt(0,2) == 0){
                    val atmosphereSize =  1f + Random.nextInt(1,4) / 10f
                    atmosphere = Atmosphere(renderAlways, atmosphereSize, AtmosphereMaterial(MapGeneratorMaterials.basicAtmosphereTexture, Color(Random.nextInt(0,255),Random.nextInt(0,255),Random.nextInt(0,255),Random.nextInt(0,255))))
                }else
                    if(Random.nextInt(0,11) == 0){
                        ring = PlanetRing(Random.nextInt(1,4) / 2f ,0f,0f,Random.nextInt(1,4) / 2f, Vector3f(0f,0f,0f),Renderable( renderAlways ,ModelLoader.loadModel("assets/models/ring/ring.obj",0f,0f,0f)!!))
                    }

                var moons = generateMoons()

                planets.add(Planet("",size,index * 100f + distance,speed,rotationAngle,selfRotationAngle,MapGeneratorMaterials.PlanetMaterials[materialID],atmosphere,ring,
                    moons,Renderable( renderAlways , ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!)))
            }

            return planets
        }

        private fun generateMoons() : List<Moon>{

            val moons = mutableListOf<Moon>()

                val amount = Random.nextInt(1, 4)

                for (i in 1..amount) {

                    val size = Random.nextInt(1, 50).toFloat() / 100f

                    val distance = Random.nextInt(5, 20).toFloat()

                    val speed = Random.nextFloat() / 250f + 0.0001f
                    val rotationAngle = Random.nextInt(0, 45).toFloat()
                    val selfRotationAngle = Vector3f(Random.nextInt(-30, 30).toFloat(), Random.nextInt(-30, 30).toFloat(), Random.nextInt(-30, 30).toFloat())

                    val moonMaterial = MapGeneratorMaterials.moonMaterial

                    moons.add(Moon(size, distance, speed, rotationAngle, selfRotationAngle, moonMaterial,
                            Renderable(renderAlways, ModelLoader.loadModel("assets/models/sphere.obj", 0f, 0f, 0f)!!)))
                }

            return moons
        }



    }
}