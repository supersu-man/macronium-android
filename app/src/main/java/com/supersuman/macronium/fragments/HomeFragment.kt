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
import com.google.android.material.card.MaterialCardView
import com.supersuman.macronium.R
import com.supersuman.macronium.appDatabase
import com.supersuman.macronium.other.BackgroundService
import com.supersuman.macronium.other.DatabaseDao
import com.supersuman.macronium.other.Preset
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode


class HomeFragment : Fragment() {

    private lateinit var gridLayout: GridLayout
    private lateinit var connectButton: MaterialCardView
    private lateinit var disconnectButton: MaterialCardView
    private lateinit var databaseDao: DatabaseDao

    private var pinnedPresets: MutableList<Preset> = mutableListOf()

    private val scanQrCode = registerForActivityResult(ScanQRCode(), ::handleResult)

    private fun handleResult(result: QRResult) {
        if (result is QRResult.QRSuccess) {
            val ipAddress = result.content.rawValue
            val intent = Intent(requireActivity(), BackgroundService::class.java).apply {
                action = "CONNECT"
                putExtra("qrstring", ipAddress)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity?.startForegroundService(intent)
            } else {
                activity?.startService(intent)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
    }


    private fun initViews(view: View) {
        databaseDao = appDatabase.databaseDao()
        gridLayout = view.findViewById(R.id.fragmentHomeGridlayout)
        connectButton = requireActivity().findViewById(R.id.connectButton)
        disconnectButton = requireActivity().findViewById(R.id.disconnectButton)
    }

    private fun initListeners() {
        databaseDao.getPinnedPresets().observe(requireActivity()) {
            pinnedPresets.clear()
            pinnedPresets.addAll(it)
            addCardsToGrid()
        }
        connectButton.setOnClickListener {
            scanQrCode.launch(null)
        }
        disconnectButton.setOnClickListener {
            try {
                activity?.stopService(Intent(requireActivity(), BackgroundService::class.java))
            } catch (e: Exception) {
            }
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
        textView.text = item.presetName
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
            val intent = Intent(requireActivity(), BackgroundService::class.java)
            intent.action = "SEND_MESSAGE"
            intent.putExtra("key", "key-press")
            intent.putExtra("arg", item.presetCommand.joinToString("+"))
            requireActivity().startService(intent)
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

}