package phoenix.client.shader

import phoenix.utils.matrix.Mat4
import phoenix.utils.vector.Vec2
import phoenix.utils.vector.Vec3

class NormalShader(vertexShader: String, fragmentShader: String) : ShaderProgram(vertexShader, fragmentShader) {
    private var location_modelViewProjectionMatrix = 0
    private var location_textureSampler = 0
    private var location_lightmapSampler = 0
    private var location_lightmapCoords = 0
    private var location_camPos = 0
    private var location_normalMapSampler = 0

    override fun bindAttributes() {
        bindAttribute(0, "position")
        bindAttribute(1, "textureCoords")
        bindAttribute(2, "normal")
        bindAttribute(3, "tangent")
        bindAttribute(4, "bitangent")
    }

    override fun getAllUniformLocations() {
        location_modelViewProjectionMatrix = getUniformLocation("modelViewProjectionMatrix")
        location_textureSampler = getUniformLocation("textureSampler")
        location_lightmapSampler = getUniformLocation("lightmapSampler")
        location_lightmapCoords = getUniformLocation("lightmapCoords")
        location_camPos = getUniformLocation("camPos")
        location_normalMapSampler = getUniformLocation("normalMapSampler")
    }

    fun connectTextureUnits() {
        loadInt(location_textureSampler, 0)
        loadInt(location_lightmapSampler, 1)
        loadInt(location_normalMapSampler, 2)
    }

    fun loadModelViewProjectionMatrix(mat: Mat4) {
        loadMatrix(location_modelViewProjectionMatrix, mat)
    }

    fun loadLightmapCoords(coords: Vec2) {
        loadVec2(location_lightmapCoords, coords)
    }

    fun loadCameraPos(pos: Vec3) {
        loadVec3(location_camPos, pos)
    }
}