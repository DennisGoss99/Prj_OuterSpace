package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.skybox.*
import cga.exercise.components.geometry.gui.*
import cga.exercise.components.geometry.material.Material
import cga.exercise.components.geometry.material.OverlayMaterial
import cga.exercise.components.geometry.mesh.Renderable
import cga.exercise.components.geometry.mesh.RenderableContainer
import cga.exercise.components.geometry.transformable.Transformable
import cga.exercise.components.light.*
import cga.exercise.components.shader.ShaderProgram
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
    private val guiShader: ShaderProgram = ShaderProgram("assets/shaders/gui_vert.glsl", "assets/shaders/gui_frag.glsl")

    private val renderAlways = listOf(RenderCategory.FirstPerson,RenderCategory.ThirdPerson)
    private val renderFirstPerson = listOf(RenderCategory.FirstPerson)
    private val renderThirdPerson = listOf(RenderCategory.ThirdPerson)

    private val renderables = RenderableContainer( hashMapOf(
        //"ground" to ModelLoader.loadModel("assets/models/ground.obj",0f,0f,0f)!!,
        "earth" to Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!),
        "moon" to Renderable( renderAlways ,ModelLoader.loadModel("assets/models/sphere.obj",0f,0f,0f)!!),
        "spaceShip" to Renderable( renderThirdPerson ,ModelLoader.loadModel("assets/models/spaceShip/spaceShip.obj",0f,toRadians(180f),0f)!!)
    ))

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

    // camera
    var cameraMode = RenderCategory.FirstPerson

    var camera: TronCamera = TronCamera(modelMatrix = Matrix4f())

    private var skyboxRenderer = Skybox(2000.0f, listOf(
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
        "cockpit" to GuiElement("assets/textures/gui/UI.png", renderFirstPerson),
        "outerSpace" to GuiElement("assets/textures/gui/Test.png", renderAlways, Vector2f(0.25f), Vector2f(0f,0.4f)),
        "planets" to planetGui
    ))

    //scene setup
    init {

        println(gui.keys)

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

        renderables["ground"]?.meshes?.forEach { m ->
            m.material = material
        }

        //Material Earth
        var earthMaterial = OverlayMaterial(
                Texture2D("assets/textures/planets/earth_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
                Texture2D("assets/textures/planets/earth_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
                Texture2D("assets/textures/planets/earth_spec.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
                Texture2D("assets/textures/planets/earth_clouds.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
                64f
        )
        renderables["earth"]?.meshes?.forEach { m ->
            m.material = earthMaterial
        }

        //Material Moon
        var moonMaterial = Material(
            Texture2D("assets/textures/planets/moon_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/moon_emit.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            Texture2D("assets/textures/planets/moon_diff.png",true).setTexParams(GL_REPEAT,GL_REPEAT,GL_LINEAR_MIPMAP_LINEAR,GL_LINEAR),
            32f
        )
        renderables["moon"]?.meshes?.forEach { m ->
            m.material = moonMaterial
        }

//        camera.translateGlobal(Vector3f(0f, 2f, 5f))
//        camera.rotateLocal(toRadians(-35f),0f,0f)
//
//        sphereRenderable.scaleLocal(Vector3f(20f))
        renderables["moon"]?.translateLocal(Vector3f(40f,0f,0f))
        renderables["moon"]?.scaleLocal(Vector3f(0.27f))

//        spacecraftRenderable.scaleLocal(Vector3f(0.02f))



    }

    var lastTime = 0.5f

    fun render(dt: Float, t: Float) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)




        pointLightHolder.bind(mainShader,"pointLight")
        spotLightHolder.bind(mainShader,"spotLight", camera.getCalculateViewMatrix())

        mainShader.setUniform("emitColor", Vector3f(0f,0.5f,1f))

        renderables.render(cameraMode, mainShader)

        if(t-lastTime > 0.01f)
            mainShader.setUniform("time", t)

        camera.bind(mainShader)


        skyboxRenderer.render(skyBoxShader)
        camera.bind(skyBoxShader)

        gui.render(cameraMode, guiShader)


        if(t-lastTime > 0.01f)
            lastTime = t

    }

    var movingObject : Transformable = camera

    fun update(dt: Float, t: Float) {
        val rotationMultiplier = 45f
        val translationMultiplier = 10.0f

        if (window.getKeyState(GLFW_KEY_Q)) {
            movingObject.translateLocal(Vector3f(0.0f, translationMultiplier * dt,0.0f ))
        }
        if (window.getKeyState(GLFW_KEY_E)) {
            movingObject.translateLocal(Vector3f(0.0f, -translationMultiplier * dt,0.0f ))
        }


        if (window.getKeyState ( GLFW_KEY_W)) {
            movingObject.translateLocal(Vector3f(0.0f, 0.0f, -translationMultiplier * dt))
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


        if (window.getKeyState ( GLFW_KEY_A)) {
            movingObject.rotateLocal(0.0f, 0.0f, rotationMultiplier* dt)
        }

        if (window.getKeyState ( GLFW_KEY_D)) {
            movingObject.rotateLocal(0.0f, 0.0f, -rotationMultiplier* dt)
        }


    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {

        if(GLFW_KEY_F5 == key && action == 0){

            when(cameraMode){

                RenderCategory.FirstPerson ->{
                    renderables["spaceShip"]!!.modelMatrix = camera.modelMatrix
                    camera.parent = renderables["spaceShip"]
                    camera.modelMatrix = Matrix4f()
                    camera.translateGlobal(Vector3f(0f, 6f, 14f))
                    camera.rotateLocal(-40f,0f,0f)
                    movingObject = renderables["spaceShip"]!!
                    cameraMode = RenderCategory.ThirdPerson
                }
                RenderCategory.ThirdPerson -> {
                    camera.parent = null
                    camera.modelMatrix = renderables["spaceShip"]!!.getWorldModelMatrix()
                    movingObject = camera
                    cameraMode = RenderCategory.FirstPerson
                }
            }

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
                camera.rotateAroundPoint(0f, (oldXpos-xpos).toFloat() * 0.02f,0f, Vector3f(0f,0f,0f))
                camera.translateLocal(Vector3f(0f, 0f, 4f))
            }
        }

        oldXpos = xpos
        oldYpos = ypos
    }


    fun cleanup() {}
}
