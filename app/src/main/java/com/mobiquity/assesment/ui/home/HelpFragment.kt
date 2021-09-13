package com.mobiquity.assesment.ui.home

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.mobiquity.assesment.R
import com.mobiquity.assesment.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_help.*

@AndroidEntryPoint
class HelpFragment : BaseFragment() {
    override fun getLayoutResourceId() = R.layout.fragment_help

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://openweathermap.org/guide")

    }
}