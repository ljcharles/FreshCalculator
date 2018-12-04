package freshloic.fr.freshcalculator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

public class HistoryActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    FloatingActionButton add_data;
    EditText add_name;
    ListView calculList;

    ArrayList<Calcul> listItem;
    CalculListAdapter adapter;
    static int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.setConfigTitleBar(getSupportActionBar());

        setContentView(R.layout.activity_history);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "Lato-Regular.ttf",true);

        databaseHelper = new DatabaseHelper(this);

        listItem = new ArrayList<>();

        add_data = findViewById(R.id.add_data);
        add_name = findViewById(R.id.add_name);
        calculList = findViewById(R.id.calcul_list);

        viewData();

        calculList.setOnItemClickListener((adapterView, view, position, l) -> {
            final Calcul calcul = (Calcul)adapterView.getItemAtPosition(position);
            String name = String.valueOf(position + 1);
            Toast.makeText(HistoryActivity.this, "Calcul n° " + name + " sélectionné", Toast.LENGTH_SHORT).show();

            TextView expression = view.findViewById(R.id.textView1);
            TextView resultat = view.findViewById(R.id.textView2);
            // TextView categorie = view.findViewById(R.id.textView4);

            ImageButton imageDelete = view.findViewById(R.id.imageDelete);
            imageDelete.setOnClickListener((View view1) -> {

            if (databaseHelper.deleteData(calcul.getDateAjout(), false)) {
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
                Toast.makeText(HistoryActivity.this, "Suppression...", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(HistoryActivity.this, "Echec Suppression", Toast.LENGTH_SHORT).show();
            });

            ImageButton imageRevert = view.findViewById(R.id.imageReverse);
            imageRevert.setOnClickListener(view1 -> {
                Intent editScreen = new Intent(getApplicationContext(), MainActivity.class);
                editScreen.putExtra("expression",expression.getText().toString());
                editScreen.putExtra("resultat",resultat.getText().toString());
                editScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(editScreen);
            });

            MySpinner spinner = view.findViewById(R.id.spinner1);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String msupplier = spinner.getSelectedItem().toString();
                    Toast.makeText(getApplicationContext(), "Selected item : " + msupplier, Toast.LENGTH_SHORT).show();

                    if (databaseHelper.updateCategorieCalcul(calcul.getDateAjout(), msupplier)){
                        adapter.notifyDataSetChanged();
                        Toast.makeText(HistoryActivity.this, "Mise à jour réussi", Toast.LENGTH_SHORT).show();

                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }else
                        Toast.makeText(HistoryActivity.this, "Echec Mise à jour", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        });


        add_data.setOnClickListener(view -> {
            String name = add_name.getText().toString();

            if (Utils.isNotEmptyString(name) && databaseHelper.insertCategories(name)){
                Toast.makeText(HistoryActivity.this, "Data Added", Toast.LENGTH_SHORT).show();

                add_name.setText("");
                listItem.clear();
                viewData();
            }else {
                Toast.makeText(HistoryActivity.this, "Data not Added", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void viewData() {
        Cursor cursor = databaseHelper.viewData();

        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else {
            adapter = new CalculListAdapter(this,R.layout.adapter_view_layout, listItem);
            id++;
            while (cursor.moveToNext()){
                Calcul calcul = new Calcul(
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                listItem.add(calcul); // 2 index result 1 index expression, 0 index id
            }
            calculList.setAdapter(adapter);
        }
        cursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem searchItem = menu.findItem(R.id.recherche);
        MenuItem deleteItem = menu.findItem(R.id.supressionHisto);

        deleteItem.setOnMenuItemClickListener(menuItem -> {
            if (databaseHelper.deleteData(null,true)){
                Toast.makeText(HistoryActivity.this, "Supprimer", Toast.LENGTH_SHORT).show();
                listItem.clear();
                for (int position = 0; position < adapter.getCount(); position++) {
                    adapter.remove(adapter.getItem(position));
                }
                adapter.notifyDataSetChanged();
                id = 0;
            }
            else Toast.makeText(HistoryActivity.this, "Non Supprimer", Toast.LENGTH_SHORT).show();
            return true;
        });

        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                Cursor cursor = databaseHelper.searchCalcul(s);

                if (cursor.getCount() != 0){
                    listItem.clear();
                    adapter = new CalculListAdapter(HistoryActivity.this,R.layout.adapter_view_layout, listItem);
                    while (cursor.moveToNext()){
                        Calcul calcul = new Calcul(
                                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                        listItem.add(calcul); // 1 index name, 0 index id
                    }
                    adapter.notifyDataSetChanged();
                    calculList.setAdapter(adapter);
                }
                cursor.close();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}
