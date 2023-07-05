package com.example.sautinews;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.AlignmentSpan;
import android.text.style.BulletSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.util.HashMap;

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

    private  UndoManager undoManager;
    private TextView textViewdesc;
    private EditText editTextArticleTitle;
    private ImageView imageViewArticlePicture;
    private long timestamp;
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
    //Save and piblish method for the Article
    public void finishArticle(String Status)
    {
        timestamp=System.currentTimeMillis();
        String title=editTextArticleTitle.getText().toString();
        String Content=editText.getText().toString();
        String currentId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef=FirebaseDatabase.getInstance().getReference();
        HashMap<Object,Object> artcleMap=new HashMap<>();
        artcleMap.put("ArticleTitle",title);
        artcleMap.put("ArticleContent",Content);
        artcleMap.put("timeStamp",timestamp);

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

}
