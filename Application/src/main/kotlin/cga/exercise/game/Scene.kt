package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.*
import cga.exercise.components.geometry.skybox.*
import cga.exercise.components.geometry.gui.*
import cga.exercise.components.geometry.material.Material
import cga.exercise.components.geometry.material.OverlayMaterial
import cga.exercise.components.light.*
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
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
    private val guiShader: ShaderProgram = ShaderProgram("assets/shaders/gui_vert.glsl", "assets/shaders/gui_frag.glsl")


    private val groundRenderable : Renderable= ModelLoader.loadModel("assets/models/ground.obj",0f,0f,0f)!!
    private val sphereRenderable : Renderable= ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!

    private val pointLightHolder = PointLightHolder( mutableListOf(
        PointLight(Vector3f(20f,1f,20f),Vector3f(1f,0f,1f)),
        PointLight(Vector3f(-20f,1f,20f),Vector3f(1f,0f,1f)),
        PointLight(Vector3f(-20f,1f,-20f),Vector3f(1f,0f,1f)),
        //PointLight(Vector3f(20f,1f,-20f),Vector3f(1f,0f,1f)),
        PointLight(Vector3f(0f,3f,-0f),Vector3f(1f,1f,1f))
    ))

    private val spotLightHolder = SpotLightHolder( mutableListOf(
        SpotLight(Vector3f(0f,1f,0f),Vector3f(1f,1f,1f),  50f, 70f),
        SpotLight(Vector3f(0f,1f,0f),Vector3f(1f,1f,0.6f),  30f, 90f )
    ))


    var camera: TronCamera = TronCamera(modelMatrix = Matrix4f())

    private var skyboxRenderer = Skybox(500.0f, listOf(
        "assets/textures/skybox/BluePinkNebular_right.png",
        "assets/textures/skybox/BluePinkNebular_left.png",
        "assets/textures/skybox/BluePinkNebular_bottom.png",
        "assets/textures/skybox/BluePinkNebular_top.png",
        "assets/textures/skybox/BluePinkNebular_back.png",
        "assets/textures/skybox/BluePinkNebular_front.png"
    ))

    private val planetGui = GuiElement("assets/textures/gui/Planeten.png", Vector2f(0.25f,0.25f),Vector2f(0.5f))

    private val gui = Gui(listOf(
        GuiElement("assets/textures/gui/UI.png"),
        GuiElement("assets/textures/gui/Test.png", Vector2f(0.25f), Vector2f(0f,0.4f))//,
//        planetGui,
//        GuiElement("assets/textures/gui/Position.png", Vector2f(0.25f,0.25f), translate = Vector2f(1f), parent = planetGui)
        )
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

        //Material Boden
        var material = Material(
            Texture2D("assets/textures/planets/earth_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/ground_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/earth_spec.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            64f,
            Vector2f(1f))

        groundRenderable?.meshes.forEach { m ->
           m.material = material
        }

        //Material Erde
        var earthMaterial = OverlayMaterial(
                Texture2D("assets/textures/planets/earth_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
                Texture2D("assets/textures/planets/earth_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
                Texture2D("assets/textures/planets/earth_spec.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
                Texture2D("assets/textures/planets/earth_clouds.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
                64f
        )
        sphereRenderable?.meshes.forEach { m ->
            m.material = earthMaterial
        }

//        camera.translateGlobal(Vector3f(0f, 2f, 5f))
//        camera.rotateLocal(toRadians(-35f),0f,0f)
//
//        sphereRenderable.scaleLocal(Vector3f(20f))
        sphereRenderable.translateLocal(Vector3f(0f,4f,0f))
//        spacecraftRenderable.scaleLocal(Vector3f(0.02f))



    }

    var lastTime = 0.5f

    fun render(dt: Float, t: Float) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)




        pointLightHolder.bind(mainShader,"pointLight")
        spotLightHolder.bind(mainShader,"spotLight", camera.getCalculateViewMatrix())

        mainShader.setUniform("emitColor", Vector3f(0f,0.5f,1f))

        groundRenderable.render(mainShader)
        sphereRenderable.render(mainShader)

        if(t-lastTime > 0.01f)
            mainShader.setUniform("time", t)

        camera.bind(mainShader)


        skyboxRenderer.render(skyBoxShader)
        camera.bind(skyBoxShader)

        gui.render(guiShader)


        if(t-lastTime > 0.01f)
            lastTime = t

    }

    fun update(dt: Float, t: Float) {
        val rotationMultiplier = 45f
        val translationMultiplier = 10.0f

        if (window.getKeyState(GLFW_KEY_Q)) {
            camera.translateLocal(Vector3f(0.0f, translationMultiplier * dt,0.0f ))
        }
        if (window.getKeyState(GLFW_KEY_E)) {
            camera.translateLocal(Vector3f(0.0f, -translationMultiplier * dt,0.0f ))
        }


        if (window.getKeyState ( GLFW_KEY_W)) {
            camera.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt))
        }

        if (window.getKeyState ( GLFW_KEY_S)) {
            camera.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt))
        }

        if (window.getKeyState ( GLFW_KEY_G)) {
            camera.translateLocal(Vector3f(0.0f, 0.0f, translationMultiplier * dt * 100))
        }

        if (window.getKeyState ( GLFW_KEY_T)) {
            camera.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt * 100))
        }


        if (window.getKeyState ( GLFW_KEY_A)) {
            camera.rotateLocal(0.0f, 0.0f, rotationMultiplier* dt)
        }

        if (window.getKeyState ( GLFW_KEY_D)) {
            camera.rotateLocal(0.0f, 0.0f, -rotationMultiplier* dt)
        }


    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    var oldXpos : Double = 0.0;
    var oldYpos : Double = 0.0;

    fun onMouseMove(xpos: Double, ypos: Double) {

        camera.rotateLocal((oldYpos-ypos).toFloat()/20.0f, (oldXpos-xpos).toFloat()/20.0f, 0f)

//        camera.translateLocal(Vector3f(0f, 0f, -4f))
//        camera.rotateAroundPoint(0f, (oldXpos-xpos).toFloat() * 0.0002f,0f, Vector3f(0f,0f,0f))
//        camera.translateLocal(Vector3f(0f, 0f, 4f))
//
        oldXpos = xpos
        oldYpos = ypos
    }


    fun cleanup() {}
}
