package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }

        var isRepeat = false
        binding.songRepeatIv.setOnClickListener {
            if (isRepeat)
                setRepeatStatus(false)
            else
                setRepeatStatus(true)

            isRepeat = !isRepeat
        }

        var isRandom = false
        binding.songRandomIv.setOnClickListener {
            if (isRandom)
                setRandomStatus(false)
            else
                setRandomStatus(true)

            isRandom = !isRandom
        }

        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            binding.songMusicTitleTv.text = intent.getStringExtra("title")
            binding.songSingerNameTv.text = intent.getStringExtra("singer")

        }
    }

    fun setPlayerStatus(isPlaying : Boolean){
        if (isPlaying){
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
        else{
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }
    fun setRepeatStatus(isRepeat : Boolean){
        if(isRepeat){
            binding.songRepeatIv.setColorFilter(Color.BLUE)
        }
        else{
            binding.songRepeatIv.setColorFilter(Color.BLACK)
        }
    }

    fun setRandomStatus(isRepeat : Boolean){
        if(isRepeat){
            binding.songRandomIv.setColorFilter(Color.BLUE)
        }
        else{
            binding.songRandomIv.setColorFilter(Color.BLACK)
        }
    }
}

