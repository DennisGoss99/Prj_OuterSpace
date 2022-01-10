package cga.exercise.components.text

import cga.exercise.components.geometry.material.SimpleMaterial
import cga.exercise.components.texture.Texture2D
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.GL_LINEAR
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import java.io.File


class FontType(val path : String) {

    private val PAD_TOP = 0
    private val PAD_LEFT = 1
    private val PAD_BOTTOM = 2
    private val PAD_RIGHT = 3

    private val DESIRED_PADDING = 8f

    protected val LINE_HEIGHT = 0.03f
    protected val SPACE_ASCII = 32f

    private val aspectRatio = 16f/9f

    var verticalPerPixelSize = 0f
    var horizontalPerPixelSize = 0f

    var face = ""
    var size = 0
    var bold = false
    var italic = false
    var charset= ""
    var unicode = 0
    var stretchH = 0
    var smooth = 0
    var aa = 0
    lateinit var padding : List<Int>
    var paddingWidth = 0
    var paddingHeight = 0
    lateinit var spacing : List<Int>

    var lineHeight = 0
    var base = 0
    var scaleW = 0
    var scaleH = 0
    var pages = 0
    var packed = false

    var id = 0
    var fontPath = ""

    var charCount = 0
    val chars = mutableMapOf<Int, Char>()

    var kerningCount = 0

    val fontImageMaterial : SimpleMaterial


    init {
        val fileText = File(path).readLines()
        fileText.forEach { line ->
            val subLine = getSubLine(line)//line.split(" ")
            when(subLine.first()){
                "info" ->{
                    face = getStringValue(subLine[1], "face")
                    size = getIntValue(subLine[2], "size")
                    bold = getBoolValue(subLine[3], "bold")
                    italic = getBoolValue(subLine[4], "italic")
                    charset = getStringValue(subLine[5], "charset")
                    unicode = getIntValue(subLine[6], "unicode")
                    stretchH = getIntValue(subLine[7], "stretchH")
                    smooth = getIntValue(subLine[8], "smooth")
                    aa = getIntValue(subLine[9], "aa")
                    padding = getIntArrayValue(subLine[10], "padding")
                    paddingWidth = padding[PAD_LEFT] + padding[PAD_RIGHT]
                    paddingHeight = padding[PAD_TOP] + padding[PAD_BOTTOM]
                    spacing = getIntArrayValue(subLine[11], "spacing")
                }
                "common" ->{
                    lineHeight = getIntValue(subLine[1], "lineHeight")
                    val lineHeightPixels = lineHeight - paddingHeight
                    verticalPerPixelSize = LINE_HEIGHT / lineHeightPixels.toFloat()
                    horizontalPerPixelSize = verticalPerPixelSize / aspectRatio

                    base = getIntValue(subLine[2], "base")
                    scaleW = getIntValue(subLine[3], "scaleW")
                    scaleH = getIntValue(subLine[4], "scaleH")
                    pages = getIntValue(subLine[5], "pages")
                    packed = getBoolValue(subLine[6], "packed")
                }
                "page" ->{
                    id = getIntValue(subLine[1], "id")
                    fontPath = getStringValue(subLine[2],"file")
                }
                "chars" ->{
                    charCount = getIntValue(subLine[1], "count")
                }
                "char" ->{
                    val fixedList = subLine.filter { !it.isNullOrBlank() }

                    val id = getIntValue(fixedList[1], "id")
                    val x = getIntValue(fixedList[2], "x").toFloat()
                    val y = getIntValue(fixedList[3], "y").toFloat()
                    val xTex = (x + (padding[PAD_LEFT] - DESIRED_PADDING)) / scaleW
                    val yTex = (y + (padding[PAD_TOP] - DESIRED_PADDING)) / scaleH
                    val properYTex = 1 - yTex
                    val width = getIntValue(fixedList[4], "width").toFloat() - (paddingWidth - (2 * DESIRED_PADDING))
                    val height = getIntValue(fixedList[5], "height").toFloat() - (paddingHeight - (2 * DESIRED_PADDING))
                    val quadWidth = width * horizontalPerPixelSize
                    val quadHeight = height * verticalPerPixelSize
                    val xMaxTexSize = width / scaleW + xTex
                    val yMaxTexSize = height / scaleH + yTex
                    val properYMaxTexSize = 1 - yMaxTexSize
                    val xOff = (getIntValue(fixedList[6], "xoffset").toFloat() + padding[PAD_LEFT] - DESIRED_PADDING) * horizontalPerPixelSize
                    val yOff = (getIntValue(fixedList[7], "yoffset").toFloat() + padding[PAD_TOP] - DESIRED_PADDING) * verticalPerPixelSize
                    val xAdvance = (getIntValue(fixedList[8], "xadvance").toFloat() - paddingWidth) * horizontalPerPixelSize

                    chars[id] = Char(id, xTex, properYTex, xOff, yOff, quadWidth, quadHeight, xMaxTexSize, properYMaxTexSize, xAdvance, getIntValue(fixedList[9], "page"), getIntValue(fixedList[10], "chnl"))
                }
                "kernings" ->{
                    //kerningCount = getIntValue(subLine[1], "count")
                }
                "kerning" ->{}
            }
        }

        fontImageMaterial = SimpleMaterial(Texture2D.invoke(path.substringBeforeLast('/') + "/" + fontPath,false).setTexParams(GL_CLAMP_TO_EDGE,GL_CLAMP_TO_EDGE, GL_LINEAR, GL_LINEAR))
    }

    private fun getSubLine(line: String): List<String> {
        val returnList = mutableListOf<String>()
        var isStringOpen = false
        var string = ""
        line.forEach { c ->
            if (c == '"')
                isStringOpen = !isStringOpen

            if (c == ' ' && !isStringOpen) {
                returnList.add(string)
                string = ""
            }else
                string += c
        }

        returnList.add(string)

        return returnList
    }

    private fun getStringValue(raw : String, name : String) : String{
        if(! raw.startsWith(name))
            throw Exception("Can't parse $name in file: $path")

        return raw.substringAfter('"').trimEnd('"')
    }

    private fun getIntValue(raw : String, name : String) : Int{
        if(! raw.startsWith(name))
            throw Exception("Can't parse $name in file: $path")

        return raw.substringAfter('=').toInt()
    }

    private fun getBoolValue(raw : String, name : String) : Boolean{
        if(! raw.startsWith(name))
            throw Exception("Can't parse $name in file: $path")

        return raw.substringAfter('=').toBoolean()
    }

    private fun getIntArrayValue(raw : String, name: String) : List<Int>{
        if(! raw.startsWith(name))
            throw Exception("Can't parse $name in file: $path")

        return raw.substringAfter('=').split(',').map { it.toInt() }
    }

}