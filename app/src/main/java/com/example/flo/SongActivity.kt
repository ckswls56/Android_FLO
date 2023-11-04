package com.example.flo

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var song : Song
    lateinit var timer : Timer
    private var mediaPlayer : MediaPlayer? = null
    private var gson: Gson = Gson()
    var isRepeat = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)

        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songRepeatIv.setOnClickListener {
            isRepeat = !isRepeat
            setRepeatStatus()
        }

        var isRandom = false
        binding.songRandomIv.setOnClickListener {
            if (isRandom)
                setRandomStatus(false)
            else
                setRandomStatus(true)

            isRandom = !isRandom
        }


    }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playtime",60),
                intent.getBooleanExtra("isPlaying",false),
                intent.getStringExtra("music")!!

            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")
        binding.songSingerNameTv.text = intent.getStringExtra("singer")
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second/60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime/60, song.playTime%60)
        binding.songProgressSb.progress = (song.second * 100000 / song.playTime)
        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)

        setPlayerStatus(song.isPlaying)
    }

    fun setPlayerStatus(isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying
        if (isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
//            mediaPlayer?.seekTo((song.second * 100000 / song.playTime))
            mediaPlayer?.start()
        }
        else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }
    fun setRepeatStatus(){
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

    //사용자가 포커스를 잃었을 때
    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/ 1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("title",song.title)
        val songJson = gson.toJson(song)
        editor.putString("songData", songJson)
        editor.putInt("second",song.second)

        editor.apply()

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() //리소스 해제
        mediaPlayer = null // 미디어플레이어 해제
    }
    inner class Timer(private val playTime: Int,var isPlaying: Boolean = true):Thread(){
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run() {
            super.run()
            try {
                while (true){
                    if(second >= playTime){
                        break
                    }
                    if(isPlaying){
                        sleep(50)
                        mills+=50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills/playTime)*100).toInt()
                        }
                        if (mills%1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second/60, second%60)
                            }
                            second++
                        }
                    }

                }
                if(isRepeat) {
                    // isRepeat가 true이면 새로운 Timer 스레드를 생성하여 시작
                    val newTimer = Timer(playTime, isPlaying)
                    mediaPlayer?.seekTo(0)
                    mediaPlayer?.start()
                    newTimer.start()
                }
            }catch (e : InterruptedException){
                Log.d("Song","Thread is dead")
            }

        }
    }
}

