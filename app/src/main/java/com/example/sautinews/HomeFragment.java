package com.example.sautinews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    private RecyclerView recyclerView,recyclerViewRecent,recyclerViewSuggestion;
    private AdapterArticle articleAdapter,articleAdapterRecent;
    private List<Article> articles,articlesRecent;
    private DatabaseReference articlesRef,articlesRefRecent;
    ProgressBar progressBar;
    ImageView imageViewNotifications,imageViewBookmarks,imageViewLogout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_home, container, false);
       progressBar=view.findViewById(R.id.progressBarHomeFragemnt);
        imageViewLogout=view.findViewById(R.id.imageViewLogout);

        imageViewBookmarks=view.findViewById(R.id.imageViewBookmarksHome);
        imageViewNotifications=view.findViewById(R.id.imageViewNotificationsHome);
        recyclerView = view.findViewById(R.id.MyArticlerecyclerView);
        recyclerViewRecent=view.findViewById(R.id.RecentArticleRecyclerView);
        recyclerViewSuggestion = view.findViewById(R.id.SuggestionrecyclerView);


        imageViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                // Navigate to the login screen or any other appropriate screen
                // For example:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                // Optional: Call finish() to close the current activity
            }
        });
        imageViewNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shakeAnimation(imageViewNotifications);
            }
        });
        imageViewBookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shakeAnimation(imageViewBookmarks);
                Fragment selectedFragment =new BookmarkFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment)
                        .commit();
            }
        });

        checkProfileComplete();
        // Set up LinearLayoutManager with horizontal orientation
        // Set up LinearLayoutManager with horizontal orientation for recent articles RecyclerView
        LinearLayoutManager layoutManagerRecent = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecent.setLayoutManager(layoutManagerRecent);

        // Set up LinearLayoutManager with horizontal orientation for my articles RecyclerView
        LinearLayoutManager layoutManagerMyArticles = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManagerMyArticles);
        getMyArticleData();
        getRecentArticleData();

       return view;
    }
    public void checkProfileComplete()
    {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        Query usernameQuery = usersRef.orderByChild("authorId").equalTo(userId);

        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Username already exists
                    // Handle the case accordingly
                    Intent intent=new Intent(getContext(),ProfileCompletionActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // An error occurred while retrieving the data
                // Handle the error accordingly
            }
        });

    }
    public void getMyArticleData() {
        // Initialize the list to hold Article objects
        articles = new ArrayList<>();

        // Get the current user's ID
        String currentUserId =FirebaseAuth.getInstance().getCurrentUser().getUid().toString(); // Replace this with your method to retrieve the current user's ID

        // Get a reference to the "articles" node in the Firebase Realtime Database
        articlesRef = FirebaseDatabase.getInstance().getReference("Articles").child("published");

        // Set up a ValueEventListener to listen for changes in the data
        articlesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list of articles
                articles.clear();

                // Iterate through the dataSnapshot to retrieve Article objects
                for (DataSnapshot articleSnapshot : snapshot.getChildren()) {
                    // Get the article data and create an Article object
                    Article article = articleSnapshot.getValue(Article.class);

                    // Filter the articles by authorId
                    if (article != null && article.getAuthorId().equals(currentUserId)) {
                        articles.add(article);
                    }
                }

                // Sort the list by timestamp in descending order
                Collections.sort(articles, new Comparator<Article>() {
                    @Override
                    public int compare(Article article1, Article article2) {
                        return article2.getTimestamp().compareTo(article1.getTimestamp());
                    }
                });

                // Create an instance of the ArticleAdapter and pass the list of articles
                articleAdapter = new AdapterArticle(articles, getActivity());

                // Set the adapter on the RecyclerView
                recyclerView.setAdapter(articleAdapter);
                // Notify the adapter that the data has changed
                articleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }


    public void getRecentArticleData() {
        // Initialize the list to hold Article objects
        articlesRecent = new ArrayList<>();

        // Get a reference to the "articles" node in the Firebase Realtime Database
        articlesRefRecent = FirebaseDatabase.getInstance().getReference("Articles").child("published");

        // Query the articlesRef to order the data by the "timestamp" child node in descending order
        Query query = articlesRefRecent.orderByChild("timestamp");

        // Set up a ValueEventListener to listen for changes in the data
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list of articles
                articlesRecent.clear();

                // Iterate through the dataSnapshot to retrieve Article objects
                for (DataSnapshot articleSnapshot : snapshot.getChildren()) {
                    // Get the article data and create an Article object
                    Article article = articleSnapshot.getValue(Article.class);
                    articlesRecent.add(article);
                }

                // Reverse the list to display the items from most recent to oldest
                Collections.reverse(articlesRecent);

                // Create an instance of the ArticleAdapter and pass the list of articles
                articleAdapterRecent = new AdapterArticle(articlesRecent, getActivity());

                // Set the adapter on the RecyclerView
                recyclerViewRecent.setAdapter(articleAdapterRecent);
                // Notify the adapter that the data has changed
                articleAdapterRecent.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getSuggestionArticleData() {
        // Initialize the list to hold Article objects
        articles = new ArrayList<>();
progressBar.setVisibility(View.VISIBLE);
        // Get a reference to the "articles" node in the Firebase Realtime Database
        articlesRef = FirebaseDatabase.getInstance().getReference("Suggestions").child("published");

        // Query the articlesRef to order the data by the "timestamp" child node in descending order
        Query query = articlesRef.orderByChild("timestamp");

        // Set up a ValueEventListener to listen for changes in the data
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the existing list of articles
                articles.clear();

                // Iterate through the dataSnapshot to retrieve Article objects
                for (DataSnapshot articleSnapshot : snapshot.getChildren()) {
                    // Get the article data and create an Article object
                    Article article = articleSnapshot.getValue(Article.class);
                    articles.add(article);
                }

                // Reverse the list to display the items from most recent to oldest
                Collections.reverse(articles);

                // Create an instance of the ArticleAdapter and pass the list of articles
                articleAdapter = new AdapterArticle(articles, getActivity());

                // Set the adapter on the RecyclerView
                recyclerView.setAdapter(articleAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    public  void shakeAnimation(ImageView imageView){
        Animation animation= AnimationUtils.loadAnimation(getActivity(),R.anim.shake_animation);
        imageView.startAnimation(animation);
    }

}