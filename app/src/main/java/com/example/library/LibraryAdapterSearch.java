package com.example.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LibraryAdapterSearch extends RecyclerView.Adapter< LibraryAdapterSearch.ViewHolder> implements Filterable {
    List<BookDetail> bookList;
    List<BookDetail> bookDetailsFilter;
    Callback callback;

    public LibraryAdapterSearch(List<BookDetail> bookList, Callback callback) {
        this.bookList = bookList;
        this.callback = callback;
        this.bookDetailsFilter = new ArrayList<>(bookList);
    };

    public void updateData(List<BookDetail> bookList){
        this.bookList = bookList;
        this.bookDetailsFilter = new ArrayList<>(bookList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.item_recyclerview_book;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext())  ;
        View view = layoutInflater.inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        View view = holder.getView();
        inflateDataToView(view, position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (callback== null) return;
               callback.onClickItem(position);
            }
        });

    }
    public  void  inflateDataToView(View view, int position){
        BookDetail bookDetail = bookDetailsFilter.get(position);
        ImageView coverImage = view.findViewById(R.id.coverImage);
        Picasso.get().load(bookDetail.getCoverImage()).into(coverImage);
    }
    @Override
    public int getItemCount() {
        return bookDetailsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<BookDetail> filterList = new ArrayList<>();
            if (charSequence.toString().trim().isEmpty()){
                filterList.addAll(bookList);
            }else {
                for (BookDetail bookDetail : bookList){
                    if (bookDetail.getBookCode().toLowerCase().contains(charSequence.toString().trim().toLowerCase())){
                        filterList.add(bookDetail);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return  filterResults;

        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
           bookDetailsFilter.clear();
           bookDetailsFilter.addAll((Collection<? extends BookDetail>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    static  class ViewHolder extends RecyclerView.ViewHolder{
                     View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public View getView() {
            return view;
        }
    }
    public  interface  Callback{
        void onClickItem(int position);
    }
}
