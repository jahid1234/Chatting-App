package com.example.chatappdemo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatappdemo.Adapter.UserAdapter;
import com.example.chatappdemo.Model.GroupList;
import com.example.chatappdemo.Model.User;
import com.example.chatappdemo.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateGroupActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;

    EditText search_users,groupName;

    ImageButton upload_image_btn,create_group_btn;
    CircleImageView groupImage;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    DatabaseReference databaseReference;
    FirebaseUser fuser;
    ValueEventListener imageloadListener;
    StorageReference storageReference;
    String createTime;
    boolean isDefaultImage = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create New Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(CreateGroupActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        groupName = findViewById(R.id.group_name_id);
        create_group_btn = findViewById(R.id.btn_create_group);
        groupImage = findViewById(R.id.group_image);
        upload_image_btn = findViewById(R.id.btn_upload);
        recyclerView = findViewById(R.id.create_group_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CreateGroupActivity.this));

        mUsers = new ArrayList<>();
        storageReference = FirebaseStorage.getInstance().getReference("groupimageuploads");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        createTime =  String.valueOf(System.currentTimeMillis());
        readUsers();

        search_users = findViewById(R.id.create_group_search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        upload_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        create_group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createGroup();
                //  createGroupChat();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(createTime);
            imageloadListener = databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        GroupList groupList = snapshot.getValue(GroupList.class);
                        groupName.setText(groupList.getGroupname());
                        if (groupList.getImageURL().equals("default")) {
                            groupImage.setImageResource(R.mipmap.ic_launcher);
                        } else {
                            Glide.with(getApplicationContext()).load(groupList.getImageURL()).into(groupImage);
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(search_users.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        if (!user.getId().equals(firebaseUser.getUid())) {
                            mUsers.add(user);
                        }
                    }

                    userAdapter = new UserAdapter(CreateGroupActivity.this, mUsers, true,false,true);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void searchUsers(String s){
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert fuser != null;
                    if (!user.getId().equals(fuser.getUid())){
                        mUsers.add(user);
                    }
                }

                userAdapter = new UserAdapter(CreateGroupActivity.this, mUsers, true,false,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            Glide.with(getApplicationContext()).load(imageUri).into(groupImage);
            isDefaultImage = false;

        }
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(CreateGroupActivity.this);
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){

                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(createTime);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", ""+mUri);
                        databaseReference.updateChildren(map);
                        pd.dismiss();

                        finish();
                        Intent intent = new Intent(CreateGroupActivity.this,GroupMessageActivity.class);
                        intent.putExtra("groupid",createTime);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {
                        Toast.makeText(CreateGroupActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateGroupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

        }else{
            Toast.makeText(CreateGroupActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void createGroup(){
        List<User> addedUser = new ArrayList<>();
        for(int i=0;i< mUsers.size(); i++){
            if(mUsers.get(i).isAddedToGroup()) {
                if(addedUser.size() <= 3) {
                    addedUser.add(mUsers.get(i));
                }else{
                    break;
                }
            }
        }

        if(addedUser.size() == 0){
            Toast.makeText(this, "Select At least One people", Toast.LENGTH_SHORT).show();
            return;
        }

        String creatorId = fuser.getUid();
        if(addedUser.size() <= 3) {
            String group = groupName.getText().toString();
            if(TextUtils.isEmpty(group)){
                Toast.makeText(this, "Give Group Name", Toast.LENGTH_SHORT).show();
                return;
            }
            databaseReference = FirebaseDatabase.getInstance().getReference("grouplist").child(createTime);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("groupname", group);
            hashMap.put("groupId", createTime);
            hashMap.put("imageURL", "default");
            hashMap.put("creatormember",creatorId);
            hashMap.put("member0","none");
            hashMap.put("member1","none");
            hashMap.put("member2","none");
            hashMap.put("search", group.toLowerCase());
            for (int i = 0; i < addedUser.size(); i++) {
                hashMap.put("member" + i, addedUser.get(i).getId());
            }
           // databaseReference.updateChildren(hashMap);
            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(CreateGroupActivity.this, "Group Created", Toast.LENGTH_SHORT).show();
                    if(!isDefaultImage) {
                        if (uploadTask != null && uploadTask.isInProgress()) {
                            Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
                        } else {
                            uploadImage();
                        }
                    }else{
                        finish();
                        Intent intent = new Intent(CreateGroupActivity.this,GroupMessageActivity.class);
                        intent.putExtra("groupid",createTime);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
        }else {
            Toast.makeText(this, "Only 3 people can be added to Group ", Toast.LENGTH_SHORT).show();
        }

    }


}
