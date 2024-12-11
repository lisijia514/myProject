package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;


import com.example.noteapp.adapter.MyAdapter;
import com.example.noteapp.bean.Note;
import com.example.noteapp.util.SpfUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mBtnAdd;
    private List<Note> mNotes;
    private MyAdapter mMyAdapter;
    private NoteDbOpenHelper mNoteDbOpenHelper=new NoteDbOpenHelper(this);
    public static final int MODE_LIENAR=0;
    public static final int MODE_GRID=1;
    public static final String KEY_LAYOUT_MODE="key_layout_mode";
    private int currentListLayoutMode = MODE_LIENAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intView();
        initData();
        intEvent();
    }

    @Override
    protected void onResume(){

        super.onResume();
        refreshDataFromDB();


    }
//默认值为列表布局
    private void setListLayout() {
        currentListLayoutMode=SpfUtil.getIntWithDefault(this,KEY_LAYOUT_MODE,MODE_LIENAR);
        if(currentListLayoutMode==MODE_LIENAR){
            setToLinearList(); mMyAdapter.notifyDataSetChanged();
        }else{
            setToGridList(); mMyAdapter.notifyDataSetChanged();
        }
    }

    private void setToGridList() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_GRID_LAYOUT);
    }

    private void setToLinearList() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_LINEAR_LAYOUT);
    }

    private void refreshDataFromDB() {
        mNotes=getDataFromDB();
        mMyAdapter.refreshData(mNotes);
    }

    private void intEvent() {
        mMyAdapter=new MyAdapter(this,mNotes);
        mRecyclerView.setAdapter(mMyAdapter);

        RecyclerView.LayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_LINEAR_LAYOUT);


    }

    private void initData() {
        mNotes=new ArrayList<>();
        mNoteDbOpenHelper=new NoteDbOpenHelper(this);
        mNotes=getDataFromDB();

    }

    private List<Note> getDataFromDB() {
       return mNoteDbOpenHelper.queryAllFromDb();
    }

    private void intView() {
        mRecyclerView = findViewById(R.id.rlv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 创建 DividerItemDecoration 实例
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);

        // 将 DividerItemDecoration 添加到 RecyclerView
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void add(View view) {
        Intent intent=new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        SearchView searchView=(SearchView) menu.findItem(R.id.menu_serach).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mNotes=mNoteDbOpenHelper.queryFromDbByTitle(newText);
                mMyAdapter.refreshData(mNotes);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);

        int itemId = item.getItemId();
        if (itemId == R.id.menu_linear) {
//            RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
//            mRecyclerView.setLayoutManager(linearLayoutManager);
//            mMyAdapter.setViewType(MyAdapter.TYPE_LINEAR_LAYOUT);
            setToLinearList();
            mMyAdapter.notifyDataSetChanged();
            currentListLayoutMode=MODE_LIENAR;
            SpfUtil.saveInt(this,KEY_LAYOUT_MODE, String.valueOf(currentListLayoutMode));

            return true;
        } else if (itemId == R.id.menu_grid) {
//            RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
//            mRecyclerView.setLayoutManager(gridLayoutManager);
//            mMyAdapter.setViewType(MyAdapter.TYPE_GRID_LAYOUT);
            setToGridList();
            mMyAdapter.notifyDataSetChanged();
            currentListLayoutMode=MODE_GRID;
            SpfUtil.saveInt(this,KEY_LAYOUT_MODE, String.valueOf(currentListLayoutMode));

            return true;
        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(currentListLayoutMode==MODE_LIENAR){
            MenuItem item=menu.findItem(R.id.menu_linear);
            item.setChecked(true);
        }else {
            menu.findItem(R.id.menu_grid).setChecked(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }
}