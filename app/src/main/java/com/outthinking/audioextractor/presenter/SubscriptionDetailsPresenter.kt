package com.outthinking.audioextractor.presenter

import android.text.TextUtils
import androidx.leanback.widget.AbstractDetailsDescriptionPresenter

class SubscriptionDetailsPresenter  : AbstractDetailsDescriptionPresenter() {
    override fun onBindDescription(viewHolder: ViewHolder, item: Any) {
        val subscription = item as SubscriptionContent

        viewHolder.title.text = subscription.title
        viewHolder.subtitle.text = subscription.subtitle
        if (!TextUtils.isEmpty(subscription.description)) {
            viewHolder.body.text = subscription.description
        }
    }
}