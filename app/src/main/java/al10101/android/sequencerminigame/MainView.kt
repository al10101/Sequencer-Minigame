package al10101.android.sequencerminigame

import al10101.android.sequencerminigame.game.GameView
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.math.abs
import kotlin.random.Random

class MainView(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {

    private val gameView = GameView(context)
    private val inputView = InputView(context, this)

    private var frequencyFactor = 0f
    private var amplitudeFactor = 0f

    private val random = Random(SystemClock.elapsedRealtime())
    private var realFrequencyFactor = 0f
    private var realAmplitudeFactor = 0f

    init {
        val matchParent = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(gameView, matchParent)
        addView(inputView, matchParent)
        // Initialize the game with values
        setValues()
    }

    fun changeFrequency(newFreqFactor: Float) {
        frequencyFactor = newFreqFactor
        gameView.changeFrequency(frequencyFactor)
        isWaveCorrect()
    }

    fun changeAmplitude(newAmplFactor: Float) {
        amplitudeFactor = newAmplFactor
        gameView.changeAmplitude(amplitudeFactor)
        isWaveCorrect()
    }

    private fun isWaveCorrect() {
        val threshold = 0.02f
        if (abs(frequencyFactor - realFrequencyFactor) < threshold &&
            abs(amplitudeFactor - realAmplitudeFactor) < threshold) {
            setValues()
        }
    }

    private fun setValues() {
        realFrequencyFactor = random.nextFloat()
        realAmplitudeFactor = random.nextFloat()
        gameView.resetValues(realFrequencyFactor, realAmplitudeFactor)
    }

}