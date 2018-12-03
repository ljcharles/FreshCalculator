package freshloic.fr.freshcalculator;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

public class MySpinner extends android.support.v7.widget.AppCompatSpinner {

    public MySpinner(Context context) {
        super(context);

        init();
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init()
    {
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        String[] items = databaseHelper.viewCategory();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        setAdapter(adapter2);
    }
}
