package com.example.chatappdemo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatappdemo.Model.Chat;
import com.example.chatappdemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;
    private String chatType;
    FirebaseUser fuser;


    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl,String chatType) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageurl = imageurl;
        this.chatType = chatType;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);

        if (!chat.getMessage().equals("default")) {
            holder.show_message.setText(chat.getMessage());
            holder.imageView.setVisibility(View.GONE);
        } else {
            Glide.with(mContext).load(chat.getImageMessage()).into(holder.imageView);
            holder.show_message.setVisibility(View.GONE);
        }

        if (imageurl.equals("default")) {
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }
        if (position == mChat.size() - 1) {
            holder.txt_time.setText(chat.getTime());
        } else {
            holder.txt_time.setVisibility(View.GONE);
        }

            if (position == mChat.size() - 1) {
                if (chat.isIsseen()) {
                    holder.txt_seen.setText("Seen");
                } else {
                    holder.txt_seen.setText("Delivered");
                }
            } else {
                holder.txt_seen.setVisibility(View.GONE);
            }

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
                                    if (chat.getSender().equals(fuser.getUid())) {
                                        database.getReference().child("Chats").child(chat.getKey()).child("deleteBySender").setValue(true);
                                    } else {
                                        database.getReference().child("Chats").child(chat.getKey()).child("deleteByReceiver").setValue(true);
                                    }
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

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;
        public TextView txt_seen;
        public TextView txt_time;
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
            txt_time = itemView.findViewById(R.id.txt_time);
            imageView = itemView.findViewById(R.id.show_image_msg);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
