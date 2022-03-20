package com.example.chatappdemo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatappdemo.Activities.GroupMessageActivity;
import com.example.chatappdemo.Activities.MessageActivity;
import com.example.chatappdemo.Model.GroupList;
import com.example.chatappdemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

    private Context mContext;
    private List<GroupList> mGroupLists;
    FirebaseUser fuser;
    public GroupListAdapter(Context mContext, List<GroupList> mGroupLists) {
        this.mContext = mContext;
        this.mGroupLists = mGroupLists;
        fuser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new GroupListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final GroupList groupList = mGroupLists.get(position);
        holder.username.setText(groupList.getGroupname());
        if (groupList.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(groupList.getImageURL()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupMessageActivity.class);
                intent.putExtra("groupid", groupList.getGroupId());
               // intent.putExtra("chatType", "groupChat");
                mContext.startActivity(intent);
            }
        });

        if(fuser.getUid().equals(groupList.getCreatormember())) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setTitle("Delete")
                            .setMessage("Are you sure")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    database.getReference().child("grouplist").child(groupList.getGroupId()).removeValue();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mGroupLists.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;
        private ImageView img_select;
        public ViewHolder(View itemView) {
            super(itemView);


            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
            img_select = itemView.findViewById(R.id.img_select);
        }
    }
}
