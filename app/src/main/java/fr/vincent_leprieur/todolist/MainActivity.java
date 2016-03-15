package fr.vincent_leprieur.todolist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity {

    private static final int ACT_ADD_ITEM = 1;
    private static final String SORTED = "sort";
    private SimpleCursorAdapter dataAdapter;
    private ListView lv;
    private boolean sorted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = ((ListView) findViewById(R.id.todolist));

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        sorted = preferences.getBoolean(SORTED, false);

        displayItems();
    }

    private void displayItems() {
        Cursor cursor;

        cursor = TodoBase.fetchAllItems(this, sorted);
        dataAdapter = new SimpleCursorAdapter(this, R.layout.row, cursor,
                                              new String [] {TodoBase.KEY_LABEL},
                                              new int [] {R.id.label},
                                              0);
        lv.setAdapter(dataAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_sort);
        item.setTitle((sorted)? R.string.not_sort : R.string.sort);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.db_debug) {
            Intent dbManager = new Intent(this,AndroidDatabaseManager.class);
            startActivity(dbManager);
        }

        if (id == R.id.add_item) {
            Intent addItem = new Intent(this, AddItemActivity.class);
            startActivityForResult(addItem, ACT_ADD_ITEM);
        }

        if (id == R.id.menu_sort) {
            sorted = !sorted;
            displayItems();
        }

        if(id == R.id.db_delete){

        }

        if (id == R.id.menu_propos) {
           // Intent a_propos = new Intent(this, .class);
            //startActivityForResult(addItem, ACT_ADD_ITEM);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACT_ADD_ITEM:
                // Cursor is not dynamic, we have to fetch data again
                // Not optimal, but a dynamic cursor requires a CursorLoader,
                // which requires a ContentProvider... More work, more difficult
                displayItems();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putBoolean(SORTED, sorted);
        editor.commit();
    }
}
