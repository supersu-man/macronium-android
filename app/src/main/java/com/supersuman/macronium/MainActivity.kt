package com.supersuman.macronium


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView


class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var connectionStatus : MaterialTextView
    private lateinit var connectButton : ImageButton
    private lateinit var disconnectButton: ImageButton

    private lateinit var socky : Socky

    @SuppressLint("SetTextI18n")
    private val resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            socky.connectSocket(data?.getStringExtra("result").toString())
            if (socky.isConnected()){
                connectionStatus.text = "Connected"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initListeners()
        addCardsToGrid()
    }

    private fun initViews(){
        gridLayout  = findViewById(R.id.gridlayout)
        socky = Socky(gridLayout)
        connectionStatus = findViewById(R.id.connectionStatus)
        connectButton = findViewById(R.id.connectButton)
        disconnectButton = findViewById(R.id.disconnectButton)
    }

    @SuppressLint("SetTextI18n")
    private fun addCardsToGrid(){
        for (i in 13..24){
            val textView = TextView(this)
            textView.text = "f$i"
            val textParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            textView.gravity = Gravity.CENTER
            textView.layoutParams = textParams

            val cardView = MaterialCardView(this)
            val cardParams = GridLayout.LayoutParams(
                GridLayout.spec(
                    GridLayout.UNDEFINED, GridLayout.FILL, 1f
                ),
                GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
            )
            cardParams.height = getDP(130f)
            cardParams.width = 0
            cardParams.setMargins(getDP(5f),getDP(5f),getDP(5f),getDP(5f))
            cardView.layoutParams = cardParams

            cardView.setOnClickListener {
                socky.sendMessage("Macronium-key <f$i>")
            }

            cardView.addView(textView)
            gridLayout.addView(cardView)
        }
    }

    fun getDP(dp : Float): Int {
        val r: Resources = this.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            r.displayMetrics
        ).toInt()
        return px
    }

    private fun initListeners(){

        connectButton.setOnClickListener {
            launchActivityForResult()
        }
        disconnectButton.setOnClickListener {
            socky.disconnectSocket()
            connectionStatus.text = "Disconnected"
        }
    }

    private fun launchActivityForResult(){
        val intent = Intent(this, ScannerActivity::class.java)
        resultLauncher.launch(intent)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        gridLayout.removeAllViews()
        gridLayout.columnCount = getNumberOfColumns()
        addCardsToGrid()

    }

    private fun getNumberOfColumns(): Int {
        val v = this.resources.displayMetrics
        val screenWidth = v.widthPixels.toFloat() / v.densityDpi.toFloat()
        val one = 0.857
        val columns = (screenWidth/one).toInt()
        return columns
    }



}