package com.example.library;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookProgramList extends AppCompatActivity implements LibraryAdapterSearch.Callback {
    ArrayList<BookDetail> bookList;
    LibraryAdapterSearch adpter;
    RecyclerView recyclerView;
    LibraryAdapterSearch.Callback callback;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_program_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createDummyData();
        setupViews();
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.recyclerViewBookProgramList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adpter = new LibraryAdapterSearch(bookList, this);
        recyclerView.setAdapter(adpter);
    }
private  void  createDummyData(){
    bookList = new ArrayList<>();
    final FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
    DatabaseReference myRef = database.getReference("Books");
    myRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          for (DataSnapshot dataSnapshot : snapshot.getChildren()){
              BookDetail item = dataSnapshot.getValue(BookDetail.class);
              bookList.add(item);
              adpter.updateData(bookList);
          }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
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
        Intent intent = new Intent(this, BookInformation.class);
        intent.putExtra("valueItem", bookList.get(position).getBookCode()+"");
        startActivity(intent);
    }
}