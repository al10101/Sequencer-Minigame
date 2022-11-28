package al10101.android.sequencerminigame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.Log
import android.view.MotionEvent
import android.view.View

private const val TAG = "InputView"

@SuppressLint("ViewConstructor")
class InputView(context: Context, private val mainView: MainView): View(context) {

    private val leftJoystick: Joystick
    private val rightJoystick: Joystick

    init {
        val leftPositionX = 400f
        val leftPositionY = 700f
        val rightPositionX = 2160f - leftPositionX // width of the screen
        val rightPositionY = 700f
        val outerRadius = 150f
        val innerRadius = 100f
        leftJoystick = Joystick(leftPositionX, leftPositionY, outerRadius, innerRadius)
        rightJoystick = Joystick(rightPositionX, rightPositionY, outerRadius, innerRadius)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        leftJoystick.draw(canvas)
        rightJoystick.draw(canvas)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN -> {
                // The fist touch, check both joysticks
                leftJoystick.isPressed(event.x, event.y, event.getPointerId(0))
                rightJoystick.isPressed(event.x, event.y, event.getPointerId(0))
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                // The second touch, fetch the position of this second event
                val activePointerId = event.getPointerId(event.actionIndex)
                val (touchPositionX: Float, touchPositionY: Float) = event.findPointerIndex(activePointerId).let {
                        pointerIndex ->
                    // Get the pointer's current position
                    event.getX(pointerIndex) to event.getY(pointerIndex)
                }
                // This ensures that at least 1 joystick is in action
                when {
                    leftJoystick.isPressed -> {
                        // If L was pressed in a previous event, press R
                        rightJoystick.isPressed(touchPositionX, touchPositionY, event.getPointerId(event.actionIndex))
                    }
                    rightJoystick.isPressed -> {
                        // If R was pressed in a previous event, press L
                        leftJoystick.isPressed(touchPositionX, touchPositionY, event.getPointerId(event.actionIndex))
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (leftJoystick.isPressed) {
                    leftJoystick.setActuator(event)
                    val freq = leftJoystick.angleOfActuator()
                    Log.d(TAG, "Moving Left: frequency= $freq")
                    mainView.changeFrequency(freq)
                }
                if (rightJoystick.isPressed) {
                    rightJoystick.setActuator(event)
                    val ampl = rightJoystick.angleOfActuator()
                    Log.d(TAG, "Moving Right: amplitude= $ampl")
                    mainView.changeAmplitude(ampl)
                }
                update()
            }

            MotionEvent.ACTION_POINTER_UP -> {
                leftJoystick.resetActuator(event.getPointerId(event.actionIndex))
                rightJoystick.resetActuator(event.getPointerId(event.actionIndex))
                update()
            }

            MotionEvent.ACTION_UP -> {
                leftJoystick.resetActuator(null)
                rightJoystick.resetActuator(null)
                mainView.changeFrequency(0.5f)
                mainView.changeAmplitude(0.5f)
                update()
            }

        }

        return true
    }

    private fun update() {
        leftJoystick.update()
        rightJoystick.update()
        invalidate()
    }

}