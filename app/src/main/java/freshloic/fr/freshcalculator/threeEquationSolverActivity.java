package freshloic.fr.freshcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import Jama.Matrix;

public class threeEquationSolverActivity extends AppCompatActivity {
    private EditText eq_1;
    private EditText eq_2;
    private EditText eq_3;
    private TextView tv_result;
    private String a_devant_x="";
    private String a_devant_y="";
    private String a_devant_z="";
    private String b_devant_x="";
    private String a_result="";
    private String b_devant_y="";
    private String b_result="";
    private String c_result="";
    private String c_devant_x="";
    private String b_devant_z="";
    private String c_devant_y="";
    private String c_devant_z="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_equation_solver);

        eq_1 = findViewById(R.id.eq1);
        eq_2 = findViewById(R.id.eq2);
        eq_3 = findViewById(R.id.eq3);
        Button b_go = findViewById(R.id.button_go_three);
        tv_result = findViewById(R.id.textView_resultat);

        b_go.setOnClickListener(view -> {
            if (Utils.isNotEmptyString(eq_1.getText().toString())
                    && Utils.isNotEmptyString(eq_2.getText().toString())
                    && Utils.isNotEmptyString(eq_3.getText().toString())) {

                try {

                    /*
                     *Solving three variable linear equation system
                     * 3x + 2y -1z =  1    Eqn(1)
                     * 2x - 2y + 4z = -2   Eqn(2)
                     * -1x + 0.5y -1z =  0  Eqn(3)
                     */

                    String  a = eq_1.getText().toString().replaceAll("\\s+", ""),
                            b = eq_2.getText().toString().replaceAll("\\s+", ""),
                            c = eq_3.getText().toString().replaceAll("\\s+", "");

                    String aString = a.replace("-", "+-");
                    String bString = b.replace("-", "+-");
                    String cString = c.replace("-", "+-");

                    aString = aString.replaceAll("^\\+", "");
                    bString = bString.replaceAll("^\\+", "");
                    cString = cString.replaceAll("^\\+", "");

                    aString = aString.replaceAll("=\\+", "=");
                    bString = bString.replaceAll("=\\+", "=");
                    cString = cString.replaceAll("=\\+", "=");

                    String[] arr_a = aString.split("\\+");
                    String[] arr_b = bString.split("\\+");
                    String[] arr_c = cString.split("\\+");
                    Log.e("arr_a", Arrays.toString(arr_a));
                    Log.e("arr_b", Arrays.toString(arr_b));
                    Log.e("arr_c", Arrays.toString(arr_c));

                    for (String item : arr_a) {
                        Log.e("item", item);
                        if (item.matches(".*x")){
                            Log.e("item good", item);
                            a_devant_x = item.replace("x", "");
                        }
                            
                        if (item.matches(".*y")){
                            Log.e("item good", item);
                            a_devant_y = item.replace("y", "");
                        }

                        if (item.matches(".*z.*")){
                            Log.e("item good", item);
                            a_devant_z = item.replaceAll("z.*", "");
                        }

                        if (item.matches(".*=.*")) {
                            Log.e("item good", item);
                            a_result = item.replaceAll(".*=", "");
                        }
                        Log.e("calcA", a_devant_x + " " + a_devant_y + " " + a_devant_z + " " + a_result);
                    }

                    for (String item : arr_b) {
                        Log.e("item", item);
                        if (item.matches(".*x")){
                            Log.e("item good", item);
                            b_devant_x = item.replace("x", "");
                        }

                        if (item.matches(".*y")){
                            Log.e("item good", item);
                            b_devant_y = item.replace("y", "");
                        }

                        if (item.matches(".*z.*")){
                            Log.e("item good", item);
                            b_devant_z = item.replaceAll("z.*", "");
                        }

                        if (item.matches(".*=.*")) {
                            Log.e("item good", item);
                            b_result = item.replaceAll(".*=", "");
                        }
                        Log.e("calcB", b_devant_x + " " + b_devant_y + " " + b_devant_z + " " + b_result);
                    }

                    for (String item : arr_c) {
                        Log.e("item", item);
                        if (item.matches(".*x")){
                            Log.e("item good", item);
                            c_devant_x = item.replace("x", "");
                        }

                        if (item.matches(".*y")){
                            Log.e("item good", item);
                            c_devant_y = item.replace("y", "");
                        }

                        if (item.matches(".*z.*")){
                            Log.e("item good", item);
                            c_devant_z = item.replaceAll("z.*", "");
                        }

                        if (item.matches(".*=.*")) {
                            Log.e("item good", item);
                            c_result = item.replaceAll(".*=", "");
                        }
                        Log.e("calcc", c_devant_x + " " + c_devant_y + " " + c_devant_z + " " + c_result);
                    }


                    //Creating  Arrays Representing Equations
                    double[][] lhsArray = {
                            {Double.parseDouble(a_devant_x), Double.parseDouble(a_devant_y), Double.parseDouble(a_devant_z)},
                            {Double.parseDouble(b_devant_x), Double.parseDouble(b_devant_y), Double.parseDouble(b_devant_z)},
                            {Double.parseDouble(c_devant_x), Double.parseDouble(c_devant_y), Double.parseDouble(c_devant_z)}
                    };

                    double[] rhsArray = {Double.parseDouble(a_result), Double.parseDouble(b_result), Double.parseDouble(c_result)};
                    //Creating Matrix Objects with arrays
                    Matrix lhs = new Matrix(lhsArray);
                    Matrix rhs = new Matrix(rhsArray, 3);
                    //Calculate Solved Matrix
                    Matrix ans = lhs.solve(rhs);
                    //Saving Answers
                    String reponse = String.format("x = %s\n", Math.round(ans.get(0, 0)));
                    reponse += String.format("y = %s\n", Math.round(ans.get(1, 0)));
                    reponse += String.format("z = %s\n", +Math.round(ans.get(2, 0)));

                    tv_result.setText(reponse);
                } catch (Exception ignored){
                    Toast.makeText(this, "Mauvaise Expression", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
