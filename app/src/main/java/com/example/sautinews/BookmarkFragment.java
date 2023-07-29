package com.example.sautinews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_bookmark, container, false);
        recyclerView=view.findViewById(R.id.recyclerViewBookmarks);

//        getBookmarkedArticles();
        LinearLayoutManager layoutManagerMyArticles = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerMyArticles);

        return view;
    }
//    public void getBookmarkedArticles() {
//        // Initialize the list to hold bookmarked Article objects
//        articles = new ArrayList<>();
//
//        // Get the current user's ID
//        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Get a reference to the "articlesInfo/userId" node in the Firebase Realtime Database
//        DatabaseReference articlesInfoRef = FirebaseDatabase.getInstance().getReference("articlesInfo").child(currentUserId);
//
//        // Set up a ValueEventListener to listen for changes in the data
//        articlesInfoRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Clear the existing list of bookmarked articles
//                articles.clear();
//
//                // Iterate through the dataSnapshot to retrieve article IDs that are bookmarked
//                for (DataSnapshot articleSnapshot : dataSnapshot.getChildren()) {
//                    boolean isBookmarked = articleSnapshot.child("isBookmarked").getValue(Boolean.class);
//                    if (isBookmarked) {
//                        // Get the article ID from the snapshot key
//                        String articleId = articleSnapshot.getKey();
//
//                        // Get a reference to the specific article node in "Articles/articleId/articleinfo/"
//                        DatabaseReference articleRef = FirebaseDatabase.getInstance().getReference("Articles").child(articleId);
//
//                        // Set up a ValueEventListener to fetch the article information
//                        articleRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                // Get the article data and create an Article object
//                                Article article = dataSnapshot.getValue(Article.class);
//                                if (article != null) {
//                                    articles.add(article);
//
//                                    // Create an instance of the adapterBookmark and pass the list of bookmarked articles
//                                    adapterBookmark = new AdapterBookmark(articles, getActivity());
//
//                                    // Set the adapter on the RecyclerView
//                                    recyclerView.setAdapter(adapterBookmark);
//                                    // Notify the adapter that the data has changed
//                                    adapterBookmark.notifyDataSetChanged();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                // Handle the error
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle the error
//            }
//        });
//    }
//    public void getBookmarkedArticles(){
//        // Assuming you already have the userId of the authenticated user
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Replace this with the actual authenticated user's ID
//        DatabaseReference articlesRef = FirebaseDatabase.getInstance().getReference().child("Articles");
//
//        articlesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                articleList = new ArrayList<>();
//                adapterArticle = new AdapterArticle(articleList, getActivity());
//
//                for (DataSnapshot articleSnapshot : dataSnapshot.getChildren()) {
//                    // Extract the Article data from the dataSnapshot
//                    Article article = articleSnapshot.getValue(Article.class);
//
//                    // Get the articleId from the snapshot's key
//                    String articleId = articleSnapshot.getKey();
//
//                    // Create a DatabaseReference to fetch the bookmark status for the article
//                    DatabaseReference bookmarkRef = FirebaseDatabase.getInstance().getReference().child("articleInfo").child(userId).child(articleId);
//
//                    // Check if the article is bookmarked for the current user
//                    bookmarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            boolean isBookmarked = snapshot.exists(); // Check if the bookmark data exists for this user
//
//                            // Create an ArticleWithBookmark object and add it to the list
//                            ArticleWithBookmark articleWithBookmark = new ArticleWithBookmark(article, isBookmarked);
//                            articleList.add(articleWithBookmark);
//
//                            // Update the RecyclerView when all data is fetched
//                            if (articleList.size() == dataSnapshot.getChildrenCount()) {
//                                // Set up the RecyclerView with the data
//                                recyclerView.setAdapter(adapterArticle);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle the error if necessary
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle the error if necessary
//            }
//        });
//    }

}