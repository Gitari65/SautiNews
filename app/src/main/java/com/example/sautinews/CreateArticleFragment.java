package com.example.sautinews;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.HashMap;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateArticleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressBar progressBar;
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
    private DatabaseReference myRef,myRef1;
    private  UndoManager undoManager;
    private TextView textViewdesc;
    private EditText editTextArticleTitle;
    private ImageView imageViewArticlePicture;
    private long timestamp;
    String imageurl;
    String authorName;
    private Button buttonSave;
    private TextView txtViewPublish;
    private Uri selectedImageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    String category;
    private String[] categoryString={"sports","politics","Technology","Art","Nature","Entertainment","Fashion"};
    String Status;

    public CreateArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateArticleFragment newInstance(String param1, String param2) {
        CreateArticleFragment fragment = new CreateArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_create_article, container, false);

      

        // Initialize your EditText and ImageButton variables
        progressBar=view.findViewById(R.id.progressBarEdit);
        editText = view.findViewById(R.id.editTextArticleContent);
        imageViewArticlePicture=view.findViewById(R.id.imageViewArticleCoverPicture);
        editTextArticleTitle=view.findViewById(R.id.editTextArticleTitle);
        boldButton = view.findViewById(R.id.boldButton);
        italicButton = view.findViewById(R.id.italicButton);
        underlineButton = view.findViewById(R.id.underlineButton);
        imageButton_alignLeft =view.findViewById(R.id.imageButton_alignLeft);
        imageButton_alignCentre = view.findViewById(R.id.imageButton_alignCentre);
        imageButton_alignRight = view.findViewById(R.id.imageButton_alignRight);
        increaseFontSizeButton = view.findViewById(R.id.increaseFontSizeButton);
        decreaseFontSizeButton = view.findViewById(R.id.decreaseFontSizeButton);
        bulletListButton = view.findViewById(R.id.bulletListButton);
        numberedListButton =view. findViewById(R.id.numberedListButton);
        undoButton = view.findViewById(R.id.undoButton);
        redoButton = view.findViewById(R.id.redoButton);
        spellCheckButton = view.findViewById(R.id.spellCheckButton);
        undoManager = new UndoManager(editText);
        buttonSave=view.findViewById(R.id.button_saveArticle);
        txtViewPublish=view.findViewById(R.id.button_publishArticle);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference= firebaseStorage.getReference("CoverPictures");
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
                Status="saved";
                savePicture();
            }
        });
        // Set OnClickListener for the publish textView
        txtViewPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Status="published";
                showCategoryDialog();
            }
        });
        imageViewArticlePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

    return view;
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
    public void finishArticle(String Status,String imageUrl)
    {
        if (TextUtils.isEmpty(editTextArticleTitle.getText().toString())) {
            Toast.makeText(getContext(), "Please state the Title of your Article", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(getContext(), "Please state the Content of your Article", Toast.LENGTH_SHORT).show();
            return;
        }


        timestamp=System.currentTimeMillis();
        String title=editTextArticleTitle.getText().toString();
        String Content=editText.getText().toString();
        String currentId= FirebaseAuth.getInstance().getCurrentUser().getUid();


        myRef1= FirebaseDatabase.getInstance().getReference().child("users");
        Query usernameQuery = myRef1.orderByChild("authorId").equalTo(currentId);
        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    progressBar.setVisibility(View.VISIBLE);

                    for (DataSnapshot dataSnapshot:snapshot.getChildren())
                    {
                        User user=dataSnapshot.getValue(User.class);
                        if(user!=null)
                        {
                            authorName=user.getFullName();
                            timestamp=System.currentTimeMillis();
                            String title=editTextArticleTitle.getText().toString();
                            String Content=editText.getText().toString();
                            String currentId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            myRef=FirebaseDatabase.getInstance().getReference().child("Articles").child(Status).push();

                            String articleId = myRef.getKey();
                            HashMap<Object,Object> artcleMap=new HashMap<>();
                            artcleMap.put("articleTitle",title);
                            artcleMap.put("articleContent",Content);
                            artcleMap.put("timestamp",timestamp);
                            artcleMap.put("coverPicUrl",imageUrl);
                            artcleMap.put("authorId",currentId);
                            artcleMap.put("authorFullName",authorName);
                            artcleMap.put("articleId",articleId);
                            artcleMap.put("category",category);
                            progressBar.setVisibility(View.VISIBLE);
                            myRef.setValue(artcleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {

                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), Status, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(), "Good job☺", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getContext(),NewsHomePage.class);
                                        startActivity(intent);

                                    }else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getContext(), "Failed try again later", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext() ,"Failed try again later", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }





                }
                else {
                    Toast.makeText(getContext(), "Finish your profile for Better content creation", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        timestamp = System.currentTimeMillis();

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
                            String imageUrl = uri.toString();

                            // Use the imageUrl as needed (e.g., store it in Firebase Database)
                            // ...

                            // Call the method to finish creating the article
                            finishArticle(Status,imageUrl);
                        }).addOnFailureListener(e -> {
                            // Failed to get the download URL of the image
                            // Handle the error appropriately
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Image upload failed
                        // Handle the error appropriately
                    });
        } else {
            // If no image was selected, create an article object without the image URL
            finishArticle(Status,null);
        }
    }
    public void showCategoryDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Choose your Article Category ");
        builder.setItems(categoryString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                category=categoryString[i];
                savePicture();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

    }
}