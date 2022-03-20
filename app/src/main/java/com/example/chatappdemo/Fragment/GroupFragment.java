package com.example.chatappdemo.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappdemo.Activities.GroupMessageActivity;
import com.example.chatappdemo.Adapter.GroupListAdapter;
import com.example.chatappdemo.Adapter.UserAdapter;
import com.example.chatappdemo.Model.GroupList;
import com.example.chatappdemo.Model.User;
import com.example.chatappdemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {

    private RecyclerView recyclerView;

    private GroupListAdapter groupListAdapter;
    private List<GroupList> mGroupLists;


    EditText search_users;
    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mGroupLists = new ArrayList<>();


        readGroups();

        search_users = view.findViewById(R.id.search_users);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchGroups(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    private void readGroups() {

        final  FirebaseUser  firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("grouplist");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(search_users.getText().toString().equals("")) {
                    mGroupLists.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        GroupList groupList = snapshot.getValue(GroupList.class);

                        if (groupList.getCreatormember().equals(firebaseUser.getUid()) || groupList.getMember0().equals(firebaseUser.getUid())
                            || groupList.getMember1().equals(firebaseUser.getUid()) || groupList.getMember2().equals(firebaseUser.getUid())) {
                            mGroupLists.add(groupList);
                        }
                    }

                    groupListAdapter = new GroupListAdapter(getContext(), mGroupLists);
                    recyclerView.setAdapter(groupListAdapter);
                    groupListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchGroups(String s){
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("grouplist").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroupLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    GroupList groupList = snapshot.getValue(GroupList.class);

                    assert groupList != null;
                    assert fuser != null;
                    if (groupList.getCreatormember().equals(fuser.getUid()) || groupList.getMember0().equals(fuser.getUid())
                            || groupList.getMember1().equals(fuser.getUid()) || groupList.getMember2().equals(fuser.getUid())){
                        mGroupLists.add(groupList);
                    }
                }

                groupListAdapter = new GroupListAdapter(getContext(), mGroupLists);
                recyclerView.setAdapter(groupListAdapter);
                groupListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
