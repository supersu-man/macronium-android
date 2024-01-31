package com.supersuman.macronium.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.supersuman.macronium.R
import com.supersuman.macronium.other.Keys
import com.supersuman.macronium.services.socket

class MediaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialCardView>(R.id.previousButton).setOnClickListener {
            socket?.emit("key-press", Keys.AudioPrev)
        }
        view.findViewById<MaterialCardView>(R.id.playButton).setOnClickListener {
            socket?.emit("key-press", Keys.AudioPlay)
        }
        view.findViewById<MaterialCardView>(R.id.nextButton).setOnClickListener {
            socket?.emit("key-press", Keys.AudioNext)
        }
        view.findViewById<MaterialCardView>(R.id.muteButton).setOnClickListener {
            socket?.emit("key-press", Keys.AudioMute)
        }
        view.findViewById<SeekBar>(R.id.seekbar).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                println(progress)
                socket?.emit("set-volume", progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

}