package com.example.library.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library.BookDetail;
import com.example.library.BookInformation;
import com.example.library.BookProgramList;
import com.example.library.LibraryAdapterHome;
import com.example.library.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Home extends Fragment implements LibraryAdapterHome.Callback {
    View view;
   public static ArrayList<BookDetail> bookListFavourite;
   private LibraryAdapterHome adapterHome;
   LibraryAdapterHome.Callback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setupViews();
       setupRecyclerViewFavourite();
        return  view;
    }

    private void setupViews() {
        TextView seeMore = view.findViewById(R.id.txtSeeMore);
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BookProgramList.class);
                intent.putExtra("category", "all");
                startActivity(intent);
            }
        });
    }

    private  void setupRecyclerViewFavourite(){
        // recyclerView favourite
        getDataBookFavouriteFromFireBase();
        RecyclerView recyclerViewFavourite = view.findViewById(R.id.recyclerViewBookLike);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        recyclerViewFavourite.setLayoutManager(layoutManager);
        adapterHome = new LibraryAdapterHome(bookListFavourite, this);
        recyclerViewFavourite.setAdapter(adapterHome);

//        // recyclerView New
//        RecyclerView recyclerViewBookNew = view.findViewById(R.id.recyclerViewBookNew);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
//        recyclerViewBookNew.setLayoutManager(gridLayoutManager);
//        adapterHome = new LibraryAdapterHome(bookList, this);
//        recyclerViewBookNew.setAdapter(adapterHome);
    }
    private  void  getDataBookFavouriteFromFireBase(){
             bookListFavourite = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        Query query = database.getReference("Books").orderByChild("currentViews").limitToLast(3);
       query.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                   BookDetail bookDetail = dataSnapshot.getValue(BookDetail.class);
                   bookListFavourite.add(bookDetail);
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
        increaseView(position);
       Intent intent = new Intent(getContext(), BookInformation.class);
        intent.putExtra("valueItem", bookListFavourite.get(position).getBookCode());
      startActivity(intent);
    }
    private void increaseView(int position){
        String child = bookListFavourite.get(position).getBookCode();
       int currentView = bookListFavourite.get(position).getCurrentViews();
       int increase = currentView +1;
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Books");
        HashMap map = new HashMap();
        map.put("currentViews", increase);
        myRef.child(child).updateChildren(map);
    }

}
