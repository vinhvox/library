package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BookProgramList extends AppCompatActivity implements LibraryAdapterSearch.Callback {
    ArrayList<BookDetail> bookList;
    LibraryAdapterSearch adpter;
    RecyclerView recyclerView;
    ImageView imageViewEmpty;
    LibraryAdapterSearch.Callback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_program_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        queryDataFromFireBase(category);
        setupViews();
    }

    private void setupViews() {
        imageViewEmpty = findViewById(R.id.idNoData);
        recyclerView = findViewById(R.id.recyclerViewBookProgramList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adpter = new LibraryAdapterSearch(bookList, this);
        recyclerView.setAdapter(adpter);
    }
private  void queryDataFromFireBase(String category){

    bookList = new ArrayList<>();

    final FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
    if (category.equals("all")){
        DatabaseReference myRef = database.getReference("Books");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    imageViewEmpty.setVisibility(View.GONE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BookDetail bookDetail = dataSnapshot.getValue(BookDetail.class);
                        bookList.add(bookDetail);
                        adpter.updateData(bookList);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    imageViewEmpty.setVisibility(View.VISIBLE);
                }
        }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
else {
        Query query = database.getReference("Books").orderByChild("category").equalTo(category);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    imageViewEmpty.setVisibility(View.GONE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        BookDetail bookDetail = dataSnapshot.getValue(BookDetail.class);
                        bookList.add(bookDetail);
                        adpter.updateData(bookList);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    imageViewEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("input book name here");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adpter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClickItem(int position) {
        if (callback!= null){
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
        increaseView(position);
        Intent intent = new Intent(this, BookInformation.class);
        intent.putExtra("valueItem", bookList.get(position).getBookCode()+"");
        startActivity(intent);
    }
    private void increaseView(int position){
        String child = bookList.get(position).getBookCode();
        int currentView = bookList.get(position).getCurrentViews();
        int increase = currentView +1;
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Books");
        HashMap map = new HashMap();
        map.put("currentViews", increase);
        myRef.child(child).updateChildren(map);
    }
}