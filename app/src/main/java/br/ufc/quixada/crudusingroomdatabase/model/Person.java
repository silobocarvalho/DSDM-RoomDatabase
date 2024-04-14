package br.ufc.quixada.crudusingroomdatabase.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import javax.annotation.processing.Generated;

@Entity(tableName = "person")
public class Person {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    public Person(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @NonNull
    @Override
    public String toString() {
        return this.uid + " | " + this.firstName + " | " + this.lastName;
    }
}
