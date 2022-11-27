package al10101.android.sequencerminigame.game

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GameRenderer: GLSurfaceView.Renderer {

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {

        glClearColor(0.915f, 0.9f, 0.930f, 1f)

    }

    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

    }

    override fun onDrawFrame(p0: GL10?) {

        glClear(GL_COLOR_BUFFER_BIT)

    }

}