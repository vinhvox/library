package com.example.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class LibraryAdapterHome  extends RecyclerView.Adapter<LibraryAdapterHome.ViewHolder> {
    List<BookDetail> bookDetailList;
    Callback callback;

    public LibraryAdapterHome(List<BookDetail> bookDetailList, Callback callback) {
        this.bookDetailList = bookDetailList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_recyclerview_book;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        View view = holder.getView();
        inflateDataToView( view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickItem(position);
            }
        });

    }
    public  void  inflateDataToView(View view, int position){
        BookDetail bookDetail = bookDetailList.get(position);
        ImageView coverImage = view.findViewById(R.id.coverImage);
        Picasso.get().load(bookDetail.getCoverImage()).into(coverImage);
    }

    @Override
    public int getItemCount() {
        return bookDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
        public  View getView(){
            return view;
        }
    }
    public  interface Callback{
        void onClickItem(int position);
    }
}
