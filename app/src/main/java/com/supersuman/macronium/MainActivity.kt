package com.supersuman.macronium


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.iammert.library.AnimatedTabLayout
import com.supersuman.githubapkupdater.Updater
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: AnimatedTabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val updater = Updater(this,"https://github.com/supersu-man/Macronium/releases/latest")
        initViews()
        setupTabLayout()
        setupViewPager()
        thread {
            checkForUpdates(updater)
        }
    }

    private fun checkForUpdates(updater: Updater){
        if (updater.isInternetConnection()){
            updater.init()
            updater.isNewUpdateAvailable {
                if (updater.hasPermissionsGranted()){
                    Snackbar.make(viewPager,"New Update Found",Snackbar.LENGTH_INDEFINITE).setAction("Download"){
                        updater.requestDownload()
                    }.show()
                } else{
                    Snackbar.make(viewPager,"New Update Found",Snackbar.LENGTH_INDEFINITE).setAction("Download"){
                        updater.requestMyPermissions {
                            updater.requestDownload()
                        }
                    }.show()
                }
            }
        }else{
            Snackbar.make(viewPager,"Unable To Check For Updates",Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupTabLayout() {
        tabLayout.setupViewPager(viewPager)
    }

    private fun setupViewPager() {
        viewPager.adapter = PagerAdapter(supportFragmentManager)
    }

    private fun initViews(){
        tabLayout = findViewById(R.id.mainActivityTabLayout)
        viewPager = findViewById(R.id.MainActivityViewPager)
    }

}