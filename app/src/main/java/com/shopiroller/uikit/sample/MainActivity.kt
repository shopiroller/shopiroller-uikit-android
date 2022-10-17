package com.shopiroller.uikit.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shopiroller.Shopiroller.init
import com.shopiroller.ShopirollerFragment
import com.shopiroller.uikit.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SampleApplication.shopirollerAdapter?.let { init(it, this@MainActivity) }
        startShopirollerFragment()
        setContentView(R.layout.activity_main)
    }

    private fun startShopirollerFragment() {

        val shopirollerFragment = ShopirollerFragment()

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(
            R.id.shopiroller_frame_layout,
            shopirollerFragment
        )

        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}