package al10101.android.sequencerminigame.game

import android.content.Context
import android.opengl.GLSurfaceView

class GameView(context: Context): GLSurfaceView(context) {

    private val renderer = GameRenderer()

    init {
        setEGLContextClientVersion(2)
        setRenderer(renderer)
    }

}