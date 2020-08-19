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
import com.example.library.LibraryAdapterSearch;
import com.example.library.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Home extends Fragment implements LibraryAdapterSearch.Callback {
    View view;
   public static ArrayList<BookDetail> bookList;
  public  static LibraryAdapterSearch libraryAdapterSearch;
   LibraryAdapterSearch.Callback callback;
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
        libraryAdapterSearch = new LibraryAdapterSearch(bookList, this);
        recyclerViewFavourite.setAdapter(libraryAdapterSearch);
        // recyclerView New
        RecyclerView recyclerViewBookNew = view.findViewById(R.id.recyclerViewBookNew);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewBookNew.setLayoutManager(gridLayoutManager);
        libraryAdapterSearch = new LibraryAdapterSearch(bookList, this);
        recyclerViewBookNew.setAdapter(libraryAdapterSearch);


    }
    private  void  createDummyData(){
             bookList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Books");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                BookDetail bookDetail = snapshot.getValue(BookDetail.class);
                bookList.add(new BookDetail(bookDetail.getBookName(), bookDetail.getAuthorCode(), bookDetail.getNation(), bookDetail.getLanguage(), bookDetail.getBookCode(), bookDetail.getCategory(), bookDetail.getPublish(), bookDetail.getPublicationDate(), bookDetail.getNumberOfPages(), bookDetail.getIntroduce(), bookDetail.getCoverImage()));
//                bookList.addAll((Collection<? extends BookDetail>) bookDetail);
                libraryAdapterSearch.notifyDataSetChanged();
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
//        bookList = new ArrayList<>();
//        bookList.add(new BookDetail("vinh","vinh","skjdk", "kajsa","akjsak", "akjsa", "kajsak", "sodk",1,"Đắc nhân tâm – How to win friends and Influence People  của Dale Carnegie là quyển sách nổi tiếng nhất, bán chạy nhất và có tầm ảnh hưởng nhất của mọi thời đại. Tác phẩm đã được chuyển ngữ sang hầu hết các thứ tiếng trên thế giới và có mặt ở hàng trăm quốc gia. \n" +
//                "\n" +
//                "Đây là quyển sách duy nhất về thể loại self-help liên tục đứng đầu danh mục sách bán chạy nhất (best-selling Books) do báo The New York Times bình chọn suốt 10 năm liền. Riêng bản tiếng Anh của sách đã bán được hơn 15 triệu bản trên thế giới. Tác phẩm có sức lan toả vô cùng rộng lớn – dù bạn đi đến bất cứ nơi đâu, bất kỳ quốc gia nào cũng đều có thể nhìn thấy. Tác phẩm được đánh giá là quyển sách đầu tiên và hay nhất, có ảnh hưởng làm thay đổi cuộc đời của hàng triệu người trên thế giới.\n" +
//                "\n" +
//                "Không còn nữa khái niệm giới hạn, Đắc Nhân Tâm là nghệ thuật thu phục lòng người, là làm cho tất cả mọi người yêu mến mình. Đắc nhân tâm và cái Tài trong mỗi người chúng ta. Đắc Nhân Tâm trong ý nghĩa đó cần được thụ đắc bằng sự hiểu rõ bản thân, thành thật với chính mình, hiểu biết và quan tâm đến những người xung quanh để nhìn ra và khơi gợi những tiềm năng ẩn khuất nơi họ, giúp họ phát triển lên một tầm cao mới. Đây chính là nghệ thuật cao nhất về con người và chính là ý nghĩa sâu sắc nhất đúc kết từ những nguyên tắc vàng của Dale Carnegie.\n" +
//                "\n" +
//                "Quyển sách Đắc nhắn tâm là cuốn sách “đầu tiên và hay nhất mọi thời đại về nghệ thuật giao tiếp và ứng xử”, quyển sách đã từng mang đến thành công và hạnh phúc cho hàng triệu người trên khắp thế giới.;",  "https://firebasestorage.googleapis.com/v0/b/library-80e61.appspot.com/o/image%2Fweqewqewe.jpg?alt=media&token=a7da7c20-9230-4ffa-a6b3-9f88c01b7d1b"));
//        bookList.add(new BookDetail("Đắc nhân tâm","Vinh Vo Hoang","skjdk", "kajsa","akjsak", "akjsa", "kajsak", "sodk",1,"sdjlwkelwl;","https://firebasestorage.googleapis.com/v0/b/library-80e61.appspot.com/o/image%2Fvinh.jpg?alt=media&token=cfd534df-f916-4d9f-9596-b3114e48e8df"));
//        bookList.add(new BookDetail("Đắc nhân tâm","Bùi thị Thư","skjdk", "kajsa","akjsak", "akjsa", "kajsak", "sodk",1,"sdjlwkelwl;",  "https://firebasestorage.googleapis.com/v0/b/library-80e61.appspot.com/o/image%2Fweqewqewe.jpg?alt=media&token=a7da7c20-9230-4ffa-a6b3-9f88c01b7d1b"));
//        bookList.add(new BookDetail("Đắc nhân tâm","vinh","skjdk", "kajsa","akjsak", "akjsa", "kajsak", "sodk",1,"sdjlwkelwl;", "https://firebasestorage.googleapis.com/v0/b/library-80e61.appspot.com/o/image%2Fweqewqewe.jpg?alt=media&token=a7da7c20-9230-4ffa-a6b3-9f88c01b7d1b"));
//        bookList.add(new BookDetail("Đắc nhân tâm","vinh","skjdk", "kajsa","akjsak", "akjsa", "kajsak", "sodk",1,"sdjlwkelwl;",  "https://firebasestorage.googleapis.com/v0/b/library-80e61.appspot.com/o/image%2Fweqewqewe.jpg?alt=media&token=a7da7c20-9230-4ffa-a6b3-9f88c01b7d1b"));

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
