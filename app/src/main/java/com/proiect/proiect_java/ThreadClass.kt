package com.proiect.proiect_java

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage

class ThreadClass : Thread() {
    @JvmField
    var mAccelVal: Double = 0.0
    private var oldmAccelVal = Double.MAX_VALUE
    @JvmField
    var min: Double = 0.0
    @JvmField
    var max: Double = 0.0
    var max1: Double = Double.NEGATIVE_INFINITY

    // Threshold values for detecting fall
    private val MIN_THRESHOLD_PRE_FALL = 8.00
    private val MAX_THRESHOLD_PRE_FALL = 13.42
    private val MAX_DIFF_THRESHOLD = 12.00
    private val MIN_THRESHOLD_POST_FALL = 9.35
    private val MAX_THRESHOLD_POST_FALL = 10.45

    // Time intervals in milliseconds
    private val FALL_DETECTION_TIME: Long = 2000
    private val STABILIZATION_TIME: Long = 7500
    private val RECOVERY_TIME: Long = 12500
    private val UNCONSCIOUS_TIME: Long = 5000

    enum class State {
        NORMAL,
        FALL,
        IMPACT,
        RECOVERY,
        SEND_POPUP,
        UNCONSCIOUS
    }

    @JvmField
    var state: State = State.NORMAL
    private var currTimeMillis: Long = 0
    private val bot = TelegramBot("<YOUR TOKEN>")
    @JvmField
    var latitude: Double = 0.0
    @JvmField
    var longitude: Double = 0.0

    override fun run() {
        while (true) {
            if (mAccelVal != 0.0) {
                when (state) {
                    State.NORMAL -> if (mAccelVal < MIN_THRESHOLD_PRE_FALL) {
                        if (mAccelVal <= oldmAccelVal) {
                            min = mAccelVal
                            oldmAccelVal = mAccelVal
                        } else {
                            state = State.FALL
                            oldmAccelVal = Double.MAX_VALUE
                            currTimeMillis = System.currentTimeMillis()
                        }
                    }

                    State.FALL -> if (System.currentTimeMillis() <= currTimeMillis + FALL_DETECTION_TIME) {
                        if (max1 < mAccelVal) {
                            max1 = mAccelVal
                        }
                    } else {
                        if (max1 > MAX_THRESHOLD_PRE_FALL) {
                            max = max1
                            state = State.IMPACT
                        } else {
                            state = State.NORMAL // Reset to NORMAL if the fall was not significant
                        }
                        max1 = Double.NEGATIVE_INFINITY
                    }

                    State.IMPACT -> if (max - min <= MAX_DIFF_THRESHOLD) {
                        state = State.RECOVERY
                        currTimeMillis = System.currentTimeMillis()
                    } else {
                        state = State.NORMAL
                    }

                    State.RECOVERY -> if (System.currentTimeMillis() > currTimeMillis + STABILIZATION_TIME) {
                        if (System.currentTimeMillis() < currTimeMillis + RECOVERY_TIME) {
                            if (mAccelVal < MIN_THRESHOLD_POST_FALL || mAccelVal > MAX_THRESHOLD_POST_FALL) {
                                state = State.NORMAL
                            }
                        } else {
                            state = State.SEND_POPUP
                            currTimeMillis = System.currentTimeMillis()
                        }
                    }

                    State.SEND_POPUP -> if (System.currentTimeMillis() > currTimeMillis + UNCONSCIOUS_TIME) {
                        state = State.UNCONSCIOUS
                    }

                    State.UNCONSCIOUS -> {
                        sendAlert()
                        state = State.NORMAL
                    }
                }
            }
        }
    }

    private fun sendAlert() {
        val response = bot.execute(SendMessage(5735047398L, "I've fallen, please help!"))
        bot.execute(
            SendMessage(
                5735047398L,
                "GPS Data: Latitude - $latitude ; Longitude - $longitude"
            )
        )
    }
}
