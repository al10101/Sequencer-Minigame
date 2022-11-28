package al10101.android.sequencerminigame

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import kotlin.math.atan
import kotlin.math.sin
import kotlin.math.sqrt

class Joystick(
    centerPositionX: Float,
    centerPositionY: Float,
    private val outerCircleRadius: Float,
    private val innerCircleRadius: Float,
) {

    var isPressed = false
    private var pointerId: Int? = null

    private val outerCircleCenterPositionX = centerPositionX
    private val outerCircleCenterPositionY = centerPositionY

    private var innerCircleCenterPositionX = centerPositionX
    private var innerCircleCenterPositionY = centerPositionY

    private var actuatorX: Float = 0f
    private var actuatorY: Float = 0f

    private val outerCirclePaint = Paint().apply {
        color = Color.GRAY
        alpha = 100
        style = Paint.Style.FILL_AND_STROKE
    }

    private val innerCirclePaint = Paint().apply {
        color = Color.DKGRAY
        alpha = 150
        style = Paint.Style.FILL_AND_STROKE
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(
            outerCircleCenterPositionX,
            outerCircleCenterPositionY,
            outerCircleRadius,
            outerCirclePaint
        )
        canvas.drawCircle(
            innerCircleCenterPositionX,
            innerCircleCenterPositionY,
            innerCircleRadius,
            innerCirclePaint
        )
    }

    fun update() {
        updateInnerCirclePosition()
    }

    private fun updateInnerCirclePosition() {
        innerCircleCenterPositionX = outerCircleCenterPositionX + actuatorX * outerCircleRadius
        innerCircleCenterPositionY = outerCircleCenterPositionY + actuatorY * outerCircleRadius
    }

    fun isPressed(touchPositionX: Float, touchPositionY: Float, pointerIndex: Int) {
        val x = outerCircleCenterPositionX - touchPositionX
        val y = outerCircleCenterPositionY - touchPositionY
        val joystickCenterToTouchDistance = sqrt(x*x + y*y)
        isPressed = joystickCenterToTouchDistance < outerCircleRadius
        if (isPressed) {
            pointerId = pointerIndex
        }
    }

    fun setActuator(event: MotionEvent) {

        val (touchPositionX: Float, touchPositionY: Float) = event.findPointerIndex(pointerId!!).let {
                pointerIndex ->
            // Get the pointer's current position
            event.getX(pointerIndex) to event.getY(pointerIndex)
        }

        val deltaX = touchPositionX - outerCircleCenterPositionX
        val deltaY = touchPositionY - outerCircleCenterPositionY
        val deltaDistance = sqrt(deltaX*deltaX + deltaY*deltaY)

        if (deltaDistance < outerCircleRadius) {
            actuatorX = deltaX / outerCircleRadius
            actuatorY = deltaY / outerCircleRadius
        } else {
            actuatorX = deltaX / deltaDistance
            actuatorY = deltaY / deltaDistance
        }

    }

    fun angleOfActuator(): Float {

        val norm = sqrt(actuatorX*actuatorX + actuatorY*actuatorY)
        var angle = atan(-norm * actuatorY / actuatorX) // The angle determines the factor

        if (actuatorX <= 0f) {
            // If the joystick is on the left side
            angle += 3.14159f
        } else if (actuatorX > 0f && actuatorY > 0f) {
            // If the joystick is bottom right (actuatorY is set backwards)
            angle += 6.28319f
        }

        // Normalize to set factor
        angle /= 6.28319f

        return angle

    }

    fun resetActuator(pointerIndex: Int?) {
        if (pointerIndex == pointerId || pointerIndex == null) {
            actuatorX = 0f
            actuatorY = 0f
            isPressed = false
            pointerId = null
        }
    }

}