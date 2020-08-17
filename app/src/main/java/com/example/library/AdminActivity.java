package com.example.library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.library.Admin.AddAuthors;
import com.example.library.Admin.AddCustomerAccounts;
import com.example.library.Admin.AddEmployeeAccount;
import com.example.library.Admin.MoreBooks;
import com.example.library.Admin.PublishingCompany;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setupViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void setupViews() {
        CardView cardViewPublishingCompany = findViewById(R.id.cardViewAddPublishingCompany);
        CardView cardViewMoreBooks = findViewById(R.id.cardViewMoreBooks);
        CardView cardViewAddAuthor = findViewById(R.id.cardViewAddAuthor);
        CardView cardViewAddEmployee = findViewById(R.id.cardViewAddEmployee);
        CardView cardViewAddCustomer = findViewById(R.id.cardViewAddCustomers);


        cardViewPublishingCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, PublishingCompany.class);
                startActivity(intent);
            }
        });
        cardViewMoreBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, MoreBooks.class));
            }
        });
        cardViewAddAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AddAuthors.class));
            }
        });
        cardViewAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AddCustomerAccounts.class));
            }
        });
        cardViewAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AddEmployeeAccount.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}