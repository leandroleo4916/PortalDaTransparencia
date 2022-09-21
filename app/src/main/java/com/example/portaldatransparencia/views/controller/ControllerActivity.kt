package com.example.portaldatransparencia.views.controller

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ActivityControllerBinding
import com.example.portaldatransparencia.interfaces.IHideViewController
import com.example.portaldatransparencia.views.TabViewAdapterController
import com.example.portaldatransparencia.views.TabViewAdapterGeral
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ControllerActivity: AppCompatActivity(), IHideViewController {

    private val binding by lazy { ActivityControllerBinding.inflate(layoutInflater) }
    private lateinit var tabView: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tabView = binding.tabController
        setupViewController()
    }

    private fun setupViewController(){
        val tabs = arrayOf(R.string.deputados, R.string.senadores, R.string.mais)
        val tabLayout = binding.tabController
        val pagerMain = binding.viewPagerController
        val adapter = TabViewAdapterController(this)
        pagerMain.adapter = adapter

        TabLayoutMediator(tabLayout, pagerMain){ tab, position ->
            tab.text = getString(tabs[position]) }.attach()
    }

    private fun animateBarVisibility(isVisible: Boolean) {

        var offsetAnimator: ValueAnimator? = null

        if (offsetAnimator == null) {
            offsetAnimator = ValueAnimator().apply {
                interpolator = DecelerateInterpolator()
                duration = 150L
            }
            offsetAnimator.addUpdateListener {
                tabView.translationY = it.animatedValue as Float
            }
        } else { offsetAnimator.cancel() }

        val targetTranslation = if (isVisible) 0f else tabView.height.toFloat()
        offsetAnimator.setFloatValues(tabView.translationY, targetTranslation)
        offsetAnimator.start()
    }

    override fun hideNavView(value: Boolean) {
        animateBarVisibility(value)
    }

    override fun snackBar(child: View, snackBar: Snackbar.SnackbarLayout) {
        TODO("Not yet implemented")
    }

}