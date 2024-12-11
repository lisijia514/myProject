package com.example.noteapp.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.EditActivity;
import com.example.noteapp.NoteDbOpenHelper;
import com.example.noteapp.R;
import com.example.noteapp.bean.Note;
import com.example.noteapp.util.ToastUtil;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Note> mBeanList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    private int viewType;

    public static int TYPE_LINEAR_LAYOUT=0;
    public static int TYPE_GRID_LAYOUT=1;

    public MyAdapter(Context context, List<Note> mBeanList){
        this.mBeanList=mBeanList;
        this.mContext=context;
        mLayoutInflater=LayoutInflater.from(mContext);
        mNoteDbOpenHelper=new NoteDbOpenHelper(mContext);

    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mTvTitle=itemView.findViewById(R.id.tv_title);
            this.mTvContent=itemView.findViewById(R.id.tv_content);
            this.mTvTime=itemView.findViewById(R.id.tv_time);
            this.rlContainer=itemView.findViewById(R.id.rl_item_container);
        }
    }

    public class MyGridViewHolder extends RecyclerView.ViewHolder {
        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;

        public MyGridViewHolder(@NonNull View itemView) {
            super(itemView);

            this.mTvTitle=itemView.findViewById(R.id.tv_title);
            this.mTvContent=itemView.findViewById(R.id.tv_content);
            this.mTvTime=itemView.findViewById(R.id.tv_time);
            this.rlContainer=itemView.findViewById(R.id.rl_item_container);
        }
    }

    @NonNull
    @Override
    public  RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_LINEAR_LAYOUT){
            View view=mLayoutInflater.inflate(R.layout.list_item_layout,parent,false);
            MyViewHolder myViewHolder=new MyViewHolder(view);

            return myViewHolder;
        } else if (viewType==TYPE_GRID_LAYOUT) {
            View view=mLayoutInflater.inflate(R.layout.list_item_grid_layout,parent,false);
            MyGridViewHolder myGridViewHolder=new MyGridViewHolder(view);

            return myGridViewHolder;
        }
        // 默认返回一个 MyViewHolder
        View defaultView = mLayoutInflater.inflate(R.layout.list_item_layout, parent, false);
        return new MyViewHolder(defaultView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
           if(holder ==null){
               return ;
           } else if (holder instanceof MyViewHolder) {
               bindMyViewHolder((MyViewHolder) holder,position);
           }else if(holder instanceof MyGridViewHolder){
                bindMyGridViewHolder((MyGridViewHolder) holder,position);
           }

    }

    private void bindMyViewHolder(MyViewHolder holder,int position){
        Note note=mBeanList.get(position);

        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContent());
        holder.mTvTime.setText(note.getCreatedTime());
        holder.rlContainer.setOnClickListener(view -> {
            Intent intent=new Intent(mContext, EditActivity.class);
            intent.putExtra("note",note);
            mContext.startActivity(intent);
        });

        holder.rlContainer.setOnLongClickListener(v -> {
            //长按弹出弹窗：删除或者编辑
            Dialog dialog=new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
            View view =mLayoutInflater.inflate(R.layout.list_item_dialog_layout,null);

            TextView tvDelete=view.findViewById(R.id.tv_delete);
            TextView tvEdit=view.findViewById(R.id.tv_edit);

            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int row=mNoteDbOpenHelper.deleteData(note.getId());
                    if(row>0){
                        removeData(position);

                        ToastUtil.toastShort(mContext,"删除成功");
                    }else {
                        ToastUtil.toastShort(mContext,"删除失败");
                    }
                    dialog.dismiss();

                }
            });
            tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mContext,EditActivity.class);
                    intent.putExtra("note",note);
                    mContext.startActivity(intent);
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
            dialog.show();

            return false;
        });


    }
    private void bindMyGridViewHolder(MyGridViewHolder holder,int position){
        Note note=mBeanList.get(position);

        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContent());
        holder.mTvTime.setText(note.getCreatedTime());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, EditActivity.class);
                intent.putExtra("note",note);
                mContext.startActivity(intent);
            }
        });
        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                //长按弹出弹窗：删除或者编辑
                Dialog dialog=new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                View view =mLayoutInflater.inflate(R.layout.list_item_dialog_layout,null);

                TextView tvDelete=view.findViewById(R.id.tv_delete);
                TextView tvEdit=view.findViewById(R.id.tv_edit);

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int row=mNoteDbOpenHelper.deleteData(note.getId());
                        if(row>0){
                            removeData(position);

                            ToastUtil.toastShort(mContext,"删除成功");
                        }else {
                            ToastUtil.toastShort(mContext,"删除失败");
                        }
                        dialog.dismiss();

                    }
                });
                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(mContext,EditActivity.class);
                        intent.putExtra("note",note);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(view);
                dialog.show();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mBeanList.size();
    }
    public void refreshData(List<Note> notes){
        this.mBeanList=notes;
        notifyDataSetChanged();
    }
    public void removeData(int pos){
        mBeanList.remove(pos);
        notifyItemRemoved(pos);
    }

}
