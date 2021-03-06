package com.supersuman.macronium

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject


class MouseFragment : Fragment() {

    private lateinit var lockButton : FloatingActionButton
    private lateinit var touchPad : MaterialCardView
    private lateinit var tablayout : TabLayout
    private lateinit var viewPager : ViewPager
    private lateinit var serviceIntent : Intent
    private lateinit var activity: MainActivity



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initViews(){
        lockButton = requireActivity().findViewById(R.id.mouseFragmentLockButton)
        touchPad  = requireActivity().findViewById(R.id.mouseFragmentTouchPad)
        tablayout = requireActivity().findViewById(R.id.mainActivityTabLayout)
        viewPager = requireActivity().findViewById(R.id.MainActivityViewPager)
        activity = requireActivity() as MainActivity
        serviceIntent = Intent(requireActivity(), BackgroundService::class.java).apply {
            this.action = "SEND_MESSAGE"
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners(){

        lockButton.setOnClickListener {
            when(it.tag){
                "unlock" -> {
                    lockIt()
                    lockButton.setImageResource(R.drawable.ic_outline_lock_24)
                    lockButton.tag = "lock"
                }
                "lock" -> {
                    unlockIt()
                    lockButton.setImageResource(R.drawable.ic_outline_lock_open_24)
                    lockButton.tag = "unlock"
                }
            }
        }

        val prev = mutableMapOf("x" to 0f, "y" to 0f)
        var prevDiff = 0f
        var doubleDown = false

        touchPad.setOnTouchListener { _, event ->

            if (lockButton.tag == "lock"){
                touchPad.requestDisallowInterceptTouchEvent(true)
            }

            val duration = event.eventTime - event.downTime

            when (event?.action) {

                MotionEvent.ACTION_DOWN -> {
                    prev["x"] = event.x
                    prev["y"] = event.y
                    startStopGestures("start")
                }
                MotionEvent.ACTION_UP -> {
                    startStopGestures("stop")
                    doubleDown = false
                    if(duration<200){
                        mouseClick("leftclick")
                    }
                }
                MotionEvent.ACTION_MOVE -> {

                    when(event.pointerCount){
                        1 -> {
                            val jsonObject = JSONObject()
                            jsonObject.put("x", event.x- prev["x"]!!)
                            jsonObject.put("y", event.y- prev["y"]!!)
                            if (!doubleDown)
                                updatePointer(jsonObject.toString())
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
                            updateScrollPointer(jsonObject.toString())
                        }
                    }
                }
            }
            return@setOnTouchListener true
        }
    }

    private fun startStopGestures(arg : String){
        serviceIntent.putExtra("key", "mouse-gestures")
        serviceIntent.putExtra("arg", arg)
        requireActivity().startService(serviceIntent)
    }

    private fun updateScrollPointer(arg : String){
        serviceIntent.putExtra("key", "mouse-scroll")
        serviceIntent.putExtra("arg", arg)
        requireActivity().startService(serviceIntent)
    }

    private fun updatePointer(arg : String){
        serviceIntent.putExtra("key", "mouse-move")
        serviceIntent.putExtra("arg", arg)
        requireActivity().startService(serviceIntent)
    }

    private fun mouseClick(arg : String){
        serviceIntent.putExtra("key", "mouse-click")
        serviceIntent.putExtra("arg", arg)
        requireActivity().startService(serviceIntent)
    }

    private fun lockIt(){
        viewPager.adapter = PagerAdapter(requireActivity().supportFragmentManager, listOf(MouseFragment()), listOf("Touch Pad"))
    }

    private fun unlockIt(){
        viewPager.adapter = PagerAdapter(requireActivity().supportFragmentManager, activity.fragments, activity.fragmentNames)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mouse, container, false)
    }

}