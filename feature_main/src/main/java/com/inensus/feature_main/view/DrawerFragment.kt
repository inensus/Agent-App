package com.inensus.feature_main.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.Observer
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core.utils.AmountUtils
import com.inensus.core.utils.currency
import com.inensus.core_ui.BaseFragment
import com.inensus.core_ui.extentions.*
import com.inensus.feature_main.R
import com.inensus.feature_main.viewmodel.DrawerViewModel
import com.inensus.shared_navigation.Feature
import com.inensus.shared_navigation.SharedNavigation
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.fragment_drawer.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class DrawerFragment : BaseFragment() {

    private val viewModel: DrawerViewModel by viewModel()
    private val preferences: SharedPreferenceWrapper by inject()
    private val navigation: SharedNavigation by inject()

    var navCallback: ((action: NavigationAction) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_drawer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeUiState()
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, Observer {
            when (it) {
                DrawerUiState.AgentLoading -> handleAgentLoading()
                DrawerUiState.AgentError -> handleAgent()
                is DrawerUiState.AgentLoaded -> handleAgentLoaded(it.email, it.balance)
            }
        })

        viewModel.currentNavigation.observe(viewLifecycleOwner, Observer {
            navCallback?.invoke(it)
        })

        viewModel.navigationUiState.observe(viewLifecycleOwner, Observer {
            setSelectedActionItem(it)
        })
    }

    private fun handleAgent() {
        drawerHeaderSkeleton.gone()
        drawerHeaderError.show()
        drawerHeader.hide()
    }

    private fun handleAgentLoading() {
        drawerHeaderSkeleton.show()
        drawerHeaderError.gone()
        drawerHeader.hide()
    }

    private fun handleAgentLoaded(email: String, balance: BigDecimal) {
        drawerHeaderSkeleton.animateGone()
        drawerHeader.animateShow()
        emailText.text = email
        balanceText.text = getString(R.string.balance, AmountUtils.convertAmountToString(balance), requireContext().currency())
    }

    private fun setupView() {
        itemDashboard.isItemSelected = true

        view?.findViewById<ImageButton>(R.id.retryButton)?.setOnClickListener { viewModel.getMe() }

        itemDashboard.setOnClickListener { viewModel.onActionItemTapped(NavigationAction.OpenDashboard) }
        itemCustomers.setOnClickListener { viewModel.onActionItemTapped(NavigationAction.OpenCustomers) }
        itemPayments.setOnClickListener { viewModel.onActionItemTapped(NavigationAction.OpenPayments) }
        itemAppliances.setOnClickListener { viewModel.onActionItemTapped(NavigationAction.OpenAppliances) }
        itemTickets.setOnClickListener { viewModel.onActionItemTapped(NavigationAction.OpenTickets) }

        signOutText.setOnClickListener {
            signOut()
        }
    }

    fun updateMe() {
        viewModel.getMe()
    }

    private fun setSelectedActionItem(navigationAction: NavigationAction) {
        itemDashboard.isItemSelected = false
        itemCustomers.isItemSelected = false
        itemPayments.isItemSelected = false
        itemAppliances.isItemSelected = false
        itemTickets.isItemSelected = false

        when (navigationAction) {
            NavigationAction.OpenDashboard -> itemDashboard
            NavigationAction.OpenCustomers -> itemCustomers
            NavigationAction.OpenPayments -> itemPayments
            NavigationAction.OpenAppliances -> itemAppliances
            NavigationAction.OpenTickets -> itemTickets
        }.isItemSelected = true
    }

    private fun signOut() {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.warning))
            .setCancelable(false)
            .setMessage(getString(R.string.warning_sign_out))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                preferences.accessToken = ""
                navigation.navigateTo(requireActivity(), Feature.Login)
            }
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .show()
    }

    override fun provideViewModel() = viewModel

    companion object {
        fun newInstance() = DrawerFragment()
    }
}