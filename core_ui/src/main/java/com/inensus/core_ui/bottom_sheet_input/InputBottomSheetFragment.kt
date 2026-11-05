package com.inensus.core_ui.bottom_sheet_input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.inensus.core.Constants
import com.inensus.core.sharedpreferences.SharedPreferenceWrapper
import com.inensus.core_ui.R
import kotlinx.android.synthetic.main.fragment_input_bottom_sheet.*
import org.koin.android.ext.android.inject

class InputBottomSheetFragment : BottomSheetDialogFragment() {

    private val preferences: SharedPreferenceWrapper by inject()

    override fun getTheme() = R.style.AppTheme_BottomSheet

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(activity, R.style.AppTheme)

        return layoutInflater.cloneInContext(contextThemeWrapper).inflate(R.layout.fragment_input_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputView.getTitleView().text = getString(R.string.title_server_url)
        inputView.setText(preferences.baseUrl.orEmpty().removeSuffix(SharedPreferenceWrapper.BASE_URL_SUFFIX))
        inputView.getMainTextView().hint = getString(R.string.hint_server_url)

        negativeButton.setOnClickListener {
            dismiss()
        }

        positiveButton.setOnClickListener {
            if (inputView.getText().matches(Constants.HTTP_REGEX)) {
                preferences.baseUrl = inputView.getText()
                dismiss()
            } else {
                inputView.showErrorMessage(getString(R.string.server_url_validation))
            }
        }
    }

    companion object {
        fun newInstance() = InputBottomSheetFragment()
    }
}
