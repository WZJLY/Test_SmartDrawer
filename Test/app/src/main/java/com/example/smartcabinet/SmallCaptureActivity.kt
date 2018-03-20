package com.example.smartcabinet

import android.view.KeyEvent
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kotlinx.android.synthetic.main.capture_small.*


/**
 * This activity has a margin.
 */
class SmallCaptureActivity : CaptureActivity() {
    protected override fun initializeContent(): DecoratedBarcodeView {
        setContentView(R.layout.capture_small)
        return zxing_barcode_scanner as DecoratedBarcodeView
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode === KeyEvent.KEYCODE_BACK) {
            return true // return true 和 false 我都试过，都能屏蔽，原因还未知，希望知道的可以告诉我一下，谢谢
        }
        return super.onKeyDown(keyCode, event)
    }
}