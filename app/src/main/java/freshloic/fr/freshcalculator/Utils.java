package freshloic.fr.freshcalculator;

import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import java.util.regex.Pattern;

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

     static boolean isNotEmptyString(String text) {
        return (text != null
                && !text.trim().equals("null")
                && text.trim().length() > 0
                && !TextUtils.isEmpty(text.trim()));
    }

     static boolean isValidFloat(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    static boolean isNumberPhone(String s) {
        return Pattern.compile("^\\d{3}\\d{3}\\d{4}$").matcher(s).find();
    }

    static boolean isContainsLetters(String s) {
        return Pattern.compile("[a-zA-Z]+").matcher(s).find();
    }
}
