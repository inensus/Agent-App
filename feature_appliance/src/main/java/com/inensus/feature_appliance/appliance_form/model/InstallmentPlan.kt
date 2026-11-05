package com.inensus.feature_appliance.appliance_form.model

/** How often an installment falls due. Mirrors the backend's `rate_type`. */
enum class RateType(val apiValue: String) {
    WEEKLY("weekly"),
    MONTHLY("monthly");

    companion object {
        fun fromLabel(label: String) = values().firstOrNull { it.apiValue.equals(label, ignoreCase = true) } ?: MONTHLY
    }
}

/**
 * The two ways the web admin sells an appliance. Both end up sending the same thing; they
 * differ only in which side of the schedule the agent types in.
 */
enum class InstallmentMode {
    /** The agent enters how many installments there will be. */
    COUNT,

    /** The agent enters what one installment costs, and the count follows from it. */
    COST
}
