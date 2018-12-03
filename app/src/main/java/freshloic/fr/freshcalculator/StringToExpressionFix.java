package freshloic.fr.freshcalculator;

import java.util.Arrays;
import java.util.Stack;

class StringToExpressionFix {
    boolean check_error = false;

    private String standardizeDouble(double num){
        int a = (int)num;
        if (a == num)
            return Integer.toString(a);
        else return Double.toString(num);
    }

    private boolean isCharE(char c){
        return c == 'e';
    }

    private boolean isCharPi(char c){
        return c == 'π';
    }

    private boolean isNumPi(double num){
        return num == Math.PI;
    }

    private boolean isNumE(double num){
        return num == Math.E;
    }

    private boolean isNum(char c){
        return Character.isDigit(c) || (isCharPi(c) || isCharE(c));
    }

    private String NumToString(double num){
        if (isNumPi(num)) return String.valueOf(Math.PI);
        else if (isNumE(num)) return String.valueOf(Math.E);
        else return standardizeDouble(num);
    }

    private double toBinary(double num){
        return Double.parseDouble(Integer.toBinaryString((int) num));
    }

    private double toDecimal(double num){
        return Integer.parseInt(String.valueOf((int)num),2);
    }

    private double mod(double x, double y)
    {
        int result = (int) (x % y);
        return result < 0? result + y : result;
    }

    private boolean isOperator(char c){
        char operator[] = { '+', '-', '*', '/', '^', 'm', '~', 's', 'c', 't', 'l', 'b', 'd', '@', '!', '%', ')', '('};

        Arrays.sort(operator);
        return Arrays.binarySearch(operator, c) <= -1;
    }
    private int priority(char c){
        switch (c) {
            case '+' : case '-' : return 1;
            case '*' : case '/' : return 2;
            case '~' : return 3;
            case '@' : case '!' : case '^' : case 'm' : return 4;
            case 's' : case 'c' : case 't' : case 'l' : case 'b' : case 'd' : return 5;
        }
        return 0;
    }

    private boolean isOneMath(char c){
        char operator[] = { 's', 'c', 'l', 'b', 'd', 't', '@', '('};
        Arrays.sort(operator);
        return Arrays.binarySearch(operator, c) > -1;
    }

    private String standardize(String s){
        StringBuilder s1 = new StringBuilder();
        s = s.trim();
        s = s.replaceAll("\\s+"," ");
        int open = 0, close = 0;
        for (int i=0; i<s.length(); i++){
            char c = s.charAt(i);
            if (c == '(') open++;
            if (c == ')') close++;
        }

        StringBuilder sBuilder = new StringBuilder(s);

        for (int i = 0; i< (open - close); i++)
            sBuilder.append(')');

        s = sBuilder.toString();

        for (int i = 0; i < (close - open); i++) // auto remove ")"
            s=s.substring(0, s.indexOf(")"));

        for (int i=0; i<s.length(); i++){
            if (i>0 && isOneMath(s.charAt(i)) && (s.charAt(i-1) == ')' || isNum(s.charAt(i-1))))
                s1.append("*");
            else if (i>0 && (isNum(s.charAt(i-1)) || s.charAt(i-1) == ')') && (isCharPi(s.charAt(i)) || isCharE(s.charAt(i)))) s1.append("*").append(s.charAt(i));
            else s1.append(s.charAt(i));
        }
        return s1.toString();
    }

    String[] processString(String sMath){ //
        StringBuilder s1 = new StringBuilder();
        String elementMath[];
        sMath = standardize(sMath);
        for (int i=0; i<sMath.length(); i++){
            char c = sMath.charAt(i);
            if (i<sMath.length()-1 && (isCharPi(c) || isCharE(c)) && isOperator(sMath.charAt(i + 1))){
                check_error = true;
                return null;
            }
            else
            if (isOperator(c))
                s1.append(c);
            else s1.append(" ").append(c).append(" ");
        }
        s1 = new StringBuilder(s1.toString().trim());
        s1 = new StringBuilder(s1.toString().replaceAll("\\s+", " "));
        elementMath = s1.toString().split(" ");
        return elementMath;
    }

    String[] postfix(String[] elementMath){
        StringBuilder s1 = new StringBuilder();
        Stack <String> S = new Stack<>();
        for (String anElementMath : elementMath) {
            char c = anElementMath.charAt(0);

            if (isOperator(c))
                s1.append(anElementMath).append(" ");
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
                        while (!S.isEmpty() && priority(S.peek().charAt(0)) >= priority(c))
                            s1.append(S.pop()).append(" ");
                        S.push(anElementMath);
                    }
                }
            }
        }
        while (!S.isEmpty()) s1.append(S.pop()).append(" ");
        return s1.toString().split(" ");
    }

    String valueMath(String[] elementMath){
        Stack <Double> S = new Stack<>();
        double num = 0.0;
        for (String anElementMath : elementMath) {
            char c = anElementMath.charAt(0);
            if (isCharPi(c)) S.push(Math.PI);
            else if (isCharE(c)) S.push(Math.E);
            else {
                if (isOperator(c)) S.push(Double.parseDouble(anElementMath));
                else {

                    double num1 = S.pop();
                    switch (c) {
                        case '~':
                            num = -num1;
                            break;
                        case 's':
                            num = Math.sin(num1);
                            break;
                        case 'l':
                            num = Math.log(num1);
                            break;
                        case 'b':
                            num = toBinary(num1);
                            break;
                        case 'd':
                            num = toDecimal(num1);
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
                        case '@': {
                            if (num1 >= 0) {
                                num = Math.sqrt(num1);
                                break;
                            } else check_error = true;
                        }
                        case '!': {
                            if (num1 >= 0 && (int) num1 == num1) {
                                num = 1;
                                for (int j = 1; j <= (int) num1; j++) num = num * j;
                            } else check_error = true;
                        }
                        default:
                            break;
                    }
                    if (!S.empty()) {
                        double num2 = S.peek();
                        switch (c) {
                            case '+':
                                num = num2 + num1;
                                S.pop();
                                break;
                            case '-':
                                num = num2 - num1;
                                S.pop();
                                break;
                            case '*':
                                num = num2 * num1;
                                S.pop();
                                break;
                            case '/': {
//                                if (num1 != 0) num = num2 / num1;
//                                else check_error = true;
                                num = num2 / num1;
                                S.pop();
                                break;
                            }
                            case '^':
                                num = Math.pow(num2, num1);
                                S.pop();
                                break;
                            case 'm':
                                num = mod(num2, num1);
                                S.pop();
                                break;
                        }
                    }
                    S.push(num);
                }
            }
        }
        return NumToString(S.pop());
    }
}