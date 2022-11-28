package al10101.android.sequencerminigame.glutils

import android.content.Context
import android.opengl.GLES20.*

private const val U_TIME = "u_Time"
private const val U_RESOLUTION = "u_Resolution"
private const val U_BASE_COLOR = "u_BaseColor"
private const val U_FREQUENCY_FACTOR = "u_FrequencyFactor"
private const val U_AMPLITUDE_FACTOR = "u_AmplitudeFactor"
private const val U_REAL_FREQUENCY_FACTOR = "u_RealFrequencyFactor"
private const val U_REAL_AMPLITUDE_FACTOR = "u_RealAmplitudeFactor"

private const val A_POSITION = "a_Position"

class ShaderProgram(
    context: Context,
    vertexShaderResId: Int,
    fragmentShaderResId: Int
) {
    
    private val program by lazy { 
        ShaderUtils.buildProgram(
            context.readTextFileFromResource(vertexShaderResId),
            context.readTextFileFromResource(fragmentShaderResId)
        )
    }

    // Uniforms
    private val uTimeLocation by lazy {
        glGetUniformLocation(program, U_TIME)
    }
    private val uResolutionLocation by lazy {
        glGetUniformLocation(program, U_RESOLUTION)
    }
    private val uBaseColorLocation by lazy {
        glGetUniformLocation(program, U_BASE_COLOR)
    }
    private val uFrequencyFactorLocation by lazy {
        glGetUniformLocation(program, U_FREQUENCY_FACTOR)
    }
    private val uAmplitudeFactorLocation by lazy {
        glGetUniformLocation(program, U_AMPLITUDE_FACTOR)
    }
    private val uRealFrequencyFactorLocation by lazy {
        glGetUniformLocation(program, U_REAL_FREQUENCY_FACTOR)
    }
    private val uRealAmplitudeFactorLocation by lazy {
        glGetUniformLocation(program, U_REAL_AMPLITUDE_FACTOR)
    }

    // Attributes
    val aPositionLocation by lazy {
        glGetAttribLocation(program, A_POSITION)
    }

    fun useProgram() {
        glUseProgram(program)
    }

    fun setUniforms(
        time: Float, resolution: FloatArray, baseColor: FloatArray,
        frequencyFactor: Float, amplitudeFactor: Float,
        realFrequencyFactor: Float, realAmplitudeFactor: Float
    ) {
        glUniform1f(uTimeLocation, time)
        glUniform2fv(uResolutionLocation, 1, resolution, 0)
        glUniform4fv(uBaseColorLocation, 1, baseColor, 0)
        glUniform1f(uFrequencyFactorLocation, frequencyFactor)
        glUniform1f(uAmplitudeFactorLocation, amplitudeFactor)
        glUniform1f(uRealFrequencyFactorLocation, realFrequencyFactor)
        glUniform1f(uRealAmplitudeFactorLocation, realAmplitudeFactor)
    }
    
    
}