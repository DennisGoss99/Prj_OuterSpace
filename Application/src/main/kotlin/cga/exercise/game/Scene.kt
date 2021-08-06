package cga.exercise.game

import cga.exercise.components.Color
import cga.exercise.components.camera.Camera
import cga.exercise.components.camera.FirstPersonCamera
import cga.exercise.components.camera.ThirdPersonCamera
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.atmosphere.*
import cga.exercise.components.geometry.skybox.*
import cga.exercise.components.geometry.gui.*
import cga.exercise.components.geometry.material.*
import cga.exercise.components.geometry.mesh.*
import cga.exercise.components.geometry.particle.Particle
import cga.exercise.components.geometry.particle.ParticleHolder
import cga.exercise.components.geometry.particle.ParticleSpawner
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.light.*
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.spaceObjects.*
import cga.exercise.components.spaceObjects.spaceship.Spaceship
import cga.exercise.components.spaceObjects.spaceship.Thruster
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math.toRadians
import org.joml.Matrix4f
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
    private val moonMaterial = Material(
        Texture2D("assets/textures/planets/moon_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
        Texture2D("assets/textures/planets/moon_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
        Texture2D("assets/textures/planets/moon_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
        32f
    )

    var earthMaterial = OverlayMaterial(
        Texture2D("assets/textures/planets/earth_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
        Texture2D("assets/textures/planets/earth_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
        Texture2D("assets/textures/planets/earth_spec.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
        Texture2D("assets/textures/planets/earth_clouds.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
        64f
    )

    var venusMaterial = OverlayMaterial(
            Texture2D("assets/textures/planets/venus_diff.jpg",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/venus_diff.jpg",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/venus_storms2.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            64f
    )

    //Material Sun
    private val sunMaterial = Material(
            Texture2D("assets/textures/sun/sun_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/sun/sun_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/sun/sun_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            32f
    )

    //Material Mars
    var marsMaterial = Material(
            Texture2D("assets/textures/planets/mars_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/mars_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            64f
    )

    //Material Uranus
    var uranusMaterial = Material(
            Texture2D("assets/textures/planets/uranus_diff.jpg",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/uranus_diff.jpg",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            50f
    )

    //Material Mars
    var saturnMaterial = Material(
            Texture2D("assets/textures/planets/saturn_diff.jpg",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/saturn_diff.jpg",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            64f
    )

    var jupiterMaterial = Material(
            Texture2D("assets/textures/planets/jupiter_diff.jpg",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/jupiter_diff.jpg",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            32f
    )


    //-------------------------------------------------------------------------------------------------------------------------------------------------------

    private val sizeOfSun = 20f

    private val sun = Sun(sizeOfSun,0f,0f,0.00f,Vector3f(20f,40f,0f), sunMaterial,  null, Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))

    private val planetList = listOf(
        Planet("earth",1f/sizeOfSun,149f,0.0001f,0.00f,Vector3f(2f,20f,0f), earthMaterial, sun,  Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!)),
        Planet("mars",0.6f/sizeOfSun,227f,0.0002f,0.1f,Vector3f(0f,40f,0f), marsMaterial, sun,  Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!)),
        Planet("uranus",4.1f/sizeOfSun, 520f, 0.0002f, 0.5f,Vector3f(60f,0f,0f),uranusMaterial,sun,Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!)),
        Planet("venus",0.95f/sizeOfSun, 80f, 0.0001f, 0.2f,Vector3f(30f,20f,0f),venusMaterial,sun,Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!)),
        Planet("saturn",5.5f/sizeOfSun, 400f, 0.00001f, 0.6f,Vector3f(60f,0f,0f),saturnMaterial,sun,Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!)),
        Planet("jupiter",7.0f/sizeOfSun, 300f, 0.0001f, 0.3f,Vector3f(0f,0f,0f),jupiterMaterial,sun,Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))
    )

    val moonlist = listOf<Moon>(
            Moon(0.27f,45f,10f,0.0001f,Vector3f(45.0f, 0f,0f), moonMaterial, planetList[0], Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!))
    )

    private val atmosphereList = listOf(
        Atmosphere(renderAlways, 1.4f, AtmosphereMaterial(Texture2D("assets/textures/sun/sun_diff.png",true), Color(0.6f,0.4f,0.4f, 0.4f)),sun),
        Atmosphere(renderAlways, 1.3f, AtmosphereMaterial(Texture2D("assets/textures/planets/atmosphere_basic.png",true), Color(0.2f,0.6f,1.0f, 0.9f)),planetList[0]),
        Atmosphere(renderAlways, 1.3f, AtmosphereMaterial(Texture2D("assets/textures/planets/atmosphere_basic.png",true), Color(208,105,70, 50)),planetList[1])
        //        Atmosphere(renderAlways, 1.3f, AtmosphereMaterial(Texture2D("assets/textures/planets/atmosphere_basic.png",true), Color(208,105,70, 50)),renderables["spaceShipInside"]!!)

    )

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

    private val planetGui = GuiElement("assets/textures/gui/Planeten.png" ,emptyList(), Vector2f(0.25f,0.25f),Vector2f(0.5f))

    private val gui = Gui( hashMapOf(
        "marker" to GuiElement("assets/textures/gui/Position.png", emptyList(), Vector2f(0.25f,0.25f), translate = Vector2f(1f), parent = planetGui),
        //"cockpit" to GuiElement("assets/textures/gui/UI.png", renderFirstPerson),
        "outerSpace" to GuiElement("assets/textures/gui/Test.png", renderFirstPerson, Vector2f(0.20f), Vector2f(0f,0.4f)),
        "planets" to planetGui
    ))



    //scene setup
    init {
        
        //initial opengl state
        glClearColor(0f, 0f, 0f, 1.0f); GLError.checkThrow()

        glEnable(GL_CULL_FACE); GLError.checkThrow()
        glFrontFace(GL_CCW); GLError.checkThrow()
        glCullFace(GL_BACK); GLError.checkThrow()

        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        //-- Camera init

            // firstPersonCamera
                firstPersonCamera.parent = null
                firstPersonCamera.modelMatrix = spaceship!!.getWorldModelMatrix()

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
        //render spaceObjects
        sun.render(mainShader)
        planetList.forEach { it.render(mainShader) }
        moonlist.forEach { it.render(mainShader) }
        //--




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
        atmosphereList.forEach { it.render(atmosphereShader) }
        //--

        //-- GuiShader
        gui.render(cameraMode, guiShader)
        //--

        if(t-lastTime > 0.01f)
            lastTime = t

    }



    fun update(dt: Float, t: Float) {

        //renderables["moon"]?.rotateAroundPoint( 0f,0f,1f ,Vector3f(0f,0f,0f))
        planetList.forEach { it.orbit() }

        spaceship.updateThrusters(dt)

        val rotationMultiplier = 30f
        val translationMultiplier = 10.0f

        if (window.getKeyState(GLFW_KEY_Q)) {
            movingObject.rotateLocal(rotationMultiplier * dt, 0.0f, 0.0f)
        }

        if (window.getKeyState(GLFW_KEY_E)) {
            movingObject.rotateLocal(-rotationMultiplier  * dt, 0.0f, 0.0f)
        }


        if (window.getKeyState ( GLFW_KEY_W)) {
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt))
            spaceship.activateMainThrusters()
        }

        if (window.getKeyState ( GLFW_KEY_S)) {
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt))
        }

        if (window.getKeyState ( GLFW_KEY_G)) {
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt * 100))
        }

        if (window.getKeyState ( GLFW_KEY_T)) {
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 100))
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

        if(GLFW_KEY_L == key && action == 0){
            println(camera.getPosition())
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
                camera.translateLocal(Vector3f(0f, 0f, -4f))
                camera.rotateAroundPoint((oldYpos-ypos).toFloat() * 0.002f , (oldXpos-xpos).toFloat() * 0.002f,0f, Vector3f(0f,0f,0f))
                camera.translateLocal(Vector3f(0f, 0f, 4f))


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
