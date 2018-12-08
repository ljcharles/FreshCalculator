package freshloic.fr.freshcalculator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

class CalculListAdapter extends ArrayAdapter<Calcul> {

    private final Context mcontext;
    private final int mresource;
    private int lastPosition = -1;

    static class ViewHolder{
        TextView category;
        TextView dateAjout;
        TextView expression;
        TextView resultat;
    }

    CalculListAdapter(Context context, ArrayList<Calcul> objects) {
        super(context, R.layout.adapter_view_layout, objects);
        this.mcontext = context;
        this.mresource = R.layout.adapter_view_layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String expression = Objects.requireNonNull(getItem(position)).getExpression();
        String result = Objects.requireNonNull(getItem(position)).getResult();
        String dateAjout = Objects.requireNonNull(getItem(position)).getDateAjout();
        String category = Objects.requireNonNull(getItem(position)).getCategory();

        Calcul calcul = new Calcul(expression,result,dateAjout, category);
        final View resultView;
        ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
            convertView = layoutInflater.inflate(mresource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.expression = convertView.findViewById(R.id.textView1);
            viewHolder.resultat = convertView.findViewById(R.id.textView2);
            viewHolder.dateAjout = convertView.findViewById(R.id.textView3);
            viewHolder.category = convertView.findViewById(R.id.textView4);

            resultView = convertView;

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            resultView = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mcontext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        resultView.startAnimation(animation);
        lastPosition = position;

        viewHolder.resultat.setText(calcul.getResult());
        viewHolder.expression.setText(calcul.getExpression());
        viewHolder.dateAjout.setText(calcul.getDateAjout());
        viewHolder.category.setText(calcul.getCategory());


        return convertView;
    }
}
