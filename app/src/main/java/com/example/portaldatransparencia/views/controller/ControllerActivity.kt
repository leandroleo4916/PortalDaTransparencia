package com.example.portaldatransparencia.views.controller

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.example.portaldatransparencia.R
import com.example.portaldatransparencia.databinding.ActivityControllerBinding
import com.example.portaldatransparencia.interfaces.IHideViewController
import com.example.portaldatransparencia.views.view_generics.TabViewAdapterController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.Float.*

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
        val tabs = arrayOf(R.string.camara, R.string.senado, R.string.mais)
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
}

// esconde e mostra tablayout com scroll up ou down
class BottomNavigationBehavior<V : View>(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<V>(context, attrs) {

    private var lastStartedType: Int = 0
    private var offsetAnimator: ValueAnimator? = null
    private var isSnappingEnabled = false

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: V,
        directTargetChild: View, target: View, axes: Int, type: Int): Boolean {

        if (axes != ViewCompat.SCROLL_AXIS_VERTICAL) return false

        lastStartedType = type
        offsetAnimator?.cancel()

        return true
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: V,
        target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        child.translationY = max(0f, min(child.height.toFloat(), child.translationY + dy))
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, child: V,
                                    target: View, type: Int) {

        if (!isSnappingEnabled) return
        if (lastStartedType == ViewCompat.TYPE_TOUCH || type == ViewCompat.TYPE_NON_TOUCH) {

            val currTranslation = child.translationY
            val childHalfHeight = child.height * 0.5f
            // translate down
            if (currTranslation >= childHalfHeight) animateBarVisibility(child, isVisible = false)
            // translate up
            else animateBarVisibility(child, isVisible = true)
        }
    }

    private fun animateBarVisibility(child: View, isVisible: Boolean) {
        if (offsetAnimator == null) {
            offsetAnimator = ValueAnimator().apply { interpolator = DecelerateInterpolator()
                duration = 150L }

            offsetAnimator?.addUpdateListener { child.translationY = it.animatedValue as Float }
        } else { offsetAnimator?.cancel() }

        val targetTranslation = if (isVisible) 0f else child.height.toFloat()
        offsetAnimator?.setFloatValues(child.translationY, targetTranslation)
        offsetAnimator?.start()
    }
}