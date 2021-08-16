package cga.exercise.game

import cga.exercise.components.Color
import cga.exercise.components.camera.Camera
import cga.exercise.components.camera.FirstPersonCamera
import cga.exercise.components.camera.ThirdPersonCamera
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.atmosphere.*
import cga.exercise.components.geometry.skybox.*
import cga.exercise.components.geometry.gui.*
import cga.exercise.components.geometry.mesh.*
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.light.*
import cga.exercise.components.mapGenerator.MapGenerator
import cga.exercise.components.mapGenerator.MapGeneratorMaterials
import cga.exercise.components.mapGenerator.SolarSystem
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.spaceObjects.*
import cga.exercise.components.spaceObjects.spaceship.Spaceship
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math.toRadians
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {

    //Shader
    private val mainShader: ShaderProgram = ShaderProgram("assets/shaders/main_vert.glsl", "assets/shaders/main_frag.glsl")
    private val skyBoxShader: ShaderProgram = ShaderProgram("assets/shaders/skyBox_vert.glsl", "assets/shaders/skyBox_frag.glsl")
    private val atmosphereShader: ShaderProgram = ShaderProgram("assets/shaders/atmosphere_vert.glsl", "assets/shaders/atmosphere_frag.glsl")
    private val particleShader: ShaderProgram = ShaderProgram("assets/shaders/particle_vert.glsl", "assets/shaders/particle_frag.glsl")
    private val guiShader: ShaderProgram = ShaderProgram("assets/shaders/gui_vert.glsl", "assets/shaders/gui_frag.glsl")

    private val renderAlways = listOf(RenderCategory.FirstPerson,RenderCategory.ThirdPerson)
    private val renderFirstPerson = listOf(RenderCategory.FirstPerson)
    private val renderThirdPerson = listOf(RenderCategory.ThirdPerson)

    private val spaceship = Spaceship( renderThirdPerson, Renderable( renderThirdPerson ,ModelLoader.loadModel("assets/models/Spaceship/spaceShip.obj",0f,toRadians(180f),0f)!!))

    private val renderables = RenderableContainer( hashMapOf(
        //"ground" to Renderable(renderAlways, ModelLoader.loadModel("assets/models/ground.obj",0f,0f,0f)!!),
        "spaceShip" to spaceship,
        "spaceShipInside" to Renderable( renderFirstPerson ,ModelLoader.loadModel("assets/models/SpaceshipInside/spaceshipInside.obj",0f,toRadians(-90f),toRadians(0f))!!)
    ))

    //Material Moon


//-------------------------------------------------------------------------------------------------------------------------------------------------------
    private val pointLightHolder = PointLightHolder( mutableListOf(
        PointLight(Vector3f(0f,4f,5f), Color(150 * 8 ,245 * 8,255 * 8).toVector3f(), renderables["spaceShipInside"])
        //PointLight(Vector3f(20f,1f,20f),Vector3f(1f,0f,1f))
    ))

    private val spotLightHolder = SpotLightHolder( mutableListOf(
//        SpotLight(Vector3f(0f,1f,0f),Vector3f(1f,1f,1f),  50f, 70f),
//        SpotLight(Vector3f(0f,1f,0f),Vector3f(1f,1f,0.6f),  30f, 90f )
    ))

    // camera
    var cameraMode = RenderCategory.FirstPerson

    private val firstPersonCamera = FirstPersonCamera()
    private val thirdPersonCamera = ThirdPersonCamera()

    var camera : Camera = firstPersonCamera

    var movingObject : Transformable = camera

    private var skyboxRenderer = Skybox(20000.0f, listOf(
        "assets/textures/skybox/BluePinkNebular_right.png",
        "assets/textures/skybox/BluePinkNebular_left.png",
        "assets/textures/skybox/BluePinkNebular_bottom.png",
        "assets/textures/skybox/BluePinkNebular_top.png",
        "assets/textures/skybox/BluePinkNebular_back.png",
        "assets/textures/skybox/BluePinkNebular_front.png"
    ))

    private val speedDisplay = GuiElement("assets/textures/gui/SpeedSymbols.png" , renderAlways, Vector2f(0.1f,0.1f),Vector2f(-0.85f,0.9f))
    private val speedMarker = SpeedMarker(1,"assets/textures/gui/SpeedMarker.png", renderAlways, Vector2f(1f,1f), parent = speedDisplay)
    private val gui = Gui( hashMapOf(
        "outerSpace" to GuiElement("assets/textures/gui/Logo.png", renderFirstPerson, Vector2f(0.20f), Vector2f(0f,0.4f)),

        "speedDisplay" to speedDisplay,
        "speedMarker" to speedMarker
    ))


    private val earth = Planet(
        "earth",
        1f,149f,0.0001f,0.00f,Vector3f(2f,20f,0f),
        MapGeneratorMaterials.earthMaterial,
        Atmosphere(renderAlways, 1.3f, AtmosphereMaterial(Texture2D("assets/textures/planets/atmosphere_basic.png",true), Color(70,105,208, 50))),
        null,
        listOf(Moon(0.27f,8f,0.001f,0.0001f,Vector3f(45.0f, 0f,0f), MapGeneratorMaterials.moonMaterial, Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))),
        Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))

    private val mars = Planet(
        "mars",
        0.6f,227f,0.0002f,0.1f,Vector3f(0f,40f,0f),
        MapGeneratorMaterials.marsMaterial,
        Atmosphere(renderAlways, 1.3f, AtmosphereMaterial(Texture2D("assets/textures/planets/atmosphere_basic.png",true), Color(208,105,70, 50))),
        null,
        listOf(),
        Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))

    private val uranus = Planet(
        "uranus",
        4.1f, 520f, 0.0002f, 0.5f,Vector3f(60f,0f,0f),
        MapGeneratorMaterials.uranusMaterial,
        null,
        null,
        listOf(),
        Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))

    private val venus = Planet(
        "venus",
        0.95f, 80f, 0.0001f, 0.2f,Vector3f(30f,20f,0f),
        MapGeneratorMaterials.venusMaterial,
        null,
        null,
        listOf(),
        Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))

    private val saturn = Planet(
        "saturn",
        5.5f, 400f, 0.00001f, 0.6f,Vector3f(60f,0f,0f),
        MapGeneratorMaterials.saturnMaterial,
        null,
        PlanetRing(1f,0f,0f,0f, Vector3f(0f,0f,0f),Renderable( renderAlways ,ModelLoader.loadModel("assets/models/ring/ring.obj",0f,0f,0f)!!)),
        listOf(),
        Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))

    private val jupiter = Planet(
        "jupiter",7.0f, 300f, 0.0001f, 0.3f,Vector3f(0f,0f,0f),
        MapGeneratorMaterials.jupiterMaterial,
        null,
        null,
        listOf(),
        Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))

    private val sun = Sun(
        20f,0f,0f,0.00f,Vector3f(20f,40f,0f),
        MapGeneratorMaterials.sunMaterial,
        Atmosphere(listOf(RenderCategory.FirstPerson, RenderCategory.ThirdPerson), 1.2f, MapGeneratorMaterials.sunAtmosphereMaterial),
        Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))

    private var solarSystem = SolarSystem(
        listOf(sun),
        listOf(earth, mars, uranus, venus, saturn, jupiter)
    )

    //scene setup
    init {
        
        //initial opengl state
        glClearColor(0f, 0f, 0f, 1.0f); GLError.checkThrow()

        glEnable(GL_CULL_FACE); GLError.checkThrow()
        glFrontFace(GL_CCW); GLError.checkThrow()
        glCullFace(GL_BACK); GLError.checkThrow()

        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()


        spaceship.modelMatrix = earth.getWorldModelMatrix()

        spaceship.translateLocal(Vector3f(30f,4f, 30f))
        spaceship.rotateLocal(0f,90f, 0f)

        //-- Camera init

            // firstPersonCamera
                firstPersonCamera.parent = null
                firstPersonCamera.modelMatrix = spaceship.getWorldModelMatrix()

                renderables["spaceShipInside"]!!.parent = firstPersonCamera
                renderables["spaceShipInside"]!!.translateLocal(Vector3f(0f,-5f,-12f))
                renderables["spaceShipInside"]!!.rotateLocal(5f,0f,0f)

            // thirdPersonCamera
                spaceship.modelMatrix = thirdPersonCamera.modelMatrix
                thirdPersonCamera.parent = spaceship
                thirdPersonCamera.translateGlobal(Vector3f(0f, 6f, 14f))
                thirdPersonCamera.rotateLocal(-40f,0f,0f)

        //--




        //Material Boden
//        var material = Material(
//            Texture2D("assets/textures/planets/earth_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
//            Texture2D("assets/textures/ground_emit.png",true).setTexParams(GL_REPEAT,
//                GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
//            Texture2D("assets/textures/planets/earth_spec.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
//            64f,
//            Vector2f(1f))

//        renderables["ground"]?.meshes?.forEach { m ->
//            m.material = material
//        }



    }


    var lastTime = 0.5f

    fun render(dt: Float, t: Float) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        //-- main Shader
        pointLightHolder.bind(mainShader,"pointLight")
        spotLightHolder.bind(mainShader,"spotLight", camera.getCalculateViewMatrix())

        mainShader.setUniform("emitColor", Vector3f(0f,0.5f,1f))

        if(t-lastTime > 0.01f)
            mainShader.setUniform("time", t)

        camera.bind(mainShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
        renderables.render(cameraMode, mainShader)

        solarSystem.renderSpaceObjects(mainShader)



        //-- SkyBoxShader

        SkyboxPerspective.bind(skyBoxShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
        skyboxRenderer.render(skyBoxShader)
        //--



        //-- Particle
        spaceship.bindThrusters(particleShader,camera.getCalculateProjectionMatrix(),camera.getCalculateViewMatrix())
        spaceship.renderThrusters(particleShader)
        //--

        //-- AtmosphereShader
        atmospherePerspective.bind(atmosphereShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
        solarSystem.renderAtmosphere(atmosphereShader)
        //--

        //-- GuiShader
        gui.render(cameraMode, guiShader)
        //--

        if(t-lastTime > 0.01f)
            lastTime = t

    }



    fun update(dt: Float, t: Float) {

        when(speedMarker.state){
            1 -> solarSystem.update(dt,t)
            2 -> {
                solarSystem.update(dt,t)
                solarSystem.update(dt,t)
            }
        }


        spaceship.updateThrusters(dt)

        val rotationMultiplier = 30f
        val translationMultiplier = 35.0f

        if (window.getKeyState(GLFW_KEY_Q)) {
            movingObject.rotateLocal(rotationMultiplier * dt, 0.0f, 0.0f)
        }

        if (window.getKeyState(GLFW_KEY_E)) {
            movingObject.rotateLocal(-rotationMultiplier  * dt, 0.0f, 0.0f)
        }


        if (window.getKeyState ( GLFW_KEY_W) && !window.getKeyState ( GLFW_KEY_T)) {
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt))
            spaceship.activateMainThrusters()
        }

        if (window.getKeyState ( GLFW_KEY_S)) {
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt))
        }

        if (window.getKeyState ( GLFW_KEY_G)) {
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt * 10))
        }

        if (window.getKeyState ( GLFW_KEY_T)) {
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 3))
            spaceship.activateMainThrusters()
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 3))
            spaceship.activateMainThrusters()
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 3))
            spaceship.activateMainThrusters()
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 3))
            spaceship.activateMainThrusters()
        }

        if (cameraMode == RenderCategory.FirstPerson){
            if (window.getKeyState ( GLFW_KEY_A))
                movingObject.rotateLocal(0.0f, 0.0f, rotationMultiplier* dt)

            if (window.getKeyState ( GLFW_KEY_D))
                movingObject.rotateLocal(0.0f, 0.0f, -rotationMultiplier* dt)
        }

        if (cameraMode == RenderCategory.ThirdPerson){
            if (window.getKeyState ( GLFW_KEY_A))
 //               movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt))
                movingObject.rotateLocal(0.0f, rotationMultiplier* dt, 0.0f)

            if (window.getKeyState ( GLFW_KEY_D))
 //               movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt))
                movingObject.rotateLocal(0.0f, -rotationMultiplier* dt, 0.0f)
        }


    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {

        if(GLFW_KEY_F5 == key && action == 0)
            when(cameraMode){

                RenderCategory.FirstPerson ->{
                    spaceship.modelMatrix = firstPersonCamera.modelMatrix
                    camera = thirdPersonCamera
                    cameraMode = RenderCategory.ThirdPerson

                    movingObject = spaceship
                }
                RenderCategory.ThirdPerson -> {

                    camera = firstPersonCamera
                    cameraMode = RenderCategory.FirstPerson
                    movingObject = camera
                }
            }

        if(GLFW_KEY_N == key && action == 0){
            solarSystem = MapGenerator.generateSolarSystem()
        }

        if(GLFW_KEY_TAB == key && action == 0){
            speedMarker.addToState()
        }

    }


    var oldXpos : Double = 0.0
    var oldYpos : Double = 0.0

    fun onMouseMove(xpos: Double, ypos: Double) {

        when(cameraMode){

            RenderCategory.FirstPerson ->{
                camera.rotateLocal((oldYpos-ypos).toFloat()/20.0f, (oldXpos-xpos).toFloat()/20.0f, 0f)

            }
            RenderCategory.ThirdPerson -> {
                camera.rotateAroundPoint((oldYpos-ypos).toFloat() * 0.002f , (oldXpos-xpos).toFloat() * 0.002f,0f, Vector3f(0f,0f,0f))
            }
        }

        oldXpos = xpos
        oldYpos = ypos
    }

    fun onMouseScroll(xoffset: Double, yoffset: Double) {
        val yoffset = -yoffset.toFloat()

        if(cameraMode == RenderCategory.FirstPerson || thirdPersonCamera.zoomFactor + yoffset < 20f)
            return

        thirdPersonCamera.zoomFactor += yoffset
        thirdPersonCamera.translateLocal(Vector3f(0f, 0f, yoffset))
    }

    fun cleanup() {
        renderables.cleanup()
        gui.cleanup()

        mainShader.cleanup()
        guiShader.cleanup()
        skyBoxShader.cleanup()


    }


}
