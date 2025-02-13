package github.tornaco.android.thanos.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Tornaco on 2018/6/13 14:07.
 * This file is written for project X-APM at host guohao4.
 */
public class TypefaceHelper {

    private static Typeface sGoogleFont;
    private static Typeface sGoogleFontBold;
    private static Typeface sJetbrainsMono;

    public static Typeface googleSans(Context context) {
        synchronized (TypefaceHelper.class) {
            if (sGoogleFont == null) {
                sGoogleFont = Typeface.createFromAsset(context.getAssets(), "fonts/google/ProductSansRegular.ttf");
            }
            return sGoogleFont;
        }
    }

    public static Typeface googleSansBold(Context context) {
        synchronized (TypefaceHelper.class) {
            if (sGoogleFontBold == null) {
                sGoogleFontBold = Typeface.createFromAsset(context.getAssets(), "fonts/google/ProductSansBold.ttf");
            }
            return sGoogleFontBold;
        }
    }

    public static Typeface jetbrainsMono(Context context) {
        synchronized (TypefaceHelper.class) {
            if (sJetbrainsMono == null) {
                sJetbrainsMono = Typeface.createFromAsset(context.getAssets(), "fonts/google/jetbrains/JetBrainsMonoRegular.ttf");
            }
            return sJetbrainsMono;
        }
    }
}
