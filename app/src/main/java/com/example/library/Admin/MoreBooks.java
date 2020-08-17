package com.example.library.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.library.BookDetail;
import com.example.library.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class MoreBooks extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1000 ;
    private static final int PERMISSION_CODE_GALLERY = 1100 ;
    private int REQUEST_CODE_PICK_CAPTURE = 1111;
    private  int REQUEST_CODE_CAMERA_CAPTURE= 1112;
    ImageView imageBookCreate;
    Spinner spinnerCategory;
    String spinnerItem;
    Uri imageUri ;
    Uri uriDownload;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        popupMenuImage();
        setDataToSpinner();



    }
    private void setDataToSpinner(){
        final ArrayList<String> dataSpinner = new ArrayList<>();
        dataSpinner.add(getString(R.string.king_doanh));
        dataSpinner.add(getString(R.string.quansu));
        dataSpinner.add(getString(R.string.ton_giao));
        dataSpinner.add(getString(R.string.thieu_nhi));
        dataSpinner.add(getString(R.string.phat_trien));
        dataSpinner.add(getString(R.string.gia_dinh));
        dataSpinner.add(getString(R.string.giao_duc));
        dataSpinner.add(getString(R.string.kinhte));
        dataSpinner.add(getString(R.string.lich_su));
        spinnerCategory = findViewById(R.id.spinnerCategory);
       ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataSpinner);
       spinnerCategory.setAdapter(adapterSpinner);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             spinnerItem = dataSpinner.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void getDataFromViewsAddToFirebase() {
       progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Up load to Firebase...");
        //innit
        EditText edtBookName = findViewById(R.id.edtBookName);
        EditText edtAuthorCode = findViewById(R.id.edtAthorCode);
        EditText edtBookNation = findViewById(R.id.edtBookNation);
        EditText edtBookLanguage = findViewById(R.id.edtBookLaguage);
        EditText edtBookCode = findViewById(R.id.edtBookCode);
        EditText edtPublishingCompanyCode = findViewById(R.id.edtPublishingCompanyCode);
        EditText edtIntro = findViewById(R.id.edtIntroBook);
        EditText edtDateOfPublication = findViewById(R.id.edtDateOfPublication);
        EditText edtNumbersOfPage = findViewById(R.id.edtNumbersOfPage);
        //get data
        String bookName = edtBookName.getText().toString().trim();
        String authorCode = edtAuthorCode.getText().toString().trim();
        String bookNation = edtBookNation.getText().toString().trim();
        String bookLanguage = edtBookLanguage.getText().toString().trim();
        String bookCode = edtBookCode.getText().toString().trim();
        String publishingCompanyCode = edtPublishingCompanyCode.getText().toString().trim();
        String intro = edtIntro.getText().toString().trim();
        String dateOfPublication =  edtDateOfPublication.getText().toString().trim();
        String numbersOfPage = edtNumbersOfPage.getText().toString().trim();

        if (bookName.isEmpty() || authorCode.isEmpty() || bookNation.isEmpty()|| bookLanguage.isEmpty()|| bookCode.isEmpty()|| publishingCompanyCode.isEmpty() || intro.isEmpty() || dateOfPublication.isEmpty() || numbersOfPage.isEmpty() ){
            Toast.makeText(MoreBooks.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
        }
        else {
            progressDialog.show();
//            Toast.makeText(MoreBooks.this, t+"", Toast.LENGTH_LONG).show();
        saveImageToStorageAndDownloadUri(bookName, authorCode, bookNation, bookLanguage, bookCode, spinnerItem, publishingCompanyCode, dateOfPublication,Integer.parseInt(numbersOfPage) , intro);
        }

    }
    private void addDataToFireBaseDataBase(String bookName, String authorCode,String bookNation, String  bookLanguage, String bookCode,String spinnerItem, String publishingCompanyCode, String dateOfPublication, int numbersOfPage , String intro, String uriImage){
        BookDetail bookDetail= new BookDetail(bookName, authorCode, bookNation, bookLanguage, bookCode, spinnerItem,  publishingCompanyCode, dateOfPublication, numbersOfPage, intro,uriImage);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://library-80e61.firebaseio.com/");
        DatabaseReference myRef = database.getReference("Books");
        myRef.child(bookCode).setValue(bookDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(MoreBooks.this, "Save data FireBase success", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MoreBooks.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void saveImageToStorageAndDownloadUri(final String bookName, final String authorCode, final String bookNation, final String  bookLanguage, final String bookCode, final String spinnerItem, final String publishingCompanyCode, final String dateOfPublication, final int numbersOfPage , final String intro){
    imageBookCreate = findViewById(R.id.imageBookCreate);
//        String path = pathBookImageInStorage+bookCode+".png";
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://library-80e61.appspot.com");
    StorageReference storageRef = storage.getReference();
// Create a reference to "mountains.jpg"
    final StorageReference mountainsRef = storageRef.child("image/"+bookCode+".jpg");
    imageBookCreate.setDrawingCacheEnabled(true);
    imageBookCreate.buildDrawingCache();
    Bitmap bitmap = ((BitmapDrawable) imageBookCreate.getDrawable()).getBitmap();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    final byte[] data = baos.toByteArray();
    final UploadTask uploadTask = mountainsRef.putBytes(data);
    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return mountainsRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        uriDownload = task.getResult();
                        Log.d("VVVVV", uriDownload.toString());

                        addDataToFireBaseDataBase(bookName, authorCode, bookNation, bookLanguage, bookCode, spinnerItem, publishingCompanyCode, dateOfPublication,Integer.parseInt(String.valueOf(numbersOfPage)) , intro, uriDownload.toString() );

                    } else {
                        Toast.makeText(MoreBooks.this, "Download uri fail",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    });
}
    private void popupMenuImage(){
        imageBookCreate= findViewById(R.id.imageBookCreate);
        imageBookCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MoreBooks.this,imageBookCreate );
                popupMenu.inflate(R.menu.menu_image_add_information);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.openCamera:
                                checkCameraPermission();
                                break;
                            case R.id.openPhotos:
                                checkPermissionReadFromGallery();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private  void checkCameraPermission(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
           if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
           || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
               String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
               //show popup to request permission
               requestPermissions(permission, PERMISSION_CODE);
           }
           else {
               //permission already granted
               openCamera();
           }
       }
       else {
           // system os < marshmallow
           openCamera();
       }
    }
    private  void  openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA_CAPTURE);
    }
    private  void checkPermissionReadFromGallery(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE_GALLERY);
            }
            else {
                openGallery();
            }
        }
        else {
            openGallery();
        }
    }
    private  void openGallery(){
        Intent intentPickGallery = new Intent(Intent.ACTION_PICK);
        intentPickGallery.setType("image/*");
        startActivityForResult(intentPickGallery, REQUEST_CODE_PICK_CAPTURE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menuSave:
                getDataFromViewsAddToFirebase();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_LONG).show();
                }
                break;
            case  PERMISSION_CODE_GALLERY:
                if (grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openGallery();
                }
                else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_CODE_CAMERA_CAPTURE&& resultCode== RESULT_OK&& data!= null){
          imageBookCreate.setImageURI(imageUri);
        }
        if (requestCode== REQUEST_CODE_PICK_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            imageUri = data.getData();
            imageBookCreate.setImageURI(imageUri);
        }
    }
}