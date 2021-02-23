package com.example.administrator.sd01;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 29648 on 2021/2/5.
 */

public class SQLHelper extends SQLiteOpenHelper {
    public SQLHelper(Context context) {
        super(context, "fyerp", null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //Enable foreign key constraints
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table commodity(\n" +
                "  comcode varchar2(25) primary key,\n" +
                "  comname varchar2(20) not null,\n" +
                "  comcolor varchar2(10) not null,\n" +
                "  comsize varchar2(10) not null,\n" +
                "  barcode varchar2(25) not null,\n" +
                "  comno varchar2(10) not null\n" +
                ");");
        db.execSQL("create table bar(\n" +
                "  barcode varchar2(25) primary key,\n" +
                "  comcode varchar2(25) references commodity(comcode) ON DELETE CASCADE\n" +
                ");");
        db.execSQL("create table stock(\n" +
                "  stonum varchar2(1000) primary key,\n" +
                "  comcode varchar2(10) references COMMODITY(comcode) ON DELETE CASCADE,\n" +
                "  stocode varchar2(10) not null\n" +
                ");");
        db.execSQL("create table stockspace(\n" +
                "  sspspace varchar2(10) primary key,\n" +
                "  comcode varchar2(10) references COMMODITY(comcode) ON DELETE CASCADE,\n" +
                "  stocode varchar2(10) not null\n" +
                ");");

        db.execSQL("insert into commodity values('w1','V领抽条半袖女套衫','藏青','XL 170/92A','123','E295D0103');");
        db.execSQL("insert into commodity values('w2','V领抽条半袖男套衫','白','XL 175/92A','456','E295D0104');");
        db.execSQL("insert into commodity values('w3','V领男套衫','黑','L 175/92A','789','E295D0105');");
        db.execSQL("insert into commodity values('w4','A领男衬衫','白','X 178/88A','112','E295A0115');");

        db.execSQL("insert into bar values('123','w1');");
        db.execSQL("insert into bar values('456','w2');");
        db.execSQL("insert into bar values('789','w3');");

        db.execSQL("insert into stock values('12','w1','SD010037');");
        db.execSQL("insert into stock values('210','w2','SD010038');");
        db.execSQL("insert into stock values('120','w3','SD010039');");
        db.execSQL("insert into stock values('114','w4','SD121212');");

        db.execSQL("insert into stockspace values('W-2-18','w1','SD010037');");
        db.execSQL("insert into stockspace values('Z-2-19','w2','SD010038');");
        db.execSQL("insert into stockspace values('Y-2-20','w3','SD010039');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
