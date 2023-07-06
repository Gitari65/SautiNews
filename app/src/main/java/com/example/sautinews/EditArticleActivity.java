package com.example.sautinews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.text.style.BulletSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

public class EditArticleActivity extends AppCompatActivity {

    private EditText editText;
    private ImageButton boldButton;
    private ImageButton italicButton;
    private ImageButton underlineButton;
    private ImageButton imageButton_alignLeft;
    private ImageButton imageButton_alignCentre;
    private ImageButton imageButton_alignRight;
    private ImageButton increaseFontSizeButton;
    private ImageButton decreaseFontSizeButton;
    private ImageButton bulletListButton;
    private ImageButton numberedListButton;
    private ImageButton undoButton;
    private ImageButton redoButton;
    private ImageButton spellCheckButton;
    private DatabaseReference myRef;
    private  UndoManager undoManager;
    private TextView textViewdesc;
    private EditText editTextArticleTitle;
    private ImageView imageViewArticlePicture;
    private long timestamp;
    String imageurl;
  private Button buttonSave;
    private TextView txtViewPublish;
    private Uri selectedImageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    public EditArticleActivity() {
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        // Initialize your EditText and ImageButton variables
        editText = findViewById(R.id.editTextArticleContent);
        imageViewArticlePicture=findViewById(R.id.imageViewArticleCoverPicture);
        editTextArticleTitle=findViewById(R.id.editTextArticleTitle);
        boldButton = findViewById(R.id.boldButton);
        italicButton = findViewById(R.id.italicButton);
        underlineButton = findViewById(R.id.underlineButton);
        imageButton_alignLeft = findViewById(R.id.imageButton_alignLeft);
        imageButton_alignCentre = findViewById(R.id.imageButton_alignCentre);
        imageButton_alignRight = findViewById(R.id.imageButton_alignRight);
        increaseFontSizeButton = findViewById(R.id.increaseFontSizeButton);
        decreaseFontSizeButton = findViewById(R.id.decreaseFontSizeButton);
        bulletListButton = findViewById(R.id.bulletListButton);
        numberedListButton = findViewById(R.id.numberedListButton);
        undoButton = findViewById(R.id.undoButton);
        redoButton = findViewById(R.id.redoButton);
        spellCheckButton = findViewById(R.id.spellCheckButton);
        undoManager = new UndoManager(editText);
        buttonSave=findViewById(R.id.button_saveArticle);
       txtViewPublish=findViewById(R.id.button_publishArticle);
        // Set OnClickListener for the boldButton
        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBold();
            }
        });

        // Set OnClickListener for the italicButton
        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleItalic();
            }
        });

        // Set OnClickListener for the underlineButton
        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleUnderline();
            }
        });

        // Set OnClickListener for the imageButton_alignLeft
        imageButton_alignLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignTextLeft();
            }
        });

        // Set OnClickListener for the imageButton_alignCentre
        imageButton_alignCentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignTextCenter();
            }
        });

        // Set OnClickListener for the imageButton_alignRight
        imageButton_alignRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alignTextRight();
            }
        });

        // Set OnClickListener for the increaseFontSizeButton
        increaseFontSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseFontSize();
            }
        });

        // Set OnClickListener for the decreaseFontSizeButton
        decreaseFontSizeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                decreaseFontSize();
            }
        });

// Set OnClickListener for the bulletListButton
        bulletListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBulletList();
            }
        });

// Set OnClickListener for the numberedListButton
        numberedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleNumberedList();
            }
        });

// Set OnClickListener for the undoButton
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });

// Set OnClickListener for the redoButton
        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redo();
            }
        });

// Set OnClickListener for the spellCheckButton
        spellCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spellCheck();
            }
        });
        // Set OnClickListener for the save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status="saved";
                finishArticle(status);
            }
        });
        // Set OnClickListener for the publish textView
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status="published";
                finishArticle(status);
            }
        });
        imageViewArticlePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }



    private void toggleBold() {
        // Toggle the selected text between bold and normal
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());
        StyleSpan span = new StyleSpan(Typeface.BOLD);

        boolean isAlreadyBold = isStyleApplied(spannable, start, end, Typeface.BOLD);

        if (isAlreadyBold) {
            spannable.removeSpan(span);
        } else {
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        editText.setText(spannable);
    }

    private void toggleItalic() {
        // Toggle the selected text between italic and normal
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());
        StyleSpan span = new StyleSpan(Typeface.ITALIC);

        boolean isAlreadyItalic = isStyleApplied(spannable, start, end, Typeface.ITALIC);

        if (isAlreadyItalic) {
            spannable.removeSpan(span);
        } else {
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        editText.setText(spannable);
        // Set TextWatcher for the EditText to track changes
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update the UndoManager on text changes
                undoManager.onTextChanged(start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    //Save and publish method for the Article
    public void finishArticle(String Status)
    {
        if (TextUtils.isEmpty(editTextArticleTitle.getText().toString())) {
            Toast.makeText(this, "Please state the Title of your Article", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(this, "Please state the Content of your Article", Toast.LENGTH_SHORT).show();
            return;
        }
        savePicture();
        timestamp=System.currentTimeMillis();
        String title=editTextArticleTitle.getText().toString();
        String Content=editText.getText().toString();
        String currentId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef=FirebaseDatabase.getInstance().getReference().child("Article").child(Status);
        HashMap<Object,Object> artcleMap=new HashMap<>();
        artcleMap.put("ArticleTitle",title);
        artcleMap.put("ArticleContent",Content);
        artcleMap.put("timeStamp",timestamp);
        artcleMap.put("imageUrl",imageurl);
        myRef.setValue(artcleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(EditArticleActivity.this, Status, Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(EditArticleActivity.this, "Failed try again later", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void toggleUnderline() {
        // Toggle the selected text between underline and normal
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());
        UnderlineSpan span = new UnderlineSpan();

        UnderlineSpan[] existingSpans = spannable.getSpans(start, end, UnderlineSpan.class);

        if (existingSpans.length > 0) {
            for (UnderlineSpan existingSpan : existingSpans) {
                spannable.removeSpan(existingSpan);
            }
        } else {
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        editText.setText(spannable);
    }

    private void alignTextLeft() {
        // Align the selected text to the left
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());
        AlignmentSpan.Standard span = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL);

        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        editText.setText(spannable);
    }

    private void alignTextCenter() {
        // Align the selected text to the center
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());
        AlignmentSpan.Standard span = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER);

        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        editText.setText(spannable);
    }

    private void alignTextRight() {
        // Align the selected text to the right
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());
        AlignmentSpan.Standard span = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE);

        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        editText.setText(spannable);
    }

    private void increaseFontSize() {
        // Increase the font size of the selected text
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());
        RelativeSizeSpan span = new RelativeSizeSpan(1.2f);

        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        editText.setText(spannable);
    }

    private void decreaseFontSize() {
        // Decrease the font size of the selected text
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());
        RelativeSizeSpan span = new RelativeSizeSpan(0.8f);

        spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        editText.setText(spannable);
    }

    private void toggleBulletList() {
        // Toggle the selected lines between bullet list and normal text
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());
        BulletSpan span = new BulletSpan(40, getResources().getColor(R.color.orange));

        BulletSpan[] existingSpans = spannable.getSpans(start, end, BulletSpan.class);

        if (existingSpans.length > 0) {
            for (BulletSpan existingSpan : existingSpans) {
                spannable.removeSpan(existingSpan);
            }
        } else {
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        editText.setText(spannable);
    }

    private void toggleNumberedList() {
        // Toggle the selected lines between numbered list and normal text
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();

        SpannableStringBuilder spannable = new SpannableStringBuilder(editText.getText());
        BulletSpan span = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            span = new BulletSpan(40, getResources().getColor(R.color.orange), 20);
        }

        BulletSpan[] existingSpans = spannable.getSpans(start, end, BulletSpan.class);

        if (existingSpans.length > 0) {
            for (BulletSpan existingSpan : existingSpans) {
                spannable.removeSpan(existingSpan);
            }
        } else {
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        editText.setText(spannable);
    }



    private void spellCheck() {
        // Perform the spell check operation
        // ...
    }

    private boolean isStyleApplied(SpannableStringBuilder spannable, int start, int end, int style) {
        StyleSpan[] existingSpans = spannable.getSpans(start, end, StyleSpan.class);

        for (StyleSpan existingSpan : existingSpans) {
            if (existingSpan.getStyle() == style) {
                return true;
            }
        }

        return false;
    }
    private void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    private void redo() {
        if (undoManager.canRedo()) {
            undoManager.redo();
        }
    }
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            selectedImageUri = data.getData();
            imageViewArticlePicture.setImageURI(selectedImageUri);
        }
    }
    private void savePicture() {
        // Retrieve other article details from the EditText fields (author, topic, content)
        // ...

        // Get the current timestamp


        // Generate a unique filename for the image
        String imageFileName = "article_cover_" + UUID.randomUUID().toString() + ".jpg";

        // Create a reference to the image file on Firebase Storage
        StorageReference imageRef = storageReference.child("article_covers/" + imageFileName);

        // Upload the image to Firebase Storage
        if (selectedImageUri != null) {
            imageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Image upload successful, get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Create a new article object with the download URL of the image

                           imageurl=uri.toString();


                        });
                    })
                    .addOnFailureListener(e -> {
                        // Image upload failed
                        // Handle the error appropriately
                    });
        } else {
            // If no image was selected, create an article object without the image URL
            Toast.makeText(this, "image upload failed", Toast.LENGTH_SHORT).show();
        }
    }
}
