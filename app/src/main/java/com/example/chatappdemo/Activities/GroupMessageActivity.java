package com.example.chatappdemo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatappdemo.Adapter.GroupMessageAdapter;
import com.example.chatappdemo.Adapter.MessageAdapter;
import com.example.chatappdemo.Model.GroupChat;
import com.example.chatappdemo.Model.GroupList;
import com.example.chatappdemo.Model.User;
import com.example.chatappdemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMessageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton btnSendMsg, btnSendImage;
    EditText writeTextMsg;
    TextView groupName;
    CircleImageView groupImage;
    DatabaseReference databaseReference;
    String groupId;
    FirebaseUser fuser;
    boolean notify = false;
    List<GroupChat> groupChats;
    GroupMessageAdapter messageAdapter;
    final String chatType = "groupChat";
    String senderImageUrl = "default";
    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);

        recyclerView = findViewById(R.id.recycler_view);
        btnSendMsg = findViewById(R.id.btn_send);
        writeTextMsg = findViewById(R.id.text_send);
        btnSendImage = findViewById(R.id.send_image_msg);
        groupName = findViewById(R.id.groupname);
        groupImage = findViewById(R.id.group_image);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        fuser = FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupid");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(GroupMessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("imageURL");
        databaseReference1.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    senderImageUrl = String.valueOf(task.getResult().getValue());

                }
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(groupId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GroupList groupList = snapshot.getValue(GroupList.class);
                groupName.setText(groupList.getGroupname());
                if (groupList.getImageURL().equals("default")) {
                    groupImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    //and this
                    Glide.with(getApplicationContext()).load(groupList.getImageURL()).into(groupImage);
                }

                readGroupMsg();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = writeTextMsg.getText().toString();
                if (!msg.equals("")) {

                    sendMessage(fuser.getUid(), groupId, msg, "noimage");

                } else {
                    Toast.makeText(GroupMessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                writeTextMsg.setText("");
            }
        });

        seenMessage();
    }

    private void readGroupMsg() {

        groupChats = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("groupChats").child(groupId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChats.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        GroupChat groupChat = dataSnapshot.getValue(GroupChat.class);
                        groupChats.add(groupChat);
                    }
                }

                messageAdapter = new GroupMessageAdapter(GroupMessageActivity.this, groupChats, chatType);
                recyclerView.setAdapter(messageAdapter);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void seenMessage() {
        List<GroupList> groupListArrayList = new ArrayList<>();
        List<String> meStringList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(groupId).child("creatormember");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String member = String.valueOf(task.getResult().getValue());
                    meStringList.add(member);
                } else {
                    meStringList.add("");
                }
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(groupId).child("member0");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String member = String.valueOf(task.getResult().getValue());
                    meStringList.add(member);
                } else {
                    meStringList.add("");
                }
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(groupId).child("member1");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String member = String.valueOf(task.getResult().getValue());
                    meStringList.add(member);
                } else {
                    meStringList.add("");
                }
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(groupId).child("member2");
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    String member = String.valueOf(task.getResult().getValue());
                    meStringList.add(member);
                } else {
                    meStringList.add("");
                }
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("groupChats").child(groupId);
        seenListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //   GroupChat chat = snapshot.getValue(GroupChat.class);
                    if (meStringList.get(0).equals(fuser.getUid())) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("username");
                        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    String name = String.valueOf(task.getResult().getValue());
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("seenByReceiver0", name);
                                    snapshot.getRef().updateChildren(hashMap);
                                }
                            }
                        });

                    }
                    if (meStringList.get(1).equals(fuser.getUid())) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("username");
                        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    String name = String.valueOf(task.getResult().getValue());
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("seenByReceiver1", name);
                                    snapshot.getRef().updateChildren(hashMap);
                                }
                            }
                        });
                    }
                    if (meStringList.get(2).equals(fuser.getUid())) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("username");
                        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    String name = String.valueOf(task.getResult().getValue());
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("seenByReceiver2", name);
                                    snapshot.getRef().updateChildren(hashMap);
                                }
                            }
                        });
                    }

                    if (meStringList.get(3).equals(fuser.getUid())) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("username");
                        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    String name = String.valueOf(task.getResult().getValue());
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("seenByReceiver3", name);
                                    snapshot.getRef().updateChildren(hashMap);
                                }
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiverGroup, String message, String imageMessage) {
        String time = "mm.ss.yy";
        Date dateTime = Calendar.getInstance().getTime();

        time = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(dateTime);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("groupChats").child(groupId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("senderImage", senderImageUrl);
        hashMap.put("receiver", receiverGroup);
        hashMap.put("message", message);
        hashMap.put("imageMessage", imageMessage);
        hashMap.put("time", time);
        hashMap.put("seenByReceiver0", "");
        hashMap.put("seenByReceiver1", "");
        hashMap.put("seenByReceiver2", "");
        hashMap.put("seenByReceiver3", "");


        // reference.child("Chats").push().setValue(hashMap);
        reference.push().setValue(hashMap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_msg_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.leave:
                // FirebaseAuth.getInstance().signOut();
                //  startActivity(new Intent(GroupMessageActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(groupId).child("creatormember");
                databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            String member = String.valueOf(task.getResult().getValue());
                            if (fuser.getUid().equals(member)) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                database.getReference("grouplist").child(groupId).child("creatormember").setValue("leaved");
                                finish();
                                startActivity(new Intent(GroupMessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            } else {
                                databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(groupId).child("member0");
                                databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            String member = String.valueOf(task.getResult().getValue());
                                            if (fuser.getUid().equals(member)) {
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                database.getReference("grouplist").child(groupId).child("member0").setValue("leaved");
                                                finish();
                                                startActivity(new Intent(GroupMessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                            } else {
                                                databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(groupId).child("member1");
                                                databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            String member = String.valueOf(task.getResult().getValue());
                                                            if (fuser.getUid().equals(member)) {
                                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                database.getReference("grouplist").child(groupId).child("member1").setValue("leaved");
                                                                finish();
                                                                startActivity(new Intent(GroupMessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                            } else {
                                                                databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(groupId).child("member2");
                                                                databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            String member = String.valueOf(task.getResult().getValue());
                                                                            if (fuser.getUid().equals(member)) {
                                                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                                database.getReference("grouplist").child(groupId).child("member2").setValue("leaved");
                                                                                finish();
                                                                                startActivity(new Intent(GroupMessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                                                            }
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });


                return true;
        }

        return false;
    }

}
