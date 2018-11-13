package freshloic.fr.freshcalculator;

import java.util.Arrays;
import java.util.Stack;

public class InfixToPostFix {

    private boolean check_error = false;

    private boolean isCheck_error() {
        return check_error;
    }

    private void setCheck_error() {
        this.check_error = true;
    }

    private String standardizeDouble(double num){
        int a = (int) num;
        return a == num ? Integer.toString(a) : Double.toString(num);
    }

    private boolean isCharPi(char c) {
        return c == 'π';
    }

    private boolean isNumPi(double num) {
        return  num == Math.PI;
    }

    private boolean isNum(char c) {
        return Character.isDigit(c) || isCharPi(c);
    }

    private String numToString(double num){
        return isNumPi(num) ? "π" : standardizeDouble(num);
    }

    public double stringToNum(String s){
        return isCharPi(s.charAt(0)) ? Math.PI : Double.parseDouble(s);
    }

    private boolean isOperator(char c) {
        char[] operator = {
                '+', '-', '×', '÷', '^', '~', 's', 'c', 't', '@', '!', '%', ')', '('
        }; //~ est le signe négatif d'un nombre (-) @ la racine carrée

        Arrays.sort(operator);

        return Arrays.binarySearch(operator, c) <= -1;
    }

    private int priority(char c){
        switch (c) {
            case '+' : case '-' : return 1;
            case '×' : case '÷' : return 2;
            case '~' : return 3;
            case '@' : case '!' : case '^' : return 4;
            case 's' : case 'c' : case 't' : return 5;
        }
        return 0;
    }

    private boolean isOneMath(char c){
        char[] operator = {'s', 'c', 't', '@', '('};
        Arrays.sort(operator);

        return Arrays.binarySearch(operator, c) > -1;
    }

    private String standardize(String s){
        StringBuilder s1 = new StringBuilder();

        s = s.trim();
        s = s.replaceAll("\\s+"," "); //	c

        int open = 0, close = 0;

        for (int i=0; i<s.length(); i++){
            char c = s.charAt(i);
            if (c == '(') open++;
            if (c == ')') close++;
        }

        for (int i = 0; i> (open - close); i++) // auto remove ")"
            s = s.replace("\\)(?!.*\\))","");

        StringBuilder sBuilder = new StringBuilder(s);

        for (int i = 0; i< (open - close); i++) // auto ajout ")"
            sBuilder.append(')');

        s = sBuilder.toString();

        for (int i=0; i<s.length(); i++){
            if (i>0 && isOneMath(s.charAt(i)) && (s.charAt(i-1) == ')' || isNum(s.charAt(i-1))))
                s1.append("×"); //	règle le probleme:  (exp)(exp) devient (exp)×(exp)

            else if ((i == 0 || !isNum(s.charAt(i-1))) && s.charAt(i) == '-' && isNum(s.charAt(i+1)))
                s1.append("~"); // check so am

            else if (i>0 && (isNum(s.charAt(i-1)) || s.charAt(i-1) == ')') && isCharPi(s.charAt(i)))
                s1.append("×").append(s.charAt(i)); // Ex :  6π , exp)π devient 6×π , exp)×π

            else s1.append(s.charAt(i));
        }

        return s1.toString();
    }

    public String[] processString(String sMath){
        String s1, elementMath[]; char c;
        sMath = standardize(sMath);
        StringBuilder s1Builder = new StringBuilder();

        for (int i = 0; i<sMath.length(); i++){
            c = sMath.charAt(i);

            if (i<sMath.length()-1 && isCharPi(c) && !isOperator(sMath.charAt(i + 1))){
                setCheck_error();
                return null;
            }
            else if (isOperator(c)) s1Builder.append(c);
            else s1Builder.append(" ").append(c).append(" ");
        }

        s1 = s1Builder.toString();
        s1 = s1.trim();
        s1 = s1.replaceAll("\\s+"," "); //
        elementMath = s1.split(" "); //

        return elementMath;
    }

    public String[] postfix(String[] elementMath){
        StringBuilder s1 = new StringBuilder();
        Stack <String> S = new Stack<>();

        for (String anElementMath : elementMath) {
            char c = anElementMath.charAt(0);

            if (!isOperator(c)) s1.append(anElementMath).append(" ");
            else {
                if (c == '(') S.push(anElementMath);
                else {
                    if (c == ')') {
                        char c1;
                        c1 = S.peek().charAt(0);
                        if (c1 != '(') s1.append(S.peek()).append(" ");
                        S.pop();

                        while (c1 != '(') {
                            c1 = S.peek().charAt(0);
                            if (c1 != '(') s1.append(S.peek()).append(" ");
                            S.pop();
                        }
                    } else {
                        while (!S.isEmpty() && priority(S.peek().charAt(0)) >= priority(c)) s1.append(S.pop()).append(" ");
                        S.push(anElementMath);
                    }
                }
            }
        }

        while (!S.isEmpty()) s1.append(S.pop()).append(" ");
        return s1.toString().split(" ");
    }

    public String valueMath(String[] elementMath){
        Stack <Double> S = new Stack<>();
        double num = 0;
        for (String anElementMath : elementMath) {
            char c = anElementMath.charAt(0);
            if (isCharPi(c)) S.push(Math.PI);
            else {
                if (!isOperator(c)) S.push(Double.parseDouble(anElementMath));
                else {
                    double num1 = S.pop();
                    switch (c) {
                        case '~':
                            num = -num1;
                            break;
                        case 's':
                            num = Math.sin(num1);
                            break;
                        case 'c':
                            num = Math.cos(num1);
                            break;
                        case 't':
                            num = Math.tan(num1);
                            break;
                        case '%':
                            num = num1 / 100;
                            break;
                        case '@':
                            if (num1 >= 0) num = Math.sqrt(num1);
                            else setCheck_error();
                            break;
                        case '!':
                            if (num1 >= 0 && (int) num1 == num1) {
                                num = 1;
                                for (int j = 1; j <= (int) num1; j++) num = num * j;
                            } else setCheck_error();
                            break;
                        default:
                            break;
                    }
                    if (!S.empty()) {
                        double num2 = S.peek();
                        switch (c) {
                            //-----------------------
                            case '+':
                                num = num2 + num1;
                                S.pop();
                                break;
                            case '-':
                                num = num2 - num1;
                                S.pop();
                                break;
                            case '×':
                                num = num2 * num1;
                                S.pop();
                                break;
                            case '÷':
                                if (num1 != 0) num = num2 / num1;
                                else setCheck_error();
                                S.pop();
                                break;
                            case '^':
                                num = Math.pow(num2, num1);
                                S.pop();
                                break;
                        }
                    }
                    S.push(num);
                }
            }
        }
        return numToString(S.pop());
    }
}
