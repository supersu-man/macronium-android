package com.supersuman.macronium.fragments

import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.android.material.card.MaterialCardView
import com.supersuman.macronium.R
import com.supersuman.macronium.other.AppDatabase
import com.supersuman.macronium.other.Preset
import com.supersuman.macronium.other.databaseName
import com.supersuman.macronium.services.socket
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlin.concurrent.thread


class HomeFragment : Fragment() {

    private lateinit var gridLayout: GridLayout
    private var pinnedPresets: List<Preset> = listOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridLayout = view.findViewById(R.id.fragmentHomeGridlayout)
        val db = Room.databaseBuilder(view.context.applicationContext, AppDatabase::class.java, databaseName).build()
        val presetDao = db.presetDao()
        thread {
            pinnedPresets = presetDao.getPinnedPresets()
            activity?.runOnUiThread { addCardsToGrid() }
        }
    }

    private fun addCardsToGrid() {
        gridLayout.removeAllViews()
        gridLayout.columnCount = getNumberOfColumns()
        for (item in pinnedPresets) {
            val cardView = makeCard(item)
            gridLayout.addView(cardView)
        }
    }

    private fun makeCard(item: Preset): MaterialCardView {
        val textView = TextView(requireContext())
        textView.text = item.title
        textView.gravity = Gravity.CENTER
        textView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ).also {
            it.setMargins(10)
        }
        val cardView = MaterialCardView(requireContext())
        val cardParams = GridLayout.LayoutParams(
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
        )
        cardView.radius = 50f
        cardParams.height = getDP(100f)
        cardParams.width = 0
        cardParams.setMargins(getDP(5f), getDP(5f), getDP(5f), getDP(5f))
        cardView.layoutParams = cardParams
        cardView.setOnClickListener {
            socket?.emit("key-press", item.command.joinToString("+"))
        }
        cardView.addView(textView)
        return cardView
    }

    private fun getDP(dp: Float): Int {
        val r: Resources = this.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics
        ).toInt()
        return px
    }

    private fun getNumberOfColumns(): Int {
        val v = this.resources.displayMetrics
        val screenWidth = v.widthPixels.toFloat() / v.densityDpi.toFloat()
        val one = 0.857
        val columns = (screenWidth / one).toInt()
        return columns
    }

    override fun onResume() {
        super.onResume()
        val db = Room.databaseBuilder(activity?.applicationContext!!, AppDatabase::class.java, databaseName).build()
        val presetDao = db.presetDao()
        thread {
            pinnedPresets = presetDao.getPinnedPresets()
            activity?.runOnUiThread { addCardsToGrid() }
        }
    }

}