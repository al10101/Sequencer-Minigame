package al10101.android.sequencerminigame.game

import android.content.Context
import android.opengl.GLSurfaceView

class GameView(context: Context): GLSurfaceView(context) {

    private val renderer = GameRenderer(context)

    init {
        setEGLContextClientVersion(2)
        setRenderer(renderer)
    }

    fun changeFrequency(newFreqFactor: Float) {
        queueEvent {
            renderer.changeFrequency(newFreqFactor)
        }
    }

    fun changeAmplitude(newAmplFactor: Float) {
        queueEvent {
            renderer.changeAmplitude(newAmplFactor)
        }
    }

    fun resetValues(newFreq: Float, newAmpl: Float) {
        queueEvent {
            renderer.resetFreqAndAmpl(newFreq, newAmpl)
        }
    }

}