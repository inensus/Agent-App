package com.inensus.feature_payment.payment_list.service

import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_network.model.Customer
import com.inensus.feature_payment.payment_form.service.PaymentService
import com.inensus.feature_payment.payment_list.view.LoadingPaymentListType
import com.inensus.feature_payment.payment_list.view.PaymentListUiState
import io.reactivex.Single
import timber.log.Timber

class PaymentListRepository(private val service: PaymentService, preferences: SharedPreferenceWrapper) {

    private val transactionsUrl = preferences.baseUrl + TRANSACTIONS_ENDPOINT

    private var url: String? = allTransactionsUrl()

    fun getPayments(loadingPaymentListType: LoadingPaymentListType, customer: Customer?): Single<PaymentListUiState> {
        val isInitialLoad = loadingPaymentListType == LoadingPaymentListType.INITIAL

        if (isInitialLoad) {
            url = initialUrlFor(customer)
        }

        val requestUrl = url ?: return Single.just(PaymentListUiState.NoMoreData)

        return service.getPayments(requestUrl)
            .map { response ->
                if (response.data.isEmpty() && isInitialLoad) {
                    PaymentListUiState.Empty
                } else {
                    url = response.nextPageUrl?.let { withPageSize(it) }
                    PaymentListUiState.Success(response.data, loadingPaymentListType)
                }
            }
            .doOnError { error ->
                Timber.e(error)
            }
            .onErrorResumeNext { Single.just(PaymentListUiState.Error) }
    }

    /**
     * The per-customer endpoint paginates on its own. The agent-wide one only paginates when
     * `per_page` is given — without it the backend returns every transaction in a single
     * response (over a megabyte on established accounts).
     */
    private fun initialUrlFor(customer: Customer?) =
        if (customer != null) "$transactionsUrl/${customer.id}" else allTransactionsUrl()

    private fun allTransactionsUrl() = "$transactionsUrl?per_page=$PAGE_SIZE"

    /**
     * The backend builds `next_page_url` without carrying `per_page` over, so following it
     * verbatim would make the next page fall back to the unpaginated response.
     */
    private fun withPageSize(url: String) =
        if (url.contains("per_page=")) url else "$url${if (url.contains('?')) "&" else "?"}per_page=$PAGE_SIZE"

    companion object {
        private const val TRANSACTIONS_ENDPOINT = "app/agents/transactions"
        private const val PAGE_SIZE = 25
    }
}
