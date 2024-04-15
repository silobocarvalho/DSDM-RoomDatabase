package br.ufc.quixada.crudusingroomdatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.ufc.quixada.crudusingroomdatabase.dao.PersonDao;
import br.ufc.quixada.crudusingroomdatabase.database.AppDatabase;
import br.ufc.quixada.crudusingroomdatabase.model.Person;
import br.ufc.quixada.crudusingroomdatabase.recyclerview.Item;
import br.ufc.quixada.crudusingroomdatabase.recyclerview.ItemArrayAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton fabAddPerson;
    RecyclerView rv_itens;
    PersonDao personDao;
    List<Person> persons;
    ItemArrayAdapter itemArrayAdapter;
    ArrayList<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personDao = initiateDatabase();

        fabAddPerson = findViewById(R.id.fab_add);
        fabAddPerson.setOnClickListener(this);

        populateFromDatabase();

        recyclerViewStuff();

        swipeToDelete();
    }

    @NonNull
    private PersonDao initiateDatabase() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name")
                .enableMultiInstanceInvalidation()
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        return db.personDao();
    }

    private void swipeToDelete() {
        //Swipe to delete feature
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int itemPosition = viewHolder.getAdapterPosition();
                Person personToDelete = persons.get(itemPosition);
                Log.d("sid-tag", "Person to delete: " + personToDelete.toString() + "|| item position: " + itemPosition);
                //delete from database
                personDao.delete(personToDelete);

                Log.d("sid-tag", "Persons before: " + persons.size());
                //delete from array
                persons.remove(itemPosition);

                Log.d("sid-tag", "Persons after: " + persons.size());

                Log.d("sid-tag", "Itemlist before: " + itemList.size());
                //delete from RecyclerView list itens
                itemList.remove(itemPosition);
                Log.d("sid-tag", "Itemlist after: " + itemList.size());

                Log.d("sid-tag", "before populate: " + persons.size() + "|| itemlist: " + itemList.size());
                //To update array Persons with id generated in the database
                //populateFromDatabase();
                Log.d("sid-tag", "after populate: " + persons.size() + "|| itemlist: " + itemList.size());

                itemArrayAdapter.notifyItemRemoved(itemPosition);
                itemArrayAdapter.notifyItemRangeChanged(itemPosition, itemList.size());

            }
        }).attachToRecyclerView(rv_itens);
    }

    private void populateFromDatabase() {
        // Populating list items. Get data from Room Database.
        persons = personDao.getAll();
        itemList = new ArrayList<>();

        for (Person p: persons) {
            Item item = new Item(p.firstName, p.lastName);
            itemList.add(item);
        }
    }

    private void recyclerViewStuff() {
        // 1- AdapterView: ListView
        rv_itens = findViewById(R.id.rv_itens);

        itemArrayAdapter = new ItemArrayAdapter(R.layout.item_view, itemList);
        rv_itens = (RecyclerView) findViewById(R.id.rv_itens);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_itens.setLayoutManager(layoutManager);

        //Separate itens in the list
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                layoutManager.getOrientation());
        rv_itens.addItemDecoration(dividerItemDecoration);

        rv_itens.setAdapter(itemArrayAdapter);
    }

    @Override
    public void onClick(View v) {
        EditText et_first_name, et_last_name;
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);

        if(v == fabAddPerson){
            String firstName = et_first_name.getText().toString();
            String lastName = et_last_name.getText().toString();
            //Validate input data
            if(firstName.equals("") || lastName.equals("")){

                // Not valid
                Toast.makeText(this, "Insira os dados corretamente.", Toast.LENGTH_SHORT).show();

            }else{

                //Add new person
                Person novaPessoa = new Person(firstName, lastName);

                //Add database and RecyclerView list
                // It's necessary to get the ID generated by the database to be able to remove this object when swiped left
                long novaPessoaId = personDao.insert(novaPessoa);
                novaPessoa.uid = (int)novaPessoaId;
                Item novoItem = new Item(novaPessoa.firstName, novaPessoa.lastName);
                itemList.add(novoItem);

                //Update Person array
                persons.add(novaPessoa);

                //Atualiza view
                itemArrayAdapter.notifyItemInserted(itemList.size());

                //After add a new person, remove the text in the edittext
                et_first_name.setText("");
                et_last_name.setText("");

            }


        }
    }
}