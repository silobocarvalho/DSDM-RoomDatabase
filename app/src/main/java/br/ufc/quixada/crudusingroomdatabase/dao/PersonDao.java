package br.ufc.quixada.crudusingroomdatabase.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import br.ufc.quixada.crudusingroomdatabase.model.Person;

@Dao
public interface PersonDao {
    @Query("SELECT * FROM person")
    List<Person> getAll();

    @Query("SELECT * FROM person WHERE uid IN (:userIds)")
    List<Person> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM person WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    Person findByName(String first, String last);

    @Insert
    void insertAll(Person... users);

    @Delete
    void delete(Person user);

}
