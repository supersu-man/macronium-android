package com.supersuman.macronium

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import org.json.JSONArray


class HomeFragment : Fragment() {

    private lateinit var gridLayout: GridLayout
    private lateinit var connectButton : MaterialCardView
    private lateinit var disconnectButton : MaterialCardView
    private lateinit var presetsData: PresetsData
    private lateinit var sharedpref : SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var sharedPreferences: SharedPreferences
    //private lateinit var repoButton : MaterialCardView

    val scanQrCode = registerForActivityResult(ScanQRCode(), ::handleResult)

    private fun handleResult(result : QRResult){
        if (result is QRResult.QRSuccess){
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        addCardsToGrid(sharedPreferences,"presets")
        initListeners()
    }

    private fun initViews(){
        presetsData = PresetsData()
        connectButton = requireActivity().findViewById(R.id.connectButton)
        gridLayout  = requireActivity().findViewById(R.id.fragmentHomeGridlayout)
        disconnectButton = requireActivity().findViewById(R.id.disconnectButton)
        sharedPreferences = requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        //repoButton = requireActivity().findViewById(R.id.repoButton)
    }

    private fun initListeners() {
        sharedpref = SharedPreferences.OnSharedPreferenceChangeListener {
                sharedPreferences: SharedPreferences, s: String ->
            if (s=="presets"){
                addCardsToGrid(sharedPreferences,s)
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedpref)
        connectButton.setOnClickListener {
            scanQrCode.launch(null)
        }
        disconnectButton.setOnClickListener {
            try {
                activity?.stopService(Intent(requireActivity(), BackgroundService::class.java))
            } catch (e:Exception){}
        }
        /*
        repoButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/supersu-man/macronium-pc/releases/latest"))
            startActivity(browserIntent)
        }
         */
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        addCardsToGrid(sharedPreferences,"presets")
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedpref)
    }

    @SuppressLint("SetTextI18n")
    private fun addCardsToGrid(sharedPreferences: SharedPreferences, s : String){
        val array = loadOrderedCollection(sharedPreferences,s)
        gridLayout.removeAllViews()
        gridLayout.columnCount = getNumberOfColumns()
        for (i in array){
            val cardView = makeCard(presetsData.getMappedData(i).toString(),i)
            gridLayout.addView(cardView)
        }
    }

    private fun makeCard(displayName: String, key: String): MaterialCardView {
        val textView = TextView(requireContext())
        textView.text = displayName
        textView.gravity = Gravity.CENTER
        textView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val cardView = MaterialCardView(requireContext())
        val cardParams = GridLayout.LayoutParams(
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
        )
        cardView.radius = 50f
        cardParams.height = getDP(130f)
        cardParams.width = 0
        cardParams.setMargins(getDP(5f),getDP(5f),getDP(5f),getDP(5f))
        cardView.layoutParams = cardParams

        cardView.setOnClickListener {
            val intent = Intent(requireActivity(), BackgroundService::class.java)
            intent.action = "SEND_MESSAGE"
            intent.putExtra("arg", key)
            requireActivity().startService(intent)
        }

        cardView.addView(textView)
        return cardView
    }

    private fun getDP(dp : Float): Int {
        val r: Resources = this.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        ).toInt()
        return px
    }



    private fun loadOrderedCollection(sharedPreferences: SharedPreferences, key: String): MutableList<String> {
        val arrayList = mutableListOf<String>()
        val jsonArray = JSONArray(sharedPreferences.getString(key, "[]"))
        for (i in 0 until jsonArray.length()) {
            arrayList.add(jsonArray.get(i) as String)
        }
        return arrayList
    }

    private fun getNumberOfColumns(): Int {
        val v = this.resources.displayMetrics
        val screenWidth = v.widthPixels.toFloat() / v.densityDpi.toFloat()
        val one = 0.857
        val columns = (screenWidth/one).toInt()
        return columns
    }

}