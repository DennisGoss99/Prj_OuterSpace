package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.PointLightHolder
import cga.exercise.components.light.SpotLight
import cga.exercise.components.light.SpotLightHolder
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
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

    private val StaticShader: ShaderProgram = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")

    private val groundRenderable : Renderable= ModelLoader.loadModel("assets/models/ground.obj",0f,0f,0f)!!
    //private val spacecraftRenderable : Renderable= ModelLoader.loadModel("C:\\Users\\Merdo\\Desktop\\Unbenannt.obj",0f,0f,0f)!!

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

    //scene setup
    init {

        //initial opengl state
        glClearColor(0f, 0f, 0f, 1.0f); GLError.checkThrow()

        glEnable(GL_CULL_FACE); GLError.checkThrow()
        glFrontFace(GL_CCW); GLError.checkThrow()
        glCullFace(GL_BACK); GLError.checkThrow()

        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()



        val diff = Texture2D("assets/textures/ground_diff.png",true)
        val emit = Texture2D("assets/textures/ground_emit.png",true)
        val spec = Texture2D("assets/textures/ground_spec.png",true)


        diff.setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR)
        emit.setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR)
        spec.setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR)

        var material = Material(diff,emit,spec,64f,Vector2f(64f))

        groundRenderable?.meshes.forEach { m ->
           m.material = material
        }


        camera.rotateLocal(toRadians(-35f),0f,0f)
        camera.translateGlobal(Vector3f(0f, 0f, 4f))

        //spacecraftRenderable.scaleLocal(Vector3f(0.02f))

    }

    fun render(dt: Float, t: Float) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        camera.bind(StaticShader)

        pointLightHolder.bind(StaticShader,"pointLight")
        spotLightHolder.bind(StaticShader,"spotLight", camera.getCalculateViewMatrix())

        StaticShader.setUniform("emitColor", Vector3f(0f,1f,0f))

        //spacecraftRenderable.render(StaticShader)
        groundRenderable.render(StaticShader)



    }

    fun update(dt: Float, t: Float) {
        val rotationMultiplier = toRadians(45f)
        val translationMultiplier = 5.0f

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



    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    var oldXpos : Double = 0.0;
    var oldYpos : Double = 0.0;

    fun onMouseMove(xpos: Double, ypos: Double) {

        camera.rotateLocal((oldYpos-ypos).toFloat()/200.0f, 0f, (oldXpos-xpos).toFloat()/200.0f)

//        camera.translateLocal(Vector3f(0f, 0f, -4f))
//        camera.rotateAroundPoint(0f, (oldXpos-xpos).toFloat() * 0.0002f,0f, Vector3f(0f,0f,0f))
//        camera.translateLocal(Vector3f(0f, 0f, 4f))
//
        oldXpos = xpos
        oldYpos = ypos
    }


    fun cleanup() {}
}