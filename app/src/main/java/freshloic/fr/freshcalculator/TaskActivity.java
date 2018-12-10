package freshloic.fr.freshcalculator;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        databaseHelper = DatabaseHelper.getInstance(this);
        listView = findViewById(R.id.listTask);

        loadTaskList();
    }

    private void loadTaskList() {
        ArrayList<String> taskList = databaseHelper.getTaskList();
        if (arrayAdapter == null){
            arrayAdapter = new ArrayAdapter<>(this,R.layout.row, R.id.task_title,taskList);
            listView.setAdapter(arrayAdapter);
        }else {
            arrayAdapter.clear();
            arrayAdapter.addAll(taskList);
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.add_note:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Ajouter une nouvelle note")
                        .setMessage("Noter votre idée ici")
                        .setView(taskEditText)
                        .setPositiveButton("Ajouter", (dialogInterface, i) -> {
                            String task = taskEditText.getText().toString();
                            databaseHelper.insertTasks(task);
                            loadTaskList();
                        })
                        .setNegativeButton("Annuler", null)
                        .create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteTask(View view){
        TextView taskTextView = findViewById(R.id.task_title);
        String task = taskTextView.getText().toString();
        databaseHelper.deleteTasks(task);
        loadTaskList();
    }
}
