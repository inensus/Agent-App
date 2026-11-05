package com.inensus.feature_ticket.ticket_list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inensus.core.lifecycle.LiveEvent
import com.inensus.core_network.model.Customer
import com.inensus.core_ui.BaseViewModel
import com.inensus.core_ui.extentions.addTo
import com.inensus.feature_ticket.ticket_list.di.TicketListModule
import com.inensus.feature_ticket.ticket_list.model.Ticket
import com.inensus.feature_ticket.ticket_list.repository.TicketListRepository
import com.inensus.feature_ticket.ticket_list.view.LoadingTicketListType
import com.inensus.feature_ticket.ticket_list.view.TicketListUiState
import com.inensus.shared_customer.repository.CustomerRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import org.koin.java.KoinJavaComponent.getKoin

class TicketListViewModel(private val repository: TicketListRepository, private val customerRepository: CustomerRepository) : BaseViewModel() {

    private val _uiState = LiveEvent<TicketListUiState>()
    val uiState: LiveEvent<TicketListUiState> = _uiState

    private val _tickets = MutableLiveData<List<Ticket>>()
    val tickets: LiveData<List<Ticket>> = _tickets

    private val _customer = MutableLiveData(customerRepository.customer)
    var customer: LiveData<Customer?> = _customer

    private val pagination = PublishProcessor.create<Unit>()
    private var loadingTicketListType = LoadingTicketListType.INITIAL

    init {
        observeTicketsPublish()

        getTickets(type = LoadingTicketListType.INITIAL)
    }

    private fun observeTicketsPublish() {
        pagination
            .filter { uiState.value !is TicketListUiState.Loading }
            .doOnNext {
                _uiState.postValue(TicketListUiState.Loading(loadingTicketListType))
            }
            .flatMapSingle {
                repository.getTickets(loadingTicketListType, customerRepository.customer)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _uiState.value = it
            }
            .addTo(compositeDisposable)
    }

    fun getTickets(type: LoadingTicketListType) {
        loadingTicketListType = type

        pagination.onNext(Unit)
    }

    fun onTicketTapped(ticket: Ticket) {
        _uiState.value = TicketListUiState.TicketTapped(ticket.id.toString())
    }

    fun saveTicketsState(tickets: List<Ticket>) {
        _tickets.value = tickets
    }

    override fun onCleared() {
        super.onCleared()
        getKoin().getScope(TicketListModule.TICKET_LIST_SCOPE).close()
    }
}