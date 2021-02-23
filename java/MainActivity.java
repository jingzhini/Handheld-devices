package com.example.administrator.sd01;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_chose,btn_inquire;
    TextView txt_stock,txt_num,txt_name,txt_color,txt_size,txt1,txt2,txt3;
    EditText bar;
   /* ArrayList list=new ArrayList();
    ListView listView;*/
    TableLayout tl;
    TableLayout.LayoutParams tl_Wrap=new TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT
    );
    TableRow.LayoutParams row_MWrap=new TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
    );
    Chronometer ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //实例化控件
        btn_chose= (Button) findViewById(R.id.btn_chose);
        btn_inquire= (Button) findViewById(R.id.btn_inquire);

        txt_stock= (TextView) findViewById(R.id.txt_itemStock);
        txt_num= (TextView) findViewById(R.id.txt_itemNum);
        txt_name= (TextView) findViewById(R.id.txt_itemName);
        txt_color= (TextView) findViewById(R.id.txt_itemColor);
        txt_size= (TextView) findViewById(R.id.txt_itemSize);
        txt1= (TextView) findViewById(R.id.textView);
        txt2= (TextView) findViewById(R.id.textView2);
        txt3= (TextView) findViewById(R.id.textView3);

        bar= (EditText) findViewById(R.id.edt_barCode);

        ch= (Chronometer) findViewById(R.id.chronometer2);

        tl= (TableLayout) findViewById(R.id.tl_Item);


        //点击“选择仓库”按钮，搜索SQL‘SD01’开头的仓库代码，
        btn_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建或打开数据库
                SQLHelper helper=new SQLHelper(MainActivity.this);
                SQLiteDatabase db=helper.getReadableDatabase();

                final Cursor cursor = db.rawQuery("select * from stock where stocode like '%SD01%'",null);
                /*final String[] stockItem=new String[cursor.getColumnCount()];*/
                /*stockItem = (String[]) list.toArray(new Object[list.size()]);*/
                int i = 0;
                int num=cursor.getCount();
                final String[] stockItem=new String[num];
                /*String stocode="";*/
                if (cursor.moveToFirst()) {
                    do {
                        /*String stonum=cursor.getString(0);
                        String comcode=cursor.getString(1);*/
                        /*stocode = cursor.getString(2);
                        HashMap map = new HashMap();
                        map.put("stocode", stocode);
                        list.add(map);
                       stockItem= (String[]) list.get(2);*/

                        stockItem[i] = cursor.getString(2);
                        i+=1;
                    } while (cursor.moveToNext());
                }

                db.close();
                cursor.close();

                /*SimpleAdapter adapter=new SimpleAdapter(MainActivity.this,
                        list,
                        R.layout.layout,
                        new String[]{"stocode"},
                        new int[]{R.id.txt});
                listView.setAdapter(adapter);*/
                /*=new String[]{"stocode"}*/

                //将结果显示到AlertDialog中，
                AlertDialog.Builder choseStock=new AlertDialog.Builder(MainActivity.this);
                choseStock.setTitle("选择仓库代码：");
               /* choseStock.setMessage("one");*/
                choseStock.setCancelable(false);//控制是否可以点击提示框之外的界面，默认为true
                //点击选项，将所选显示到txt_stock中。
                choseStock.setItems(stockItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*Toast.makeText(MainActivity.this, "you choice:"+stockItem[which], Toast.LENGTH_SHORT).show();*/
                        txt_stock.setText(stockItem[which]);
                    }
                });

                /*choseStock.setCursor(cursor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "your choice:"+, Toast.LENGTH_SHORT).show();
                    }
                },stocode);*/

                /*choseStock.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });*/

                choseStock.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*Toast.makeText(MainActivity.this, "cancel!!!", Toast.LENGTH_SHORT).show();*/
                    }
                });
                choseStock.create().show();
                /*showChoseStock();*/

            }
        });

        //仓库代码为空，点击查询按钮，
        //显示所有SD01开头的仓库代码，货位，数量
        btn_inquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断TextView（editview）是否为空
                /*txt_stock.getText().toString().equals("")*/

                if (TextUtils.isEmpty(txt_stock.getText().toString())) {
                    SQLHelper helper=new SQLHelper(MainActivity.this);
                    SQLiteDatabase db=helper.getReadableDatabase();

                    Cursor cursor=db.rawQuery("select * from stock where stocode like '%SD01%'",null);
                    Cursor curSpace=db.rawQuery("select * from stockspace where stocode like '%SD01%'",null);
                    int i=0;
                    String[] stoCode=new String[cursor.getCount()];
                    String[] stoNum=new String[cursor.getCount()];
                    String[] space=new String[curSpace.getCount()];
                    if(cursor.moveToFirst()&&curSpace.moveToFirst()){
                        do {
                            stoCode[i]=cursor.getString(2);
                            stoNum[i]=cursor.getString(0);
                            space[i]=curSpace.getString(0);
                            i+=1;
                        }while (cursor.moveToNext()&&curSpace.moveToNext());
                    }
                    db.close();

                    //清除原来tablelayout中的所有row
                    /*tl.removeAllViews();*/

                    //移除掉第二三行：
                    /*tl.removeView(tl.getChildAt(2));
                    tl.removeView(tl.getChildAt(1));*/
                    /*tl.removeViews(1,2);*///从一开始，移除两个
                    clearList();

                    for (int x=0;x<cursor.getCount();x++){
                        TableRow row=new TableRow(MainActivity.this);

                        tl_Wrap.setMargins(2,0,2,1);
                        row.setLayoutParams(tl_Wrap);

                        ArrayList<TextView> tvArray=new ArrayList<TextView>();

                        for (int index=0;index<3;index++){
                            tvArray.add(new TextView(MainActivity.this));
                            tvArray.get(index).setTextSize(14);
                            tvArray.get(index).setBackgroundResource(R.drawable.txt_border);
                            switch (index){
                                case 0:
                                    tvArray.get(index).setText(stoCode[x]);
                                    break;
                                case 1:
                                    tvArray.get(index).setText(space[x]);
                                    break;
                                case 2:
                                    tvArray.get(index).setText(stoNum[x]);
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            tvArray.get(index).setGravity(Gravity.CENTER);
                            tvArray.get(index).setPadding(0,5,0,5);
                            tvArray.get(index).setTypeface(Typeface.DEFAULT_BOLD);
                            tvArray.get(index).setLayoutParams(row_MWrap);
                            row.addView(tvArray.get(index));
                        }

                        tl.addView(row);
                        cursor.close();
                        curSpace.close();
                    }
                }
                else if (!TextUtils.isEmpty(txt_stock.getText().toString())&&!TextUtils.isEmpty(bar.getText().toString())){
                    SQLHelper helper=new SQLHelper(MainActivity.this);
                    SQLiteDatabase db=helper.getReadableDatabase();

                    String[] strBar=new String[1];
                    strBar[0]=bar.getText().toString();

                    Cursor curOther=db.rawQuery("select * from bar where barcode=?",strBar);
                    String[] comCode=new String[1];
                    if (curOther.moveToFirst()){
                        comCode[0]=curOther.getString(1);
                    }

                    Cursor curMain=db.rawQuery("select * from commodity where comcode=?",comCode);
                    Cursor cursor=db.rawQuery("select * from stock where comcode=?",comCode);
                    Cursor curSpace=db.rawQuery("select * from stockspace where comcode=?",comCode);

                    if (curMain.moveToFirst()&cursor.moveToFirst()&curSpace.moveToFirst()){
                        txt_num.setText(curMain.getString(5));
                        txt_name.setText(curMain.getString(1));
                        txt_color.setText(curMain.getString(2));
                        txt_size.setText(curMain.getString(3));
                        txt1.setText(cursor.getString(2));
                        txt2.setText(curSpace.getString(0));
                        txt3.setText(cursor.getString(0));
                    }

                    db.close();
                    curMain.close();
                    curOther.close();
                    cursor.close();
                    curSpace.close();

                    clearList();

                    TableRow row=new TableRow(MainActivity.this);
                    tl_Wrap.setMargins(2,0,2,1);
                    row.setLayoutParams(tl_Wrap);

                    final ArrayList<TextView> tvArray=new ArrayList<TextView>();
                    for (int index=0;index<3;index++){
                        tvArray.add(new TextView(MainActivity.this));
                        tvArray.get(index).setTextSize(14);
                        tvArray.get(index).setBackgroundResource(R.drawable.txt_border);
                        switch (index){
                            case 0:
                                tvArray.get(index).setText(txt1.getText());
                                break;
                            case 1:
                                tvArray.get(index).setText(txt2.getText());
                                break;
                            case 2:
                                tvArray.get(index).setText(txt3.getText());
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        tvArray.get(index).setGravity(Gravity.CENTER);
                        tvArray.get(index).setPadding(0,5,0,5);
                        tvArray.get(index).setTypeface(Typeface.DEFAULT_BOLD);
                        tvArray.get(index).setLayoutParams(row_MWrap);
                        row.addView(tvArray.get(index));
                    }

                    tl.addView(row);

                    //记录当前时间，3秒后清空
                    final long beginTime=SystemClock.elapsedRealtime();
                    ch.start();

                    ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                        @Override
                        public void onChronometerTick(Chronometer chronometer) {
                            if (SystemClock.elapsedRealtime()-beginTime>=3000){
                                ch.stop();

                                tvArray.get(0).setText("");
                                tvArray.get(1).setText("");
                                tvArray.get(2).setText("");
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(MainActivity.this, "请输入仓库代码或条形码！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clearList(){
        int j=tl.getChildCount();
        if (j>1){
            for (int clear=j;clear>0;clear--){
                tl.removeView(tl.getChildAt(clear));
            }
        }
    }
}
