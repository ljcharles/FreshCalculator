package freshloic.fr.freshcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ncorti.slidetoact.SlideToActView;

import java.util.Calendar;
import java.util.Objects;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView screenAns, screenMath;
    SlideToActView slideToActView;
    DatabaseHelper databaseHelper;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private ImageButton mSpeakBtn;

    StringBuilder textMath = new StringBuilder();
    StringBuilder textAns = new StringBuilder("0");
    StringBuilder screenTextMath = new StringBuilder();
    String expressionInHistory = "lol";
    String resultatInHistory = "lol";
    int checkSubmit = 0;

    private int[] idArray = {
            R.id.btnSin,R.id.btnCos,R.id.btnTan,R.id.btnLog,R.id.btnFactorial,R.id.btnPow,R.id.btnModulo,
            R.id.btnClear,R.id.btnBackSpace,R.id.btnBracketsOpen,R.id.btnBracketsClose,R.id.btnSquare,
            R.id.btnSeven,R.id.btnNine,R.id.btnEight,R.id.btnDiv,R.id.btnMod,R.id.btnBin,R.id.btnDec,
            R.id.btnFour,R.id.btnFive,R.id.btnSix,R.id.btnMulti,R.id.btnInverse,
            R.id.btnOne,R.id.btnTwo,R.id.btnThree,R.id.btnMinus,R.id.btnAdd,R.id.btnResult,
            R.id.btnZero,R.id.btnDot,R.id.btnPi,R.id.btnE,R.id.btnPlusMoins,R.id.btnUnivers
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setConfigTitleBar(getSupportActionBar());

        setContentView(R.layout.activity_main);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Lato-Regular.ttf",true);

        databaseHelper = new DatabaseHelper(this);

        screenAns = findViewById(R.id.txtResult);
        screenMath = findViewById(R.id.txtCal);

        Intent receivedIntent = getIntent();
        expressionInHistory = receivedIntent.getStringExtra("expression");
        resultatInHistory = receivedIntent.getStringExtra("resultat");

        if(!Objects.equals(expressionInHistory, "lol") && !Objects.equals(resultatInHistory, "lol")){
            screenAns.setText(resultatInHistory);
            screenMath.setText(expressionInHistory);
        }

        slideToActView = findViewById(R.id.slide_to_unlock);
        slideToActView.setOnSlideCompleteListener(slideToActView -> {
            startActivity(new Intent(MainActivity.this,HistoryActivity.class));
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        });

        for (int anIdArray : idArray) if(findViewById(anIdArray) != null) (findViewById(anIdArray)).setOnClickListener(this);
        
        mSpeakBtn = (ImageButton) findViewById(R.id.btnSpeak);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
    }
    
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Bonjour, Entrer votre calcul !");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    screenMath.setText(result.get(0));
                }
                break;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideToActView.resetSlider();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        screenAns = findViewById(R.id.txtResult);
        screenMath = findViewById(R.id.txtCal);

        CharSequence reponse = screenAns.getText().toString();
        CharSequence expression = screenMath.getText().toString();

        outState.putCharSequence("reponse", reponse);
        outState.putCharSequence("expression", expression);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        screenAns = findViewById(R.id.txtResult);
        screenMath = findViewById(R.id.txtCal);

        CharSequence reponse =  Objects.requireNonNull(savedInstanceState.getCharSequence("reponse")).toString();
        CharSequence expression =  Objects.requireNonNull(savedInstanceState.getCharSequence("expression")).toString();
        screenAns.setText(reponse);
        screenMath.setText(expression);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.title1:
                Toast.makeText(this, "Item1 Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void error(){
        screenAns.setText(R.string.mathError);
        checkSubmit = 1;
        textAns = textMath = screenTextMath = new StringBuilder();
    }

    public void submit(){
        StringToExpressionFix ITP = new StringToExpressionFix();
        String[] elementMath = null;

        if (textMath.length()>0){

            char c = textMath.charAt(textMath.length() - 1);
            if(c == '-' || (c == '+') || (c == '/') || (c == '*')) {
                textMath.setLength(textMath.length() - 1);
                screenTextMath.setLength(screenTextMath.length() - 1);
                screenMath.setText(screenTextMath);
            }

            try{
                if (!ITP.check_error) elementMath = ITP.processString(textMath.toString());
                //	On sépare l'expression en sous éléments
                if (!ITP.check_error) elementMath = ITP.postfix(elementMath);
                // 	On verifie les priorités et on formate le resultat pour l'affichage
                if (!ITP.check_error) textAns = new StringBuilder(ITP.valueMath(elementMath)); // On récupere le résultat

                screenAns.setText(textAns);

                if (!ITP.check_error){
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    if (Utils.isValidFloat(textAns.toString()))
                        databaseHelper.insertData(screenTextMath.toString(),textAns.toString(), mydate,"defaut");
                }

            }catch(Exception e){ error();}
            if (ITP.check_error) error();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
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
                screenMath.setText(screenTextMath);
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
                screenMath.setText(screenTextMath);
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
                screenMath.setText(screenTextMath);
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

                screenMath.setText(screenTextMath);
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
                screenMath.setText(screenTextMath);
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
                screenMath.setText(screenTextMath);
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
                screenMath.setText(screenTextMath);
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
                screenMath.setText(screenTextMath);
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
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnDot:

                if (screenMath.length() > 0 && textMath.length() > 0) {
                    char c = textMath.charAt(textMath.length() - 1);
                    if (c == '.') break;
                }

                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    if (textMath.length() > 0) {
                        char c = textMath.charAt(textMath.length() - 1);
                        if(c != '.') {
                            textMath.append(".");
                            screenTextMath.append(",");
                        }
                    }
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnPi:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("π");
                    screenTextMath.append("π");
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnE:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("e");
                    screenTextMath.append("e");
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnAdd:
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
                    screenMath.setText(screenTextMath);
                }
                break;
            case R.id.btnMinus:
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
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnMulti:
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
                                screenTextMath.append("×");
                            }
                        }
                    }
                    screenMath.setText(screenTextMath);
                }
                break;
            case R.id.btnDiv:
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
                    screenMath.setText(screenTextMath);
                }
                break;
            case R.id.btnPow:
                if (screenTextMath.length() > 0) {
                    if (screenTextMath.length() < 38) {    //if length < 38
                        if (checkSubmit == 1) {
                            screenTextMath = new StringBuilder();
                            textMath = new StringBuilder();
                            checkSubmit = 0;
                        }

                        if (textMath.length() > 1) {
                            String c = String.valueOf(textMath.charAt(textMath.length() - 2) + textMath.charAt(textMath.length() - 1));
                            if(!Objects.equals(c, "^(")) {
                                textMath.append("^(");
                                screenTextMath.append("^(");
                            }
                        }

                    }
                    screenMath.setText(screenTextMath);
                }
                break;
            case R.id.btnModulo:
                if (screenTextMath.length() > 0) {
                    if (screenTextMath.length() < 38) {    //if length < 38
                        if (checkSubmit == 1) {
                            screenTextMath = new StringBuilder();
                            textMath = new StringBuilder();
                            checkSubmit = 0;
                        }
                        textMath.append("m(");
                        screenTextMath.append("mod(");
                    }
                    screenMath.setText(screenTextMath);
                }
                break;
            case R.id.btnSquare:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    if (textMath.length() > 0) {
                        char c = textMath.charAt(textMath.length() - 1);
                        if(c != '@') {
                            textMath.append("@");
                            screenTextMath.append("√");
                        }
                    }
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnLog:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("l(");
                    screenTextMath.append("Log(");
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnBin:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("b(");
                    screenTextMath.append("bin(");
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnDec:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("d(");
                    screenTextMath.append("dec(");
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnSin:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("s(");
                    screenTextMath.append("Sin(");
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnCos:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("c(");
                    screenTextMath.append("Cos(");
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnTan:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("t(");
                    screenTextMath.append("Tan(");
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnUnivers:
                if (!Objects.equals(screenTextMath.toString(), "42")) {
                    if (screenTextMath.length() < 38) {    //if length < 38
                        if (checkSubmit == 1) {
                            screenTextMath = new StringBuilder();
                            textMath = new StringBuilder();
                            checkSubmit = 0;
                        }
                        textMath.append("42");
                        screenTextMath.append("42");
                        screenAns.setText(R.string.Univers);
                    }
                    screenMath.setText(screenTextMath);
                }
                break;
            case R.id.btnBracketsOpen:
                if (screenTextMath.length() < 38) {    //if length < 38
                    if (checkSubmit == 1) {
                        screenTextMath = new StringBuilder();
                        textMath = new StringBuilder();
                        checkSubmit = 0;
                    }
                    textMath.append("(");
                    screenTextMath.append("(");
                }
                screenMath.setText(screenTextMath);
                break;
            case R.id.btnBracketsClose:
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
                    screenMath.setText(screenTextMath);
                }
                break;

            case R.id.btnMod:
                if (screenTextMath.length() == 0) screenTextMath = new StringBuilder("0");
                screenTextMath = new StringBuilder("(" + screenTextMath + ")%");
                screenMath.setText(screenTextMath.toString());
                if (checkSubmit == 0) submit();
                textMath = new StringBuilder(textAns + "/100");
                submit();
                break;
            case R.id.btnPlusMoins:
                if (screenTextMath.length() < 1) screenTextMath = new StringBuilder("0");
                screenTextMath = new StringBuilder("-(" + screenTextMath + ")");
                textMath.append("~");
                screenMath.setText(screenTextMath.toString());
                if (checkSubmit == 0) submit();
                break;
            case R.id.btnFactorial:
                if (screenTextMath.length() > 0) {
                    if (screenTextMath.length() < 38) {    //if length < 38
                        if (checkSubmit == 1) {
                            screenTextMath = new StringBuilder();
                            textMath = new StringBuilder();
                            checkSubmit = 0;
                        }

                        if (textMath.length() > 0) {
                            char c = textMath.charAt(textMath.length() - 1);
                            if(c == '!') {
                                textMath.append("!");
                                screenTextMath.append("!");
                            }
                        }
                    }
                    screenMath.setText(screenTextMath);
                }
                break;
            case R.id.btnInverse:
                if (screenTextMath.length() == 0) screenTextMath = new StringBuilder(textAns);
                screenTextMath = new StringBuilder("1/(" + screenTextMath + ")");
                screenMath.setText(screenTextMath);
                if (checkSubmit == 0) submit();
                textMath = new StringBuilder("1/" + textAns);
                submit();
                break;
            case R.id.btnResult:
                if (screenTextMath.length() == 0 || (Objects.equals(screenTextMath.toString(), "√"))) break;
                submit();
                break;
            case R.id.btnClear:
                textMath = new StringBuilder();
                screenTextMath = new StringBuilder();
                textAns = new StringBuilder("0");
                screenAns.setText(textAns);
                screenMath.setText("|");
                break;
            case R.id.btnBackSpace:
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
                screenMath.setText(screenTextMath);
                break;
        }
    }
}
