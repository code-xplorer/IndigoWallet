package com.ismail.creatvt.indigowallet.settings.categories

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import com.ismail.creatvt.indigowallet.BaseActivity
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.databinding.ActivityCategoriesBinding
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_EXPENSE
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.activity_categories.toolbar
import kotlinx.android.synthetic.main.activity_settings.*
import kotlin.math.abs

class CategoriesActivity : BaseActivity() {

    private var binding: ActivityCategoriesBinding? = null
    private var expenseFragment: CategoryListFragment? = null
    private var incomeFragment: CategoryListFragment? = null
    private var viewModel: CategoriesViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_IndigoWallet_BlueHeaderActivity)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_categories
        )
        setSupportActionBar(toolbar)
        setTitle(R.string.categories)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this).get(CategoriesViewModel::class.java)
        viewModel?.viewContract = this
        binding?.viewModel = viewModel

        setUpViewPagerPages()

        addFabAnimationOnTabChange()

        setUpTabLayoutWithViewPager()

    }

    private fun setUpViewPagerPages() {
        incomeFragment = CategoryListFragment(TYPE_INCOME)
        expenseFragment = CategoryListFragment(TYPE_EXPENSE)

        category_viewpager.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                return if (position == 0) {
                    incomeFragment!!
                } else {
                    expenseFragment!!
                }
            }
        }
    }

    /**
     * Connects tab layout with viewpager
     */
    private fun setUpTabLayoutWithViewPager() {
        TabLayoutMediator(category_tablayout, category_viewpager) { tab, position ->
            tab.text = if (position == 0) {
                getString(R.string.income)
            } else {
                getString(R.string.expense)
            }
        }.attach()
    }

    /**
     * Animates the scale of the add category fab to scale up and down
     * as the tabs are scrolled to give the smooth fab transition as per
     * material design guidelines
     *
     * https://material.io/components/buttons-floating-action-button#behavior
     **/
    private fun addFabAnimationOnTabChange() {
        binding?.categoryViewpager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                val inverseOffset = 1 - positionOffset
                binding?.addCategoryButton?.scaleX = abs(inverseOffset - positionOffset)
                binding?.addCategoryButton?.scaleY = abs(inverseOffset - positionOffset)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel?.type = if (position == 0) TYPE_INCOME else TYPE_EXPENSE
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.viewContract = null
    }

}

