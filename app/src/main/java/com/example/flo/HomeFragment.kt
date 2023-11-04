package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val timer = Timer()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeAlbumImgIv1.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm , AlbumFragment())
                .commitAllowingStateLoss()
        }


        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 좌우로



        val pannelVPAdapter = PannelVPAdapter(this)
        pannelVPAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        pannelVPAdapter.addFragment(PannelFragment(R.drawable.img_jenre_exp_1))
        pannelVPAdapter.addFragment(PannelFragment(R.drawable.img_jenre_exp_2))
        pannelVPAdapter.addFragment(PannelFragment(R.drawable.img_jenre_exp_3))
        binding.homePannelVp.adapter = pannelVPAdapter
        binding.homePannelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 자동 슬라이딩을 위한 타이머 설정
        val handler = Handler(Looper.getMainLooper())
        val delay: Long = 3000 // 3초마다 슬라이드

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    val currentItem = binding.homePannelVp.currentItem
                    val nextItem = if (currentItem < pannelVPAdapter.itemCount - 1) currentItem + 1 else 0
                    binding.homePannelVp.setCurrentItem(nextItem, true)
                }
            }
        }, delay, delay)

        binding.circleIndicator.setViewPager(binding.homePannelVp)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fragment가 파기될 때 타이머 중지
        timer.cancel()
    }

}