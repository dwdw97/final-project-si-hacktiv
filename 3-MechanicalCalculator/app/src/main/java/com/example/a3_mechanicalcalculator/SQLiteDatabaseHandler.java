package com.example.a3_mechanicalcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    //Database version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private static final String DATABASE_NAME = "Calculator";
    //Country table name
    private static final String TABLE_NAME = "Calculation";

    //Country Table Columns names
    private static final String KEY_ID = "id";
    private static final String ATTRIBUTE_1 = "calculationStr";
    private static final String ATTRIBUTE_2 = "lastDigit";
    private static final String ATTRIBUTE_3 = "result";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ATTRIBUTE_1
                + " TEXT, " + ATTRIBUTE_2 + " TEXT, " + ATTRIBUTE_3 + " DOUBLE)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addCalculation(Calculation calculation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_1, calculation.getCalculationStr());
        values.put(ATTRIBUTE_2, calculation.getLastDigit());
        values.put(ATTRIBUTE_3, calculation.getResult());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    Calculation getCalculation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {KEY_ID, ATTRIBUTE_1, ATTRIBUTE_2, ATTRIBUTE_3}, KEY_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor!=null)
            cursor.moveToFirst();

        Calculation calculation = new Calculation(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3));
        return calculation;
    }

    public List<Calculation> getAllCalculation() {
        List<Calculation> calculationList = new ArrayList<Calculation>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all rows and adding to list
        if(cursor.moveToFirst()) {
            do {
                Calculation calculation = new Calculation();
                calculation.setId(cursor.getInt(0));
                calculation.setCalculationStr(cursor.getString(1));
                calculation.setLastDigit(cursor.getString(2));
                calculation.setResult(cursor.getDouble(3));

                calculationList.add(calculation);
            } while(cursor.moveToNext());
        }

        return calculationList;
    }

    public int updateCalculation(Calculation calculation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ATTRIBUTE_1, calculation.getCalculationStr());
        values.put(ATTRIBUTE_2, calculation.getLastDigit());
        values.put(ATTRIBUTE_3, calculation.getResult());

        return db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] {String.valueOf(calculation.getId()) });
    }

    public void deleteCalculation(Calculation calculation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] {String.valueOf(calculation.getId()) });
        db.close();
    }

    public void deleteAllCalculation() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    public int getCalculationCount() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
