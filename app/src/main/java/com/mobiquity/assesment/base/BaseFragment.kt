package com.mobiquity.assesment.base

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobiquity.assesment.R
import com.mobiquity.assesment.ui.MainActivity
import dmax.dialog.SpotsDialog

abstract class BaseFragment : Fragment() {

    private lateinit var spotsDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(getLayoutResourceId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spotsDialog = getAlertDialog(requireContext())
    }

    /* override this method in fragment to update page title*/
    open fun setPageTitle(title: String) {
        getActionBar()?.let {
            it.title = title
            it.show()
        }
    }

    private fun getActionBar() = ((activity as? MainActivity)?.supportActionBar)

    /* override this method in fragment to attach the layout*/
    abstract fun getLayoutResourceId(): Int

    private fun getAlertDialog(context: Context, cancelable: Boolean = false): AlertDialog {
        return SpotsDialog.Builder()
            .setContext(context)
            .setTheme(R.style.DialogTheme)
            .setCancelable(cancelable)
            .build()
    }

    fun showDialog() {
        if (::spotsDialog.isInitialized && !spotsDialog.isShowing)
            spotsDialog.show()
    }

    fun hideDialog() {
        if (::spotsDialog.isInitialized && spotsDialog.isShowing)
            spotsDialog.dismiss()
    }
}