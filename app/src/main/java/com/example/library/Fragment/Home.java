package com.example.library.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.BookDetail;
import com.example.library.BookInformation;
import com.example.library.LibraryAdapterHome;
import com.example.library.LibraryAdapterSearch;
import com.example.library.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;

public class Home extends Fragment implements LibraryAdapterHome.Callback {
    View view;
   public static ArrayList<BookDetail> bookList;
   private LibraryAdapterHome adapterHome;
   LibraryAdapterHome.Callback callback;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        createDummyData();
        setupViews();
        return  view;
    }
    private  void setupViews(){
        // recyclerView favourite
        RecyclerView recyclerViewFavourite = view.findViewById(R.id.recyclerViewBookLike);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        recyclerViewFavourite.setLayoutManager(layoutManager);
        adapterHome = new LibraryAdapterHome(bookList, this);
        recyclerViewFavourite.setAdapter(adapterHome);

        // recyclerView New
        RecyclerView recyclerViewBookNew = view.findViewById(R.id.recyclerViewBookNew);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewBookNew.setLayoutManager(gridLayoutManager);
        adapterHome = new LibraryAdapterHome(bookList, this);
        recyclerViewBookNew.setAdapter(adapterHome);


    }
    private  void  createDummyData(){
             bookList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Books");
       myRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                   BookDetail bookDetail = dataSnapshot.getValue(BookDetail.class);
                   bookList.add(bookDetail);
                   adapterHome.notifyDataSetChanged();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    @Override
    public void onClickItem(int position) {
        if (callback!= null){
            Toast.makeText(view.getContext(), "Error", Toast.LENGTH_LONG).show();
        }
       Intent intent = new Intent(getContext(), BookInformation.class);
        intent.putExtra("valueItem", bookList.get(position).getBookCode());
      startActivity(intent);


    }

}
