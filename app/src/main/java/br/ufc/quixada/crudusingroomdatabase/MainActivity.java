package br.ufc.quixada.crudusingroomdatabase;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;

import br.ufc.quixada.crudusingroomdatabase.dao.PersonDao;
import br.ufc.quixada.crudusingroomdatabase.database.AppDatabase;
import br.ufc.quixada.crudusingroomdatabase.model.Person;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Person p1 = new Person("Sidartha", "Carvalho");

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name")
                .enableMultiInstanceInvalidation()
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        PersonDao personDao = db.personDao();

        personDao.insertAll(p1);

        List<Person> persons = personDao.getAll();

        for (Person p: persons) {
            Log.d("sid-tag", p.toString());
        }

    }
}