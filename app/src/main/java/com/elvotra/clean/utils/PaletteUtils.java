package com.elvotra.clean.utils;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

public class PaletteUtils {
    public static Palette createPaletteSync(Bitmap bitmap) {
        Palette p = Palette.from(bitmap).generate();
        return p;
    }
}


