package com.example.sautinews;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookmarkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
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
  List<Article> articles;
    List<Article> bookmarkArticles;
    DatabaseReference articlesRef;
    AdapterBookmark adapterBookmark;
    RecyclerView recyclerView;
    LottieAnimationView lottieAnimationView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_bookmark, container, false);
        recyclerView=view.findViewById(R.id.recyclerViewBookmarks);
lottieAnimationView=view.findViewById(R.id.animation_view4);
lottieAnimationView.setVisibility(View.GONE);
//        getBookmarkedArticles();
        LinearLayoutManager layoutManagerMyArticles = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerMyArticles);
getBookmarkedArticles();
        return view;
    }
    public void getBookmarkedArticles() {
        // Initialize the list to hold Article objects
        bookmarkArticles = new ArrayList<>();

        // Get the current user's ID
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get a reference to the "articlesInfo" node in the Firebase Realtime Database for the current user
        DatabaseReference articlesInfoRef = FirebaseDatabase.getInstance().getReference("articlesInfo").child(currentUserId);

        // Set up a ValueEventListener to listen for changes in the data
        articlesInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list of bookmarked articles
                bookmarkArticles.clear();

                // Iterate through the dataSnapshot to retrieve the bookmarked article IDs
                for (DataSnapshot articleSnapshot : snapshot.getChildren()) {
                    Article article2 = articleSnapshot.getValue(Article.class);

                    if (article2 != null) {
                        boolean isBookmarked = article2.isBookmarked();
                        String articleId = articleSnapshot.getKey();
                        Log.i(TAG, "onDataChange: "+articleId);
                        // If the article is bookmarked, fetch the article information from the "Articles" node
                        if (isBookmarked) {
                            // Get a reference to the "Articles" node in the Firebase Realtime Database
                            DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference("").child("Articles").child("published");


                            // Set up a ValueEventListener to fetch the article information
                            articlesRef.child(articleId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // Get the article data and create an Article object
                                    Article article = dataSnapshot.getValue(Article.class);

                                    if (article != null) {
                                        bookmarkArticles.add(article);

                                        // Sort the list by timestamp in descending order
                                        Collections.sort(bookmarkArticles, new Comparator<Article>() {
                                            @Override
                                            public int compare(Article article1, Article article2) {
                                                return article2.getTimestamp().compareTo(article1.getTimestamp());
                                            }
                                        });

                                        // Create an instance of the AdapterBookmark and pass the list of articles
                                        adapterBookmark = new AdapterBookmark(bookmarkArticles, getActivity());

                                        // Set the adapter on the RecyclerView
                                        recyclerView.setAdapter(adapterBookmark);

                                        // Notify the adapter that the data has changed
                                        adapterBookmark.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle the error if needed
                                    lottieAnimationView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }
                    // Get the value of 'isBookmarked' field for this article
else {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }


}