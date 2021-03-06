package com.example.foodify.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.*;

import com.example.foodify.Enums.FoodStyle;

import java.util.Date;


/**
 * @author jentevandersanden
 * Class that represents the row UserEntity in the table Users */
@Entity(tableName = "Users")
public class UserEntity {

    /** Columns */
    @PrimaryKey (autoGenerate = true)
    @NonNull
    private int ID;

    @ColumnInfo(name = "firstname")
    private String firstname;

    @ColumnInfo(name = "lastname")
    private String lastname;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password")
    private String password;

//    @ColumnInfo(name = "birthday")
//    private Date birthday;
//
//    @ColumnInfo(name = "foodstyle")
//    private FoodStyle foodstyle;

    @ColumnInfo(name = "address")
    private String address;

    /**
     * GETTERS AND SETTERS
     */
    @NonNull
    public int getID() {
        return ID;
    }

    public void setID(@NonNull int ID) {
        this.ID = ID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //    public Date getBirthday() {
//        return birthday;
//    }
//
//    public void setBirthday(Date birthday) {
//        this.birthday = birthday;
//    }
//
//    public FoodStyle getFoodstyle() {
//        return foodstyle;
//    }
//
//    public void setFoodstyle(FoodStyle foodstyle) {
//        this.foodstyle = foodstyle;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
