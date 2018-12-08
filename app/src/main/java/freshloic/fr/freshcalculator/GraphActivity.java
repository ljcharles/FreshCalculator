package freshloic.fr.freshcalculator;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView screenMath;
    private GraphView graph;
    private int checkSubmit = 0;
    private StringBuilder textMath = new StringBuilder();
    private StringBuilder screenTextMath = new StringBuilder();

    private final int[] idArray = {
            R.id.btnSin2,R.id.btnCos2,R.id.btnTan2,R.id.btnX,R.id.btnClear2,
            R.id.btnBracketsOpen2,R.id.btnBracketsClose2,R.id.btnBackSpace2,
            R.id.btnSeven,R.id.btnNine,R.id.btnEight,R.id.btnDiv2,
            R.id.btnFour,R.id.btnFive,R.id.btnSix,R.id.btnMulti2,
            R.id.btnOne,R.id.btnTwo,R.id.btnThree,R.id.btnMinus2,R.id.btnAdd2,
            R.id.btnZero
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        FloatingActionButton add_graph = findViewById(R.id.add_graph);
        screenMath = findViewById(R.id.add_function);
        graph = findViewById(R.id.graph);

        for (int anIdArray : idArray) if(findViewById(anIdArray) != null) (findViewById(anIdArray)).setOnClickListener(this);

        add_graph.setOnClickListener(view -> {
            String name = textMath.toString();

            if (Utils.isNotEmptyString(name)){
                /*if (Utils.isContainsRegex(name,"X")){*/
                    Toast.makeText(this, "Function Added", Toast.LENGTH_SHORT).show();
                    makeGraph(name);
                    checkSubmit = 1;
                /*} else {
                    Toast.makeText(this, "Il manque X dans la fonction", Toast.LENGTH_SHORT).show();
                }*/
            }else {
                Toast.makeText(this, "Function not Added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.graph_menu,menu);
        MenuItem shareItem = menu.findItem(R.id.shareGraph);

        shareItem.setOnMenuItemClickListener(menuItem -> {
            shareGraph();
            return false;
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void shareGraph() {
        String sujet = "Graphique";
        String body = "L'expression du graphique est : " + screenMath.getText().toString();
        Bitmap image = Utils.screenShot(graph);
        Utils.shareUp(this, sujet, body, image);
    }

    private void makeGraph(String name) {
        double x,y;

        x=0.0;

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

        String StringToEval,result;
        String[] elementMath;


        for (int i=0; i<500; i++){
            x += 0.01;
            StringToEval = name.replace("X", Double.toString(x));
            StringToExpressionFix ITP = new StringToExpressionFix();

            elementMath = ITP.processString(StringToEval);
            elementMath = ITP.postfix(elementMath);
            result = ITP.valueMath(elementMath);
            y = Double.parseDouble(result);

            series.appendData(new DataPoint(x,y), true, 500);
        }
        graph.addSeries(series);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnX:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    if (textMath.length() > 0) {
                        char c = textMath.charAt(textMath.length() - 1);
                        if(c != 'X' && (!Character.isDigit(c))){
                            textMath.append("X");
                            screenTextMath.append("X");
                        }
                    }
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnZero:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("0");
                    screenTextMath.append("0");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnOne:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("1");
                    screenTextMath.append("1");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnTwo:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("2");
                    screenTextMath.append("2");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnThree:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("3");
                    screenTextMath.append("3");
                }

                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnFour:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("4");
                    screenTextMath.append("4");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnFive:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("5");
                    screenTextMath.append("5");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnSix:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("6");
                    screenTextMath.append("6");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnSeven:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("7");
                    screenTextMath.append("7");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnEight:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("8");
                    screenTextMath.append("8");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnNine:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("9");
                    screenTextMath.append("9");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnAdd2:
                if (screenTextMath.length() > 0) {
                    if (screenTextMath.length() < 38) {    //if length < 38
                        if (checkSubmit == 1) {
                            screenTextMath = new StringBuilder();
                            textMath = new StringBuilder();
                            checkSubmit = 0;
                        }
                        if (textMath.length() > 0) {
                            char c = textMath.charAt(textMath.length() - 1);
                            if(c != '+' && (c != '*') && (c != '-') && (c != '/')) {
                                textMath.append("+");
                                screenTextMath.append("+");
                            }
                        }

                    }
                    screenMath.setText(screenTextMath.toString());
                }
                break;
            case R.id.btnMinus2:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    if (textMath.length() > 0) {
                        char c = textMath.charAt(textMath.length() - 1);
                        if(c != '-' && (c != '+')) {
                            textMath.append("-");
                            screenTextMath.append("-");
                        }
                    }

                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnMulti2:
                if (screenTextMath.length() > 0) {
                    if (screenTextMath.length() < 38) {    //if length < 38
                        if (checkSubmit == 1) {
                            screenTextMath = new StringBuilder();
                            textMath = new StringBuilder();
                            checkSubmit = 0;
                        }
                        if (textMath.length() > 0) {
                            char c = textMath.charAt(textMath.length() - 1);
                            if(c != '*' && (c != '/') && (c != '-') && (c != '+')) {
                                textMath.append("*");
                                screenTextMath.append("*");
                            }
                        }
                    }
                    screenMath.setText(screenTextMath.toString());
                }
                break;
            case R.id.btnDiv2:
                if (screenTextMath.length() > 0) {
                    if (screenTextMath.length() < 38) {    //if length < 38
                        if (checkSubmit == 1) {
                            screenTextMath = new StringBuilder();
                            textMath = new StringBuilder();
                            checkSubmit = 0;
                        }
                        if (textMath.length() > 0) {
                            char c = textMath.charAt(textMath.length() - 1);
                            if(c != '/' && (c != '*') && (c != '-') && (c != '+')) {
                                textMath.append("/");
                                screenTextMath.append("÷");
                            }
                        }

                    }
                    screenMath.setText(screenTextMath.toString());
                }
                break;
            case R.id.btnSin2:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("s(");
                    screenTextMath.append("Sin(");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnCos2:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("c(");
                    screenTextMath.append("Cos(");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnTan2:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("t(");
                    screenTextMath.append("Tan(");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnBracketsOpen2:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("(");
                    screenTextMath.append("(");
                }
                screenMath.setText(screenTextMath.toString());
                break;
            case R.id.btnBracketsClose2:
                if (screenTextMath.length() > 0) {
                    if (screenTextMath.length() < 38) {    //if length < 38
                        if (checkSubmit == 1) {
                            screenTextMath = new StringBuilder();
                            textMath = new StringBuilder();
                            checkSubmit = 0;
                        }
                        textMath.append(")");
                        screenTextMath.append(")");
                    }
                    screenMath.setText(screenTextMath.toString());
                }
                break;
            case R.id.btnClear2:
                textMath = new StringBuilder();
                screenTextMath = new StringBuilder();
                screenMath.setText("");
                graph.removeAllSeries();
                break;
            case R.id.btnBackSpace2:
                if (screenMath.length() > 0 && textMath.length() > 0) {
                    char c = textMath.charAt(textMath.length() - 1);
                    if (textMath.length() > 1 && c == '(' && textMath.charAt(textMath.length() - 2) == '^') {
                        screenTextMath = new StringBuilder(screenTextMath.substring(0, screenTextMath.length() - 2));
                        textMath = new StringBuilder(textMath.substring(0, textMath.length() - 2));
                    } else if (textMath.length() > 1 && c == '(' && (textMath.charAt(textMath.length() - 2) == 's' || textMath.charAt(textMath.length() - 2) == 'c' || textMath.charAt(textMath.length() - 2) == 't')) {
                        textMath = new StringBuilder(textMath.substring(0, textMath.length() - 2));
                        screenTextMath = new StringBuilder(screenTextMath.substring(0, screenTextMath.length() - 4));
                    } else {
                        textMath = new StringBuilder(textMath.substring(0, textMath.length() - 1));
                        screenTextMath = new StringBuilder(screenTextMath.substring(0, screenTextMath.length() - 1));
                    }
                }
                screenMath.setText(screenTextMath.toString());
                break;
        }
    }
}
