package freshloic.fr.freshcalculator;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;

final class Utils {
    static void setConfigTitleBar(ActionBar supportActionBar){
        if(supportActionBar != null){
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setLogo(R.mipmap.ic_launcher);
            supportActionBar.setDisplayUseLogoEnabled(true);
            SpannableStringBuilder title = new SpannableStringBuilder("FRESHCalculator");
            title.setSpan(new StyleSpan(Typeface.BOLD),0,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            supportActionBar.setTitle(title);
        }
    }

     static boolean isEmptyString(String text) {
        return (text == null
                || text.trim().equals("null")
                || text.trim().length() <= 0
                || TextUtils.isEmpty(text.trim())
        );
    }

     static boolean isValidFloat(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
