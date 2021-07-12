package com.supersuman.macronium

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import org.json.JSONArray
import javax.sql.StatementEvent


class HomeFragment : Fragment() {

    private lateinit var gridLayout: GridLayout
    private lateinit var connectButton : MaterialCardView
    private lateinit var disconnectButton : MaterialCardView
    private lateinit var connectionStatus : MaterialTextView
    private lateinit var socky : Socky
    private lateinit var presetsData: PresetsData
    private lateinit var sharedpref : SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var sharedPreferences: SharedPreferences
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            socky.connectSocket(data?.getStringExtra("result").toString())
            if (socky.isConnected()){
                connectionStatus.text = "Connected"
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

    private fun initListeners() {
        sharedpref = SharedPreferences.OnSharedPreferenceChangeListener {
                sharedPreferences: SharedPreferences, s: String ->
            if (s=="presets"){
                addCardsToGrid(sharedPreferences,s)
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedpref)
        connectButton.setOnClickListener {
            launchActivityForResult()
        }
        disconnectButton.setOnClickListener {
            socky.disconnectSocket()
            connectionStatus.text = "Disconnected"
        }
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

    private fun makeCard(string1: String, string2: String): MaterialCardView {
        val textView = TextView(requireContext())
        textView.text = string1
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
            socky.sendMessage("Macronium-key <$string2>")
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

    private fun initViews(){
        presetsData = PresetsData()
        connectButton = requireActivity().findViewById(R.id.connectButton)
        gridLayout  = requireActivity().findViewById(R.id.fragmentHomeGridlayout)
        socky = Socky(gridLayout)
        disconnectButton = requireActivity().findViewById(R.id.disconnectButton)
        connectionStatus = requireActivity().findViewById(R.id.connectionStatus)
        sharedPreferences = requireActivity().getSharedPreferences("PinnedPresets", Context.MODE_PRIVATE)
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

    @SuppressLint("SetTextI18n")
    private fun launchActivityForResult(){
        val intent = Intent(requireContext(), ScannerActivity::class.java)
        resultLauncher.launch(intent)
    }
    
}