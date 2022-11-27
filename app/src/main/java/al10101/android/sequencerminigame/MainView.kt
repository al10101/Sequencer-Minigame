package al10101.android.sequencerminigame

import al10101.android.sequencerminigame.game.GameView
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class MainView(context: Context, attrs: AttributeSet? = null): FrameLayout(context, attrs) {

    private val gameView = GameView(context)
    private val inputView = InputView(context, attrs)

    init {
        val matchParent = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(gameView, matchParent)
        addView(inputView, matchParent)
    }

}