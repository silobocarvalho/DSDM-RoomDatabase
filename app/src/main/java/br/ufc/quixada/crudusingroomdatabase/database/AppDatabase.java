package br.ufc.quixada.crudusingroomdatabase.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import br.ufc.quixada.crudusingroomdatabase.dao.PersonDao;
import br.ufc.quixada.crudusingroomdatabase.model.Person;

@Database(entities = {Person.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
}
