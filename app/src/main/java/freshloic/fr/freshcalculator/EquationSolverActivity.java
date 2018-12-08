package freshloic.fr.freshcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EquationSolverActivity extends AppCompatActivity {
    EditText et_a, et_b, et_c;
    Button b_go;
    TextView tv_result;
    double a, b, c, d, x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equation_solver);

        et_a = findViewById(R.id.solv1);
        et_b = findViewById(R.id.solv2);
        et_c = findViewById(R.id.solv3);
        b_go = findViewById(R.id.button_go);
        tv_result = findViewById(R.id.textView_result);

        b_go.setOnClickListener(view -> {
          if (Utils.isNotEmptyString(et_a.getText().toString())
                  && Utils.isNotEmptyString(et_b.getText().toString())
                  && Utils.isNotEmptyString(et_c.getText().toString())) {
              a = Double.parseDouble(et_a.getText().toString());
              b = Double.parseDouble(et_b.getText().toString());
              c = Double.parseDouble(et_c.getText().toString());

              d = Math.pow(b,2)-4*a*c;

              if (d == 0){
                x1 = -b/(2*a);
                  tv_result.setText(String.format("L'équation n'admet qu'une solution réelle.  \nΔ = %s\nx = %s", d, x1));
              } else if (d < 0){
                tv_result.setText(R.string.root_negatif);
              } else if (d > 0){
                  x1 = (-b+Math.sqrt(d))/(2*a);
                  x2 = (-b-Math.sqrt(d))/(2*a);
                  tv_result.setText(
                          String.format("L'équation admet deux solutions.  \nΔ = %s\nx1 = %s\nx2 = %s", d, x1, x2));
              }
          }
        });


    }
}
