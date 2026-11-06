package com.inensus.core.utils

import android.content.Context
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper

/**
 * The currency amounts are shown in, as configured by the company on the backend.
 *
 * The app used to print a hardcoded "NGN" next to every amount, which was wrong for every
 * company that does not bill in naira. The value is fetched once per launch and cached in
 * preferences; until that first fetch succeeds this is empty, so an amount is shown on its
 * own rather than next to a currency that may well be the wrong one.
 */
fun Context.currency(): String = SharedPreferenceWrapper(this).currency
