package com.example.sautinews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import android.Manifest;
import android.content.pm.PackageManager;

public class ProfileCompletionActivity extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    TextView dateTextView;
    Spinner spinner;
    String gender,userName,fullName,imageUrl,dob;
    EditText editTextUsername,editTextFullname;
    DatabaseReference myRef1;
    ProgressBar progressBar;
  private   ImageView imageView;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private static final int PERMISSION_REQUEST_CODE = 3;
    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_completion);
        dateTextView = findViewById(R.id.txtView_date);
        editTextFullname=findViewById(R.id.edtTxtFullName);
        editTextUsername=findViewById(R.id.edtTxtUserName);
        progressBar=findViewById(R.id.progressBarComplete);
        imageView=findViewById(R.id.img_addphoto);
        button=findViewById(R.id.buttonSaveProfile);

        final Calendar c = Calendar.getInstance();
        String[] sex={"choose..","male","female"};

        // Define a button or any other view that triggers the date picker dialog
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSelectionDialog();
            }
        });
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileCompletionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Set the selected date in a TextView or EditText

                                dateTextView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                dob=dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            }
                        }, mYear, mMonth, mDay);

                // Show the date picker dialog
                datePickerDialog.show();
            }
        });

       spinner=findViewById(R.id.spinner_dob);
        ArrayAdapter arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,sex);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        gender=spinner.getSelectedItem().toString();
        userName=editTextUsername.getText().toString().trim();
        fullName=editTextFullname.getText().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishSaving();
            }
        });
    }
    public void finishSaving()
    {
        if (TextUtils.isEmpty(editTextFullname.getText().toString())) {
            Toast.makeText(this, "Please state your FullName", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editTextUsername.getText().toString())) {
            Toast.makeText(this, "Please state a username", Toast.LENGTH_SHORT).show();
            return;
        }


        long timestamp=System.currentTimeMillis();
        gender=spinner.getSelectedItem().toString();
        userName=editTextUsername.getText().toString().trim();
        fullName=editTextFullname.getText().toString();
        String currentId= FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        Query usernameQuery = usersRef.orderByChild("username").equalTo(userName);

        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(ProfileCompletionActivity.this, "username already exists type another", Toast.LENGTH_SHORT).show();
                } else {
                    // Username doesn't exist
                    // Handle the case accordingly
                    HashMap<Object,Object> artcleMap=new HashMap<>();
                    artcleMap.put("userName",userName);
                    artcleMap.put("fullName",fullName);
                    artcleMap.put("timestamp",timestamp);
                    artcleMap.put("profileUrl",imageUrl);
                    artcleMap.put("authorId",currentId);
                    artcleMap.put("dOB",dob);
                    progressBar.setVisibility(View.VISIBLE);
                    String userId=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    myRef1=FirebaseDatabase.getInstance().getReference().child("users");
                    myRef1.push().setValue(artcleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ProfileCompletionActivity.this, "Saved☺", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ProfileCompletionActivity.this,NewsHomePage.class);
                                startActivity(intent);

                            }else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ProfileCompletionActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileCompletionActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // An error occurred while retrieving the data
                // Handle the error accordingly
            }});

    }


    private void showImageSelectionDialog() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    requestCameraPermission();
                } else if (options[item].equals("Choose from Gallery")) {
                    requestGalleryPermission();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        } else {
            launchCamera();
        }
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            openGallery();
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    launchCamera();
                } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null && extras.containsKey("data")) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    uploadImageToStorage(imageBitmap);
                }
            } else if (requestCode == REQUEST_IMAGE_GALLERY && data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    uploadImageToStorage(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadImageToStorage(Bitmap imageBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        String imageName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageReference.child(imageName);

        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                      imageUrl = uri.toString();



                        // Handle the retrieved image URL
                        // For example, save it to the database or display the image in your app
                        imageView.setImageURI(uri);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors during image upload
            }
        });
    }

}