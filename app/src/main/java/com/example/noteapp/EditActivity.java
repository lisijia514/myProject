package com.example.noteapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.noteapp.bean.Note;
import com.example.noteapp.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private EditText etTitle,etContent;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    private Note note;
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_layout);
        etContent=findViewById(R.id.et_content);
        etTitle=findViewById(R.id.et_title);
        intData();
    }

    private void intData() {
        Intent intent=getIntent();
        note=(Note) intent.getSerializableExtra("note");
        if(note !=null){
            etTitle.setText(note.getTitle());
            etContent.setText(note.getContent());
        }
        mNoteDbOpenHelper=new NoteDbOpenHelper(this);
    }


    public void save(View view) {
        String title=etTitle.getText().toString();
        String content=etContent.getText().toString();
        if(TextUtils.isEmpty(title)){
            ToastUtil.toastShort(this,"标题不能为空！");
            return;
        }
//        Note note=new Note();
        if(note !=null && !TextUtils.isEmpty(note.getId())){
            note.setCreatedTime(getCurrentTimeFormat());
            note.setTitle(title);
            note.setContent(content);
            long rowId=mNoteDbOpenHelper.updateData(note);
            if(rowId!=-1){
                ToastUtil.toastShort(this,"修改成功！");
                this.finish();
            }else{
                ToastUtil.toastShort(this,"修改失败！");
            }
        }else {
            ToastUtil.toastShort(this,"笔记ID或笔记不存在");
        }

    }

    private String getCurrentTimeFormat(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date=new Date();
        return simpleDateFormat.format(date);
    }
}
