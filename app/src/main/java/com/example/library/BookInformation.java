package com.example.library;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.Fragment.Home;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class BookInformation extends AppCompatActivity {
    ArrayList<BookDetail> data= new ArrayList<>();;
    TextView txtBookName;
    String bookCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViews();
        queryDataFromFireBase(bookCode);
    }

    private void setupViews() {
        Intent intent = getIntent();
        bookCode  = intent.getStringExtra("valueItem");

    }
    private void setDataToViews(ArrayList<BookDetail> item){




        String bookName = item.get(0).getBookName();
        String author = item.get(0).getAuthorCode();
        String category = item.get(0).getCategory();
        String language = item.get(0).getLanguage();
        String introduce = item.get(0).getIntroduce();
        final String nation = item.get(0).getNation();
        final String  publish = item.get(0).getPublish();
        final String  publicationDate = item.get(0).getPublicationDate();
        final int numberOfPages = item.get(0).getNumberOfPages();
        txtBookName = findViewById(R.id.txtBookNameInfo);
        TextView txtAuthor = findViewById(R.id.txtAuthorInfo);
        TextView txtCategory = findViewById(R.id.txtCategoryInfo);
        TextView txtIntroduceInfo = findViewById(R.id.txtIntroduceInfo);
        TextView txtLanguage = findViewById(R.id.txtLanguage);
        TextView txtExtendInfo = findViewById(R.id.txtExtendInfo);
        ImageView imageView = findViewById(R.id.imageBookInfo);
        txtBookName.setText(bookName);
        txtAuthor.setText(author);
        txtCategory.setText(category);
        txtIntroduceInfo.setText(introduce);
        txtLanguage.setText(language);
        txtExtendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShowExtendInfo(nation, publish, publicationDate, numberOfPages, bookCode);
            }
        });
        Picasso.get().load(item.get(0).getCoverImage()).into(imageView);
    }
    private  void dialogShowExtendInfo(String nation, String publish, String publicationDate, int numberOfPages, String bookCode){
        final Dialog dialog = new Dialog(BookInformation.this);
        dialog.setContentView(R.layout.custom_dialog_info);
        //init
        TextView txtNation =  dialog.findViewById(R.id.txtNationInfo);
        TextView txtPublish  = dialog.findViewById(R.id.txtPublishInfo);
        TextView txtPublicationDate = dialog.findViewById(R.id.txtPublicationDateInfo);
        TextView txtNumberOfPages = dialog.findViewById(R.id.txtNumberOfPagesInfo);
        TextView txtBookCode = dialog.findViewById(R.id.txtBookCodeInfo);
        Button btnDone = dialog.findViewById(R.id.btnDimissDialogInfo);
        txtNation.setText(nation);
        txtPublicationDate.setText(publicationDate);
        txtPublish.setText(publish);
        txtNumberOfPages.setText(String.valueOf(numberOfPages));
        txtBookCode.setText(bookCode);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
    private  void queryDataFromFireBase(final String bookCode){
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference reference = database.getReference("Books");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BookDetail bookDetail = snapshot.getValue(BookDetail.class);
               if (bookDetail.getBookCode().equals(bookCode.trim())){
                   Log.d("Vinh", bookDetail.getBookName()+"");
                   data.add(new BookDetail(bookDetail.getBookName(), bookDetail.getAuthorCode(), bookDetail.getNation(), bookDetail.getLanguage(), bookDetail.getBookCode(), bookDetail.getCategory(), bookDetail.getPublish(), bookDetail.getPublicationDate(), bookDetail.getNumberOfPages(), bookDetail.getIntroduce(), bookDetail.getCoverImage()));
                   setDataToViews(data);
               }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




    }
}