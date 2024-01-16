package com.supersuman.macronium.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.supersuman.macronium.MainActivity
import com.supersuman.macronium.R
import com.supersuman.macronium.services.socket
import org.json.JSONObject


class MouseFragment : Fragment() {

    private lateinit var lockButton: FloatingActionButton
    private lateinit var touchPad: MaterialCardView
    private lateinit var tablayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private var unlocked = true


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initListeners()
    }

    private fun initViews(view: View) {
        lockButton = view.findViewById(R.id.mouseFragmentLockButton)
        touchPad = view.findViewById(R.id.mouseFragmentTouchPad)
        tablayout = requireActivity().findViewById(R.id.mainActivityTabLayout)
        viewPager = requireActivity().findViewById(R.id.mainActivityViewPager)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        lockButton.setOnClickListener {
            if (unlocked) {
                unlocked = false
                tablayout.visibility = View.INVISIBLE
                lockButton.setImageResource(R.drawable.ic_outline_lock_24)
                viewPager.isUserInputEnabled = false

            } else {
                viewPager.isUserInputEnabled = true
                tablayout.visibility = View.VISIBLE
                unlocked = true
                lockButton.setImageResource(R.drawable.ic_outline_lock_open_24)
            }
        }

        val prev = mutableMapOf("x" to 0f, "y" to 0f)
        var prevDiff = 0f
        var doubleDown = false

        touchPad.setOnTouchListener { _, event ->

            if (!unlocked) {
                touchPad.requestDisallowInterceptTouchEvent(true)
            }

            val duration = event.eventTime - event.downTime

            when (event?.action) {

                MotionEvent.ACTION_DOWN -> {
                    prev["x"] = event.x
                    prev["y"] = event.y
                    socket?.emit("mouse-gestures", "start")
                }
                MotionEvent.ACTION_UP -> {
                    socket?.emit("mouse-gestures", "stop")
                    doubleDown = false
                    if (duration < 200) {
                        socket?.emit("mouse-click", "leftclick")
                    }
                }
                MotionEvent.ACTION_MOVE -> {

                    when (event.pointerCount) {
                        1 -> {
                            val jsonObject = JSONObject()
                            jsonObject.put("x", event.x - prev["x"]!!)
                            jsonObject.put("y", event.y - prev["y"]!!)

                            if (!doubleDown)
                                socket?.emit("mouse-move", jsonObject.toString())
                        }
                        2 -> {
                            if (!doubleDown) {
                                prev["x"] = event.x
                                prev["y"] = event.y
                                prevDiff = event.y - prev["y"]!!
                            }
                            val currentDiff = event.y - prev["y"]!!
                            val jsonObject = JSONObject().apply {
                                this.put("y", currentDiff - prevDiff)
                            }
                            prevDiff = currentDiff
                            doubleDown = true
                            socket?.emit("mouse-scroll", jsonObject.toString())
                        }
                    }
                }
            }
            return@setOnTouchListener true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mouse, container, false)
    }

}