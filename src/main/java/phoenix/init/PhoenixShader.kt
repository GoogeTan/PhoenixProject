package phoenix.init

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import phoenix.Phoenix
import phoenix.client.shader.NormalShader
import phoenix.client.shader.ShaderProgram
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors

private val SHADERS = mutableListOf<ShaderProgram>()

val DEFAULT_NORMAL_SHADER: NormalShader by lazy {
    val vertex = ResourceLocation(Phoenix.MOD_ID, "shaders/normal_vert.glsl")
    val fragment = ResourceLocation(Phoenix.MOD_ID, "shaders/normal_frag.glsl")
    BufferedReader(InputStreamReader(vertex.inputStream)).use { vertexReader ->
        BufferedReader(InputStreamReader(fragment.inputStream)).use { fragmentReader ->
            val program = NormalShader(
                    vertexReader.lines().collect(Collectors.toList()).joinToString("\n"),
                    fragmentReader.lines().collect(Collectors.toList()).joinToString("\n"))
            SHADERS.add(program)
            program
        }
    }
}

val ResourceLocation.inputStream: InputStream?
    get() = Minecraft.getInstance().resourceManager.getResource(this).inputStream
