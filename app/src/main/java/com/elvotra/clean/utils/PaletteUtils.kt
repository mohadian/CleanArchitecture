package com.elvotra.clean.utils

import android.graphics.Bitmap
import android.support.v7.graphics.Palette

object PaletteUtils {
    fun createPaletteSync(bitmap: Bitmap): Palette {
        return Palette.from(bitmap).generate()
    }
}


