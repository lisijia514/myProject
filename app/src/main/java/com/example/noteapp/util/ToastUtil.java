package com.example.noteapp.util;
//对于Toast的封装
import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void toastShort(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
