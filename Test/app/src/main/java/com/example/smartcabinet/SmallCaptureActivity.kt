package com.example.smartcabinet

import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kotlinx.android.synthetic.main.capture_small.*
import android.view.SurfaceHolder





/**
 * This activity has a margin.
 */
class SmallCaptureActivity : CaptureActivity() {
    protected override fun initializeContent(): DecoratedBarcodeView {
        setContentView(R.layout.capture_small)
        return zxing_barcode_scanner as DecoratedBarcodeView
    }


}