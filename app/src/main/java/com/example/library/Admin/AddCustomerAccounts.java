package com.example.library.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.library.R;

import java.util.ArrayList;

public class AddCustomerAccounts extends AppCompatActivity {
    String sex, position;
    int PERMISSION_CODE = 100;
    int REQUEST_CODE_CAMERA_CAPTURE =110;

    ImageView imageView;
    Uri imgUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        setupViews();
        getDataFromViews();
        popupMenuImage();
    }
    private void getDataFromViews() {

        final EditText edtName = findViewById(R.id.edtNameCustomer);
        final EditText edtCode = findViewById(R.id.edtCustomerCode);
        final EditText edtBD = findViewById(R.id.edtBirthDayCustomer);
        final EditText edtAddress = findViewById(R.id.edtCustmerAddress);
        final EditText edtCardCodeCustomer = findViewById(R.id.edtCardCodeCustomer);
        final EditText edtEmail = findViewById(R.id.edtEmailCustomer);
        final EditText edtPhone = findViewById(R.id.edtPhoneCustomer);
        Button btnSave = findViewById(R.id.btnSaveImformationCustomer);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = edtName.getText().toString().trim();
                String employeeCode = edtCardCodeCustomer.getText().toString().trim();
                String birthDay = edtBD.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String code = edtCode.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();

                if (fullName.isEmpty() || employeeCode.isEmpty()|| birthDay.isEmpty() || address.isEmpty()|| email.isEmpty() || code.isEmpty() || phone.isEmpty()){
                    Toast.makeText(AddCustomerAccounts.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(AddCustomerAccounts.this, fullName+" "+ employeeCode+" "+birthDay+" "+address+" "+email+" "+code+" "+phone+" "+sex+" "+position, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setupViews() {
        Spinner spinnerSex = findViewById(R.id.spinnerCutomerSex);
        final ArrayList<String> sexArray = new ArrayList<>();
        sexArray.add(getString(R.string.male));
        sexArray.add(getString(R.string.female));
        ArrayAdapter adapterSex = new ArrayAdapter(this,android.R.layout.simple_list_item_1, sexArray);
        spinnerSex.setAdapter(adapterSex);
        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sex =sexArray.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void popupMenuImage(){
        imageView = findViewById(R.id.imageCustomerCreate);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(AddCustomerAccounts.this, imageView);
                popupMenu.inflate(R.menu.menu_image_add_information);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.openCamera:
                                Toast.makeText(AddCustomerAccounts.this, "Open Camera", Toast.LENGTH_LONG).show();
                               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                   if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                                           || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                                       String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                       requestPermissions(permission, PERMISSION_CODE);
                                   }
                                   else {
                                       openCamera();
                                   }
                               }else{
                                   openCamera();
                               }

                                break;
                            case R.id.openPhotos:
                                Toast.makeText(AddCustomerAccounts.this, "Open Photos", Toast.LENGTH_LONG).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });
    }
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From to Camera");
        imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult( intent, REQUEST_CODE_CAMERA_CAPTURE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }
            else{
                Toast.makeText(AddCustomerAccounts.this, "Permissions denied...", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(AddCustomerAccounts.this, "Permissions denied...", Toast.LENGTH_LONG).show();

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA_CAPTURE && resultCode ==RESULT_OK&& data != null){
            imageView.setImageURI( imgUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}