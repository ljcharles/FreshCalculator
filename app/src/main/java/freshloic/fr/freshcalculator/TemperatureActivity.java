package freshloic.fr.freshcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;

public class TemperatureActivity extends AppCompatActivity {
    TextView tv_c, tv_f;
    SeekBar seekBar;
    Double c, f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        tv_c = findViewById(R.id.celsius);
        tv_f = findViewById(R.id.fren);

        seekBar = findViewById(R.id.seekBar2);
        seekBar.setMax(400);
        seekBar.setProgress(200);

        c = (double) (seekBar.getProgress() - 200);
        f = c * 1.38 + 32;

        tv_c.setText(String.format(Locale.getDefault(),"%.1f C°",c));
        tv_f.setText(String.format(Locale.getDefault(),"%.1f F°",f));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                c = (double) (i - 200);
                f = c * 1.38 + 32;

                tv_c.setText(String.format(Locale.getDefault(),"%.1f C°",c));
                tv_f.setText(String.format(Locale.getDefault(),"%.1f F°",f));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
