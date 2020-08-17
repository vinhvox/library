package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;


import com.example.library.Fragment.Account;
import com.example.library.Fragment.Cart;
import com.example.library.Fragment.Home;
import com.example.library.Fragment.Search;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActionBar toolbar;
   public static SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();

    }
    private  void setupView(){
        toolbar = getSupportActionBar();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new Home());

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigationHome:
                    fragment = new Home();
                    loadFragment(fragment);
                    return true;
                case R.id.navigationSearch:
                    fragment = new Search();
                    loadFragment(fragment);
                    return true;
                case R.id.navigationCart:
                    fragment = new Cart();
                    loadFragment(fragment);
                    return true;
                case R.id.navigationProfile:
                    fragment = new Account();
                    loadFragment(fragment);
                    return true;

            }

            return true;
        }
    };
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_admin, menu);
//        MenuItem menuItem = menu.findItem(R.id.menuSearch);
//        searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint("Type here to Search");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                Home.libraryAdpter.getFilter().filter(s);
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId()== R.id.menuAdmin){
//            startActivity(new Intent(MainActivity.this, AdminActivity.class));
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
