package cga.exercise.components.mapGenerator

import cga.exercise.components.Color
import cga.exercise.components.geometry.RenderCategory
import cga.exercise.components.geometry.atmosphere.Atmosphere
import cga.exercise.components.geometry.atmosphere.AtmosphereMaterial
import cga.exercise.components.geometry.material.Material
import cga.exercise.components.geometry.material.OverlayMaterial
import cga.exercise.components.texture.Texture2D
import org.lwjgl.opengl.GL11

class MapGeneratorMaterials {

    companion object {

        //Material Sun
        val sunMaterial = Material(
            Texture2D("assets/textures/sun/sun_diff.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/sun/sun_diff.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/sun/sun_diff.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            32f
        )

        val moonMaterial = Material(
            Texture2D("assets/textures/planets/moon_diff.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/moon_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/moon_diff.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            32f
        )

        val earthMaterial = OverlayMaterial(
            Texture2D("assets/textures/planets/earth_diff.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/earth_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/earth_spec.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/earth_clouds.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            64f
        )

        val venusMaterial = OverlayMaterial(
            Texture2D("assets/textures/planets/venus_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/venus_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/venus_storms2.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            64f
        )

        //Material Mars
        val marsMaterial = Material(
            Texture2D("assets/textures/planets/mars_diff.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/mars_diff.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            64f
        )

        //Material Uranus
        val uranusMaterial = Material(
            Texture2D("assets/textures/planets/uranus_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/uranus_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            50f
        )

        //Material Mars
        val saturnMaterial = Material(
            Texture2D("assets/textures/planets/saturn_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/saturn_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            64f
        )

        val jupiterMaterial = Material(
            Texture2D("assets/textures/planets/jupiter_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            Texture2D("assets/textures/planets/jupiter_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
            32f
        )

        val merkurMaterial = Material(
                Texture2D("assets/textures/planets/merkur_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                Texture2D("assets/textures/planets/merkur_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                32f
        )

        val neptunMaterial = Material(
                Texture2D("assets/textures/planets/neptun_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                Texture2D("assets/textures/planets/neptun_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                32f
        )

        val fiktiv1Material = Material(
                Texture2D("assets/textures/planets/fiktiv1_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                Texture2D("assets/textures/planets/fiktiv1_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                32f
        )

        val fiktiv2Material = Material(
                Texture2D("assets/textures/planets/fiktiv2_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                Texture2D("assets/textures/planets/mars_emit.png",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                Texture2D("assets/textures/planets/fiktiv2_diff.jpg",true).setTexParams( GL11.GL_REPEAT, GL11.GL_REPEAT, GL11.GL_LINEAR_MIPMAP_LINEAR, GL11.GL_LINEAR),
                32f
        )


        val PlanetMaterials = listOf(
            earthMaterial,
            marsMaterial,
            venusMaterial,
            uranusMaterial,
            saturnMaterial,
            jupiterMaterial,
            merkurMaterial,
            neptunMaterial,
            fiktiv1Material,
            fiktiv2Material
        )

        val sunAtmosphereMaterial = AtmosphereMaterial(Texture2D("assets/textures/sun/sun_diff.png",true), Color(0.6f,0.4f,0.4f, 0.4f))
        val basicAtmosphereTexture = Texture2D("assets/textures/planets/atmosphere_basic.png",true)




    }





}