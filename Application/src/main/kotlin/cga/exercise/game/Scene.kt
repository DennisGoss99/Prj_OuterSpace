package cga.exercise.game

import cga.exercise.components.Color
import cga.exercise.components.camera.Camera
import cga.exercise.components.camera.FirstPersonCamera
import cga.exercise.components.camera.ThirdPersonCamera
import cga.exercise.components.camera.ZoomCamera
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.atmosphere.*
import cga.exercise.components.geometry.skybox.*
import cga.exercise.components.geometry.gui.*
import cga.exercise.components.geometry.gui.animation.*
import cga.exercise.components.geometry.mesh.*
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.light.*
import cga.exercise.components.mapGenerator.MapGenerator
import cga.exercise.components.mapGenerator.MapGeneratorMaterials
import cga.exercise.components.mapGenerator.SolarSystem
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.spaceObjects.*
import cga.exercise.components.spaceObjects.spaceship.Spaceship
import cga.exercise.components.text.FontType
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math.abs
import org.joml.Math.toRadians
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*

class Scene(private val window: GameWindow) {


    var fonts = hashMapOf("Arial" to FontType("assets/fonts/Arial.fnt"),
                          "Calibri" to FontType("assets/fonts/Calibri.fnt"),
                          "Comic Sans MS" to FontType("assets/fonts/Comic Sans MS.fnt"),
                          "Times New Roman" to FontType("assets/fonts/Times New Roman.fnt"))

//    //Shader
    private val mainShader: ShaderProgram = ShaderProgram("assets/shaders/main_vert.glsl", "assets/shaders/main_frag.glsl")
    private val skyBoxShader: ShaderProgram = ShaderProgram("assets/shaders/skyBox_vert.glsl", "assets/shaders/skyBox_frag.glsl")
    private val atmosphereShader: ShaderProgram = ShaderProgram("assets/shaders/atmosphere_vert.glsl", "assets/shaders/atmosphere_frag.glsl")
    private val particleShader: ShaderProgram = ShaderProgram("assets/shaders/particle_vert.glsl", "assets/shaders/particle_frag.glsl")
    private val guiShader: ShaderProgram = ShaderProgram("assets/shaders/gui_vert.glsl", "assets/shaders/gui_frag.glsl")
    private val fontShader: ShaderProgram = ShaderProgram("assets/shaders/font_vert.glsl", "assets/shaders/font_frag.glsl")

    private var gameState = mutableListOf(RenderCategory.PressToPlay)

    private val loadingGuiElement = GuiElement("assets/textures/gui/Loading.png", 1, listOf(RenderCategory.Loading), Vector2f(1f), Vector2f(0.0f, 0f))
    //private val gui = Gui( hashMapOf( "pressKeyToPlay" to loadingGuiElement))

    private val frameCount = GuiText("",10f ,fonts["Calibri"]!!,30f,false, Vector2f(0.0f, -0.0f), 0f)
    private val test = Text(hashMapOf(fonts["Calibri"]!! to listOf(frameCount,GuiText("",10f ,fonts["Calibri"]!!,30f,false, Vector2f(1.0f, 0f)),
        GuiText("",10f ,fonts["Calibri"]!!,30f,false, Vector2f(0.0f, -0.2f)))))


    private val renderAlways = RenderCategory.values().toList()
    private val renderHelpScreen = listOf(RenderCategory.HelpScreen)
    private val renderMainGame = listOf(RenderCategory.FirstPerson, RenderCategory.ThirdPerson, RenderCategory.Zoom, RenderCategory.HelpScreen)
    private val renderStartUpScreen = listOf(RenderCategory.Loading, RenderCategory.PressToPlay)
    private val renderFirstPerson = listOf(RenderCategory.FirstPerson)
    private val renderThirdPerson = listOf(RenderCategory.ThirdPerson)

    private val spaceship = Spaceship(Renderable( renderThirdPerson ,ModelLoader.loadModel("assets/models/Spaceship/spaceShip.obj",0f,toRadians(180f),0f)!!))

    private val renderables = RenderableContainer( hashMapOf(
        "spaceShip" to spaceship,
        "spaceShipInside" to Renderable( renderFirstPerson ,ModelLoader.loadModel("assets/models/SpaceshipInside/spaceshipInside.obj",0f,toRadians(-90f),toRadians(0f))!!)
    ))


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

    private val firstPersonCamera = FirstPersonCamera()
    private val thirdPersonCamera = ThirdPersonCamera()

    private val zoomCamera = ZoomCamera()

    var camera : Camera = firstPersonCamera

    private var movingObject : Transformable = camera

    private var skyboxRenderer = Skybox(20000.0f, listOf(
        "assets/textures/skybox/BluePinkNebular_right.png",
        "assets/textures/skybox/BluePinkNebular_left.png",
        "assets/textures/skybox/BluePinkNebular_bottom.png",
        "assets/textures/skybox/BluePinkNebular_top.png",
        "assets/textures/skybox/BluePinkNebular_back.png",
        "assets/textures/skybox/BluePinkNebular_front.png"
    ))

    private val animatedGuiElement = LoopAnimatedGuiElement(Animator(0.4f, listOf(Vector2f(0.0f, -0.4f),Vector2f(0.0f, -0.5f))),"assets/textures/gui/PressKeyToPlay.png", 1, listOf(RenderCategory.PressToPlay), Vector2f(0.4f,0.4f))





    private val loadingBarGuiElement = AdvancedAnimatedGuiElement(AdvancedAnimator(listOf(Vector2f(0.0f, 0.0f) to 0.1f, Vector2f(0.8f, 0.0f) to 99f )),"assets/textures/gui/LoadingBar.png", 2, listOf(RenderCategory.Loading), Vector2f(1f), parent = loadingGuiElement)
    private val loadingBarGuiElement2 = AdvancedAnimatedGuiElement(AdvancedAnimator(listOf(Vector2f(0.0f, 0.0f) to 0.1f, Vector2f(0.8f, 0.0f) to 99f )),"assets/textures/gui/LoadingBar.png", 3, listOf(RenderCategory.Loading), Vector2f(1f), parent = loadingGuiElement)
    private val loadingBarGuiElement3 = AdvancedAnimatedGuiElement(AdvancedAnimator(listOf(Vector2f(0.0f, 0.0f) to 0.1f, Vector2f(0.8f, 0.0f) to 99f )),"assets/textures/gui/LoadingBar.png", 4, listOf(RenderCategory.Loading), Vector2f(1f), parent = loadingGuiElement)



    private val animatedHelpScreen = AdvancedAnimatedGuiElement(AdvancedAnimator(listOf(Vector2f( 0.6f, 1.5f) to 1.5f ,Vector2f(0.6f) to 0f)),"assets/textures/gui/HelpScreen.png", 2, renderHelpScreen, Vector2f(0.4f))


    private val speedDisplay = GuiElement("assets/textures/gui/SpeedSymbols.png" , 1, renderMainGame, Vector2f(0.1f,0.1f),Vector2f(-0.85f,0.9f))
    private val speedMarker = SpeedMarker(0,"assets/textures/gui/SpeedMarker.png",0, renderMainGame, Vector2f(1f,1f), parent = speedDisplay)

    private val cursor = GuiElement("assets/textures/gui/mouse-cursor.png" , 2, renderAlways, Vector2f(0.05f,0.05f))

    private val gui = Gui( hashMapOf(
        "startupScreen" to GuiElement("assets/textures/gui/StartupScreen.png", 0, renderStartUpScreen, Vector2f(1f), Vector2f(0f)),

        "pressKeyToPlay" to animatedGuiElement,

        "loading" to loadingGuiElement,
        "loadingBar" to loadingBarGuiElement,
        "loadingBar2" to loadingBarGuiElement2,
        "loadingBar3" to loadingBarGuiElement3,

        "helpScreen" to animatedHelpScreen,



        "outerSpace" to GuiElement("assets/textures/gui/Logo.png", 0, renderFirstPerson, Vector2f(0.20f), Vector2f(0f,0.4f)),

        "speedDisplay" to speedDisplay,
        "speedMarker" to speedMarker,

        "pressKeyToPlay" to loadingGuiElement,

        "cursor" to cursor
    ))


    /*
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

    private var solarSystem = SolarSystem(emptyList(), emptyList(), emptyList())
    */

    //scene setup
    init {
        
        //initial opengl state
        glClearColor(0f, 0f, 0f, 1.0f); GLError.checkThrow()

        glEnable(GL_CULL_FACE); GLError.checkThrow()
        glFrontFace(GL_CCW); GLError.checkThrow()
        glCullFace(GL_BACK); GLError.checkThrow()

        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()






//        spaceship.modelMatrix = earth.getWorldModelMatrix()
//
//        spaceship.translateLocal(Vector3f(30f,4f, 30f))
//        spaceship.rotateLocal(0f,90f, 0f)
//
//        //-- Camera init
//
//            // firstPersonCamera
//                firstPersonCamera.parent = null
//                firstPersonCamera.modelMatrix = spaceship.getWorldModelMatrix()
//
//                renderables["spaceShipInside"]!!.parent = firstPersonCamera
//                renderables["spaceShipInside"]!!.translateLocal(Vector3f(0f,-5f,-12f))
//                renderables["spaceShipInside"]!!.rotateLocal(5f,0f,0f)
//
//            // thirdPersonCamera
//                spaceship.modelMatrix = thirdPersonCamera.modelMatrix
//                thirdPersonCamera.parent = spaceship
//                thirdPersonCamera.translateGlobal(Vector3f(0f, 6f, 14f))
//                thirdPersonCamera.rotateLocal(-40f,0f,0f)
//
//            // zoomCamera
//                zoomCamera.parent = firstPersonCamera
//                zoomCamera.translateLocal(Vector3f(0f,0f, -zoomCamera.zoomFactor))
//
//        //--
//
//        //configure LoadingBar
//        loadingBarGuiElement2.setPosition(Vector2f(0.1f, 0f))
//        loadingBarGuiElement3.setPosition(Vector2f(0.2f, 0f))


    }

    var frameCounter = 0
    var lastT = 0f

    var lastTime = 0.5f

    fun render(dt: Float, t: Float) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        if (t - lastT  > 0.5f){
            lastT = t
            frameCounter *= 2
            frameCount.text = "fps=$frameCounter"
            frameCount.textHasChanged()
            frameCounter = 0
        }
        frameCounter ++
//
//        //-- main Shader
//        pointLightHolder.bind(mainShader,"pointLight")
//        spotLightHolder.bind(mainShader,"spotLight", camera.getCalculateViewMatrix())
//
//        mainShader.setUniform("emitColor", Vector3f(0f,0.5f,1f))
//
//        if(t-lastTime > 0.01f)
//            mainShader.setUniform("time", t)
//
//        camera.bind(mainShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
//        renderables.render(gameState, mainShader)
//
//        solarSystem.renderSpaceObjects(mainShader)
//
//
//
//        //-- SkyBoxShader
//
//        SkyboxPerspective.bind(skyBoxShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
//        skyboxRenderer.render(skyBoxShader)
//        //--
//
//
//        //-- Particle
//        if(gameState.contains( RenderCategory.ThirdPerson)){
//            spaceship.bindThrusters(particleShader,camera.getCalculateProjectionMatrix(),camera.getCalculateViewMatrix())
//            spaceship.renderThrusters(particleShader)
//        }
//        //--
//
//        //-- AtmosphereShader
//        atmospherePerspective.bind(atmosphereShader, camera.getCalculateProjectionMatrix(), camera.getCalculateViewMatrix())
//        solarSystem.renderAtmosphere(atmosphereShader)
//        //--

        //-- GuiShader
        gui.render(gameState, guiShader)
        //--

        //-- FontShader
        test.render(renderAlways, fontShader)
        //--

        if(t-lastTime > 0.01f)
            lastTime = t
    }



    fun update(dt: Float, t: Float) {

//        when(speedMarker.state){
//            1 -> solarSystem.update(dt,t)
//            2 -> {
//                solarSystem.update(dt,t)
//                solarSystem.update(dt,t)
//                solarSystem.update(dt,t)
//            }
//        }
//        if(window.getKeyState(GLFW_KEY_SPACE)){
//            for(i in 0..15)
//                solarSystem.update(dt,t)
//        }
//
//
//        spaceship.updateThrusters(dt,t)
//
//        //Animated GUI
//
//        animatedGuiElement.update(dt,t)
//        animatedHelpScreen.update(dt,t)
//        loadingBarGuiElement.update(dt,t)
//        loadingBarGuiElement2.update(dt,t)
//        loadingBarGuiElement3.update(dt,t)
//
//
//        val rotationMultiplier = 30f
//        val translationMultiplier = 35.0f
//
//        if (window.getKeyState(GLFW_KEY_Q)) {
//            movingObject.rotateLocal(rotationMultiplier * dt, 0.0f, 0.0f)
//        }
//
//        if (window.getKeyState(GLFW_KEY_E)) {
//            movingObject.rotateLocal(-rotationMultiplier  * dt, 0.0f, 0.0f)
//        }
//
//
//        if (window.getKeyState ( GLFW_KEY_W) && !window.getKeyState ( GLFW_KEY_T)) {
//            movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt))
//            spaceship.activateMainThrusters()
//        }
//
//        if (window.getKeyState ( GLFW_KEY_S)) {
//            movingObject.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt))
//        }
//
//        if (window.getKeyState ( GLFW_KEY_G)) {
//            movingObject.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt * 10))
//        }
//
//
//
//        if (window.getKeyState ( GLFW_KEY_T)) {
//            for(i in 0..3){
//                movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 3))
//                spaceship.activateMainThrusters()
//
//                spaceship.activateRightTurnThruster()
//                spaceship.activateLeftTurnThruster()
//
//                movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 1.5f))
//                spaceship.activateRightTurnThruster()
//                spaceship.activateLeftTurnThruster()
//            }
//        }
//
//        if (gameState.contains(RenderCategory.FirstPerson)){
//            if (window.getKeyState ( GLFW_KEY_A))
//                movingObject.rotateLocal(0.0f, 0.0f, rotationMultiplier* dt)
//
//            if (window.getKeyState ( GLFW_KEY_D))
//                movingObject.rotateLocal(0.0f, 0.0f, -rotationMultiplier* dt)
//        }
//
//        if (gameState.contains(RenderCategory.ThirdPerson)){
//            if (window.getKeyState ( GLFW_KEY_A)) {
//                movingObject.rotateLocal(0.0f, rotationMultiplier * dt, 0.0f)
//                spaceship.activateRightTurnThruster()
//            }
//
//            if (window.getKeyState ( GLFW_KEY_D)) {
//                movingObject.rotateLocal(0.0f, -rotationMultiplier * dt, 0.0f)
//                spaceship.activateLeftTurnThruster()
//            }
//        }
    }

    private var lastCameraMode = gameState
    private var lastCamera = camera

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {

//        if(gameState.contains(RenderCategory.PressToPlay)){
//
//            gameState = mutableListOf(RenderCategory.Loading)
//
//            //Load SolarSystem
//            solarSystem = SolarSystem(
//                listOf(sun),
//                listOf(earth, mars, uranus, venus, saturn, jupiter),
//                listOf(AsteroidBelt(60, 8,12), AsteroidBelt(80, 28,34)))
//
//            gameState = mutableListOf(RenderCategory.FirstPerson)
//            return
//        }

//        if(GLFW_KEY_F1 == key && action == 0){
//            if(gameState.contains(RenderCategory.HelpScreen)){
//                gameState.remove(RenderCategory.HelpScreen)
//            }else{
//                animatedHelpScreen.changeCurrentLocationState(0)
//                gameState.add(RenderCategory.HelpScreen)
//            }
//
//        }
//
//        if(GLFW_KEY_F5 == key && action == 0)
//            when{
//                gameState.contains(RenderCategory.FirstPerson) ->{
//                    spaceship.modelMatrix = firstPersonCamera.modelMatrix
//                    camera = thirdPersonCamera
//
//                    gameState.remove(RenderCategory.FirstPerson)
//                    gameState.add(RenderCategory.ThirdPerson)
//
//                    movingObject = spaceship
//                }
//                gameState.contains(RenderCategory.ThirdPerson) -> {
//
//                    camera = firstPersonCamera
//
//                    gameState.remove(RenderCategory.ThirdPerson)
//                    gameState.add(RenderCategory.FirstPerson)
//
//                    movingObject = camera
//                }
//            }
//
//        if(GLFW_KEY_Y == key && action == 1){
//            lastCamera = camera
//            lastCameraMode = gameState
//
//            camera = zoomCamera
//            gameState.add(RenderCategory.Zoom)
//        }
//        if(GLFW_KEY_Y == key && action == 0){
//            gameState.remove(RenderCategory.Zoom)
//            gameState = lastCameraMode
//            camera = lastCamera
//        }
//
//
//        if(GLFW_KEY_N == key && action == 0){
//            solarSystem = MapGenerator.generateSolarSystem()
//        }
//
//        if(GLFW_KEY_TAB == key && action == 0){
//            speedMarker.addToState()
//        }

    }


    var oldXpos : Double = 0.0
    var oldYpos : Double = 0.0

    val mouseSensitivity = 1f
    var mouseXPos = window.windowWidth / 2f
    var mouseYPos = window.windowHeight / 2f

    fun onMouseMove(xpos: Double, ypos: Double) {

        if(mouseXPos + mouseSensitivity * oldXpos-xpos < window.windowWidth)
            mouseXPos = (abs(mouseXPos + mouseSensitivity * oldXpos-xpos)% window.windowWidth).toFloat()
        if(mouseYPos + mouseSensitivity * oldYpos-ypos < window.windowHeight)
            mouseYPos = (abs(mouseYPos + mouseSensitivity * oldYpos-ypos)% window.windowHeight).toFloat()

        // sets cursor position Vector2f(openGlMouseXPos / openGlMouseYPos)
        cursor.setPosition(Vector2f(((mouseXPos / window.windowWidth) * -2) +1,((mouseYPos / window.windowHeight) * 2) -1))

//        if(!gameState.contains(RenderCategory.Zoom))
//            when{
//                gameState.contains(RenderCategory.FirstPerson) ->{
//                    camera.rotateLocal((oldYpos-ypos).toFloat()/20.0f, (oldXpos-xpos).toFloat()/20.0f, 0f)
//
//                }
//                gameState.contains(RenderCategory.ThirdPerson) -> {
//                    camera.rotateAroundPoint((oldYpos-ypos).toFloat() * 0.002f , (oldXpos-xpos).toFloat() * 0.002f,0f, Vector3f(0f,0f,0f))
//                }
//            }
//
        oldXpos = xpos
        oldYpos = ypos
    }

    fun onMouseScroll(xoffset: Double, yoffset: Double) {
        val yoffset = -yoffset.toFloat()

        if(gameState.contains(RenderCategory.Zoom) && zoomCamera.zoomFactor + yoffset * 12 >= 20f ){
            zoomCamera.zoomFactor += yoffset * 12
            zoomCamera.translateLocal(Vector3f(0f, 0f, -yoffset * 12))
        }


        if(gameState.contains(RenderCategory.ThirdPerson) && thirdPersonCamera.zoomFactor + yoffset > 20f) {
            thirdPersonCamera.zoomFactor += yoffset
            thirdPersonCamera.translateLocal(Vector3f(0f, 0f, yoffset))
        }


    }

    fun cleanup() {

        renderables.cleanup()
        gui.cleanup()
        fontShader.cleanup()
        mainShader.cleanup()
        guiShader.cleanup()
        skyBoxShader.cleanup()
    }


}
