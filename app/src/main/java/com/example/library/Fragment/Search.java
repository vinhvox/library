package com.example.library.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.library.BookProgramList;
import com.example.library.R;

public class Search extends Fragment {
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        setupViews();
        return  view;
    }

    private void setupViews() {
        CardView cardViewKinhDaonh = view.findViewById(R.id.carbViewSearchBusiness);
        CardView cardViewChild = view.findViewById(R.id.cardViewsChild);
        cardViewKinhDaonh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtDanhMuc = view.findViewById(R.id.txtBusiness);
                String danhmuc = txtDanhMuc.getText().toString();
                Intent intent = new Intent(getContext(), BookProgramList.class);
                intent.putExtra("category", danhmuc);
                startActivity(intent);
            }
        });
        cardViewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtDanhMuc =  view.findViewById(R.id.txtChild);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_extend, menu);
        menu.findItem(R.id.extendAdmin).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.extendSearch:
                Intent intent = new Intent(getContext(), BookProgramList.class);
                intent.putExtra("category", "all");
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
