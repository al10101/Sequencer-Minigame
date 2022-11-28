package al10101.android.sequencerminigame.game

import al10101.android.sequencerminigame.R
import al10101.android.sequencerminigame.glutils.Quad
import al10101.android.sequencerminigame.glutils.ShaderProgram
import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

private const val NANOSECONDS = 1_000_000_000f

class GameRenderer(private val context: Context): GLSurfaceView.Renderer {

    private lateinit var baseColor: FloatArray

    // Values go from 0 to 1 <- factors
    private var frequencyFactor = 0.5f
    private var amplitudeFactor = 0.5f

    private var realFrequencyFactor = 0.5f
    private var realAmplitudeFactor = 0.5f

    private var lastCorrectTime: Long? = null

    private lateinit var resolution: FloatArray
    private var globalStartTime: Long = 0

    private lateinit var program: ShaderProgram
    private lateinit var quad: Quad

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        glClearColor(0.915f, 0.9f, 0.930f, 1f) // Subtle purple

        program = ShaderProgram(context,
            R.raw.vertex_shader,
            R.raw.fragment_shader
        )

        // From -1 to 1 in NDC space
        quad = Quad(2f, 2f)

        baseColor = floatArrayOf(0f, 1f, 0f, 1f)

        globalStartTime = System.nanoTime()

    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)
        resolution = floatArrayOf(width.toFloat(), height.toFloat())
    }

    override fun onDrawFrame(p0: GL10?) {

        glClear(GL_COLOR_BUFFER_BIT)

        val currentTime = (System.nanoTime() - globalStartTime) / NANOSECONDS

        val timeSinceCorrect = (System.nanoTime() - (lastCorrectTime ?: System.nanoTime())) / NANOSECONDS

        program.useProgram()
        program.setUniforms(
            currentTime, resolution, baseColor,
            frequencyFactor, amplitudeFactor,
            realFrequencyFactor, realAmplitudeFactor,
            timeSinceCorrect
        )
        quad.bindData(program)
        quad.draw()

    }

    fun changeFrequency(newFreqFactor: Float) {
        frequencyFactor = newFreqFactor
    }

    fun changeAmplitude(newAmplFactor: Float) {
        amplitudeFactor = newAmplFactor
    }

    fun resetFreqAndAmpl(newFreq: Float, newAmpl: Float) {
        realFrequencyFactor = newFreq
        realAmplitudeFactor = newAmpl
    }

    fun isCorrectDuring(newLastCorrectTime: Long?) {
        lastCorrectTime = newLastCorrectTime
    }

}