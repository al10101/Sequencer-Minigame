package al10101.android.sequencerminigame

import al10101.android.sequencerminigame.game.GameView
import android.content.Context
import android.os.SystemClock
import android.os.Vibrator
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.math.abs
import kotlin.random.Random

private const val NANOSECONDS = 1_000_000_000f

class MainView(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {

    private val gameView = GameView(context)
    private val inputView = InputView(context, this)

    private val v: Vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val pattern: LongArray = longArrayOf(0, 100, 0)

    private var frequencyFactor = 0f
    private var amplitudeFactor = 0f

    private var isCorrect = false
    private var timeSinceCorrect = 0f
    private var lastTimeCorrect: Long = 0

    private val random = Random(SystemClock.elapsedRealtime())
    private var realFrequencyFactor = 0f
    private var realAmplitudeFactor = 0f

    init {
        val matchParent = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(gameView, matchParent)
        addView(inputView, matchParent)
        // Initialize the game with values
        resetValues()
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
        val timeUntilCorrect = 2f // Seconds, it matches the fragment shader
        if (abs(frequencyFactor - realFrequencyFactor) < threshold &&
            abs(amplitudeFactor - realAmplitudeFactor) < threshold) {
            if (!isCorrect) {
                isCorrect = true
                lastTimeCorrect = System.nanoTime()
            } else {
                timeSinceCorrect = (System.nanoTime() - lastTimeCorrect) / NANOSECONDS
                gameView.isCorrectSince(lastTimeCorrect)
                v.vibrate(pattern, 0)
                if (timeSinceCorrect > timeUntilCorrect) {
                    resetValues()
                    v.cancel()
                    isCorrect = false
                    gameView.isCorrectSince(null)
                }
            }
        } else {
            v.cancel()
            isCorrect = false
            gameView.isCorrectSince(null)
        }
    }

    private fun resetValues() {
        realFrequencyFactor = random.nextFloat()
        realAmplitudeFactor = random.nextFloat()
        gameView.resetValues(realFrequencyFactor, realAmplitudeFactor)
    }

}