package al10101.android.sequencerminigame

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

private const val TAG = "InputView"

class InputView(context: Context, attrs: AttributeSet? = null): View(context, attrs) {

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
                leftJoystick.isPressed(event.x, event.y, event.getPointerId(0))
                rightJoystick.isPressed(event.x, event.y, event.getPointerId(0))
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                val activePointerId = event.getPointerId(event.actionIndex)
                val (touchPositionX: Float, touchPositionY: Float) = event.findPointerIndex(activePointerId).let {
                        pointerIndex ->
                    // Get the pointer's current position
                    event.getX(pointerIndex) to event.getY(pointerIndex)
                }
                if (leftJoystick.isPressed) {
                    rightJoystick.isPressed(touchPositionX, touchPositionY, event.getPointerId(event.actionIndex))
                } else {
                    leftJoystick.isPressed(touchPositionX, touchPositionY, event.getPointerId(event.actionIndex))
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (leftJoystick.isPressed) {
                    leftJoystick.setActuator(event)
                    Log.d(TAG, "Moving Left")
                }
                if (rightJoystick.isPressed) {
                    rightJoystick.setActuator(event)
                    Log.d(TAG, "Moving Right")
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