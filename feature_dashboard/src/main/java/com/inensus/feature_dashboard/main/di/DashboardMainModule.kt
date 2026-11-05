package com.inensus.feature_dashboard.main.di

import com.inensus.core_network_auth.AuthQualifiers
import com.inensus.feature_dashboard.graph.di.DashboardGraphModule
import com.inensus.feature_dashboard.main.service.DashboardRepository
import com.inensus.feature_dashboard.main.service.DashboardService
import com.inensus.feature_dashboard.main.viewmodel.DashboardMainViewModel
import com.inensus.feature_dashboard.summary.di.DashboardSummaryModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

object DashboardMainModule {
    fun createDashboardModules() = createDashboardModule() +
            createDashboardNetworkModule() +
            DashboardSummaryModule.createDashboardSummaryModule() +
            DashboardGraphModule.createDashboardGraphModule()

    private fun createDashboardModule() = module {
        viewModel {
            getKoin().getOrCreateScope(DASHBOARD_SCOPE, named(DASHBOARD_SCOPE))
            DashboardMainViewModel(getScope(DASHBOARD_SCOPE).get())
        }

        scope(named(DASHBOARD_SCOPE)) {
            scoped { DashboardRepository(get(), get(), get(), get()) }
        }
    }

    private fun createDashboardNetworkModule() = module {
        single { provideDashboardService(get(qualifier = AuthQualifiers.AUTH_RETROFIT)) }
    }

    private fun provideDashboardService(retrofitClient: Retrofit) = retrofitClient.create(DashboardService::class.java)

    const val DASHBOARD_SCOPE = "DASHBOARD_SCOPE"
}