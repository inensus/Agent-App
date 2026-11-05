package com.inensus.feature_appliance.appliance_list.di

import com.inensus.feature_appliance.appliance_list.repository.ApplianceListRepository
import com.inensus.feature_appliance.appliance_list.viewmodel.ApplianceListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ApplianceListModule {
    fun createApplianceListModules(): List<Module> = listOf(
        module {
            viewModel {
                getKoin().getOrCreateScope(APPLIANCE_LIST_SCOPE, named(APPLIANCE_LIST_SCOPE))
                ApplianceListViewModel(getScope(APPLIANCE_LIST_SCOPE).get(), get())
            }
        },
        createApplianceListNetworkModule()
    )

    private fun createApplianceListNetworkModule() = module {
        scope(named(APPLIANCE_LIST_SCOPE)) {
            scoped { ApplianceListRepository(get(), get()) }
        }
    }

    const val APPLIANCE_LIST_SCOPE = "APPLIANCE_LIST_SCOPE"
}