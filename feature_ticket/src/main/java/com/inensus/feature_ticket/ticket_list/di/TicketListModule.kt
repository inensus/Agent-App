package com.inensus.feature_ticket.ticket_list.di

import com.inensus.feature_ticket.ticket_list.repository.TicketListRepository
import com.inensus.feature_ticket.ticket_list.viewmodel.TicketListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

object TicketListModule {
    fun createTicketListModules(): List<Module> = listOf(
        module {
            viewModel {
                getKoin().getOrCreateScope(TICKET_LIST_SCOPE, named(TICKET_LIST_SCOPE))
                TicketListViewModel(getScope(TICKET_LIST_SCOPE).get(), get())
            }
        },
        createTicketListNetworkModule()
    )

    private fun createTicketListNetworkModule() = module {
        scope(named(TICKET_LIST_SCOPE)) {
            scoped { TicketListRepository(get(), get()) }
        }
    }

    const val TICKET_LIST_SCOPE = "TICKET_LIST_SCOPE"
}