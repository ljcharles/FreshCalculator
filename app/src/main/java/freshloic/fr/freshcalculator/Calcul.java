package freshloic.fr.freshcalculator;

class Calcul {
    private String expression;
    private String result;
    private String dateAjout;
    private String category;

    Calcul( String expression, String result, String dateAjout, String category) {
        setExpression(expression);
        setResult(result);
        setDateAjout(dateAjout);
        setCategory(category);
    }

    String getExpression() {
        return expression;
    }

    private void setExpression(String expression) {
        this.expression = expression;
    }

    String getResult() {
        return result;
    }

    private void setResult(String result) {
        this.result = result;
    }

    String getDateAjout() {
        return dateAjout;
    }

    private void setDateAjout(String dateAjout) {
        this.dateAjout = dateAjout;
    }

    String getCategory() {
        return category;
    }

    private void setCategory(String category) {
        this.category = category;
    }
}
