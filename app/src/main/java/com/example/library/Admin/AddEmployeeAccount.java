package com.example.library.Admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

public class AddEmployeeAccount extends AppCompatActivity {
 String sex, position;
    int REQUEST_ID_IMAGE_CAPTURE = 100;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee_account);
        setupViews();
        getDataFromViews();
        popupMenuImage();
    }

    private void getDataFromViews() {

        final EditText edtName = findViewById(R.id.edtNameEmployee);
        final EditText edtCode = findViewById(R.id.edtBookCategory);
        final EditText edtBD = findViewById(R.id.edtBookNation);
        final EditText edtAddress = findViewById(R.id.edtBookLaguage);
        final EditText edtCodeEmployee = findViewById(R.id.edtCodeEmployee);
        final EditText edtEmail = findViewById(R.id.edtBookCode);
        final EditText edtPhone = findViewById(R.id.edtPublishingCompanyCode);
        final Spinner spinnerSex = findViewById(R.id.spinnerSexEmployee);
        Spinner spinnerPosition = findViewById(R.id.spinnerPositionEmployee);
        Button btnSave = findViewById(R.id.btnSaveInformationEmployee);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = edtName.getText().toString().trim();
                String employeeCode = edtCodeEmployee.getText().toString().trim();
                String birthDay = edtBD.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String code = edtCode.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();

                if (fullName.isEmpty() || employeeCode.isEmpty()|| birthDay.isEmpty() || address.isEmpty()|| email.isEmpty() || code.isEmpty() || phone.isEmpty()){
                    Toast.makeText(AddEmployeeAccount.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(AddEmployeeAccount.this, fullName+" "+ employeeCode+" "+birthDay+" "+address+" "+email+" "+code+" "+phone+" "+sex+" "+position, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setupViews() {
        Spinner spinnerSex = findViewById(R.id.spinnerSexEmployee);
        Spinner spinnerPosition = findViewById(R.id.spinnerPositionEmployee);
        final ArrayList<String> sexArray = new ArrayList<>();
        sexArray.add(getString(R.string.male));
        sexArray.add(getString(R.string.female));
        final ArrayList<String> positionArray = new ArrayList<>();
        positionArray.add(getString(R.string.admin));
        positionArray.add(getString(R.string.staff));

        ArrayAdapter adapterSex = new ArrayAdapter(this,android.R.layout.simple_list_item_1, sexArray);
        spinnerSex.setAdapter(adapterSex);
        ArrayAdapter adapterPosition = new ArrayAdapter(this, android.R.layout.simple_list_item_1, positionArray);
        spinnerPosition.setAdapter(adapterPosition);

        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sex =sexArray.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position =positionArray.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void popupMenuImage(){
        imageView = findViewById(R.id.imageEmployeeCreate);
      imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              //Check camera permission
              if (ContextCompat.checkSelfPermission(AddEmployeeAccount.this, Manifest.permission.CAMERA)
                      == PackageManager.PERMISSION_GRANTED) {
                  ActivityCompat.requestPermissions(AddEmployeeAccount.this,
                          new String[]{
                                  Manifest.permission.CAMERA
                          }, REQUEST_ID_IMAGE_CAPTURE);
              }
              PopupMenu popupMenu = new PopupMenu(AddEmployeeAccount.this, imageView);
              popupMenu.inflate(R.menu.menu_image_add_information);
              popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem menuItem) {
                      switch (menuItem.getItemId()){
                          case R.id.openCamera:
                              Toast.makeText(AddEmployeeAccount.this, "Open Camera", Toast.LENGTH_LONG).show();

                              Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                              startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);

                              break;
                          case R.id.openPhotos:
                              Toast.makeText(AddEmployeeAccount.this, "Open Photos", Toast.LENGTH_LONG).show();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_ID_IMAGE_CAPTURE&& resultCode== RESULT_OK&& data!= null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
               imageView.setImageBitmap(bitmap);
        }
    }
}