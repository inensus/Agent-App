package com.inensus.feature_payment.payment_list.di

import com.inensus.feature_payment.payment_list.service.PaymentListRepository
import com.inensus.feature_payment.payment_list.viewmodel.PaymentListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object PaymentListModule {
    fun createPaymentListModules(): List<Module> = listOf(
        module {
            viewModel {
                getKoin().getOrCreateScope(PAYMENT_LIST_SCOPE, named(PAYMENT_LIST_SCOPE))
                PaymentListViewModel(getScope(PAYMENT_LIST_SCOPE).get(), get())
            }
        },
        createPaymentListNetworkModule()
    )

    private fun createPaymentListNetworkModule() = module {
        scope(named(PAYMENT_LIST_SCOPE)) {
            scoped { PaymentListRepository(get(), get()) }
        }
    }

    const val PAYMENT_LIST_SCOPE = "PAYMENT_LIST_SCOPE"
}