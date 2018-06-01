package com.example.amaury.scoreapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ben on 05/05/18.
 */

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABSE_NAME = "ScoreApp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, DATABSE_NAME, null, DATABASE_VERSION);
    }

    // Called the first time the app is used on the device
    @Override
    public void onCreate(SQLiteDatabase db) {
        String countriesSql = "CREATE TABLE Countries ("
                            + "     id_country INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "     name TEXT NOT NULL"
                            + ")";

        String champSQL = "CREATE TABLE Champs ("
                        + "     id_champ INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "     name TEXT NOT NULL,"
                        + "     id_country INTEGER"
                        + ")";

        String clubSql = "CREATE TABLE Clubs ("
                        + "     id_club INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "     name TEXT NOT NULL,"
                        + "     id_country INTEGER"
                        + ")";

        String matchSql = "CREATE TABLE Matches ("
                        + "     id_match INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "     id_champ INTEGER,"
                        + "     id_dom INTEGER,"
                        + "     id_for INTEGER,"
                        + "     score_dom INTEGER,"
                        + "     score_for INTEGER,"
                        + "     date DATE"
                        + ")";

        String linkChampClubSql = "CREATE TABLE LinkChampClub ("
                                + "     id_link INTEGER PRIMARY KEY AUTOINCREMENT,"
                                + "     id_champ INTEGER,"
                                + "     id_club INTEGER"
                                + ")";

        db.execSQL(countriesSql);
        db.execSQL(champSQL);
        db.execSQL(clubSql);
        db.execSQL(matchSql);
        db.execSQL(linkChampClubSql);
        Log.i("DATABASE", "onCreate invoked");

    }


    /* Method called when a change has been made in the STRUCTURE of the database (by comparing
       the numbers of the versions (old and new)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void insertCountry(String name){
        name = name.replace("'", "''");
        String strQSL = "INSERT INTO Countries (name) VALUES ('"
                        + name + "')";

        this.getWritableDatabase().execSQL(strQSL);
        Log.i("DATABASE", "insert Country");
    }
}
