package com.example.sautinews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    private RecyclerView recyclerView;
    private AdapterArticle articleAdapter;
    private List<Article> articles;
    private DatabaseReference articlesRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.MyArticlerecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
getArticledata();

       return view;
    }
    public void getArticledata(){

        // Initialize the list to hold Article objects
        articles = new ArrayList<>();

        // Get a reference to the "articles" node in the Firebase Realtime Database
        articlesRef = FirebaseDatabase.getInstance().getReference("Articles");

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
                    articles.add(article);
                }

                // Create an instance of the ArticleAdapter and pass the list of articles
                articleAdapter = new AdapterArticle(articles, getActivity());

                // Set the adapter on the RecyclerView
                recyclerView.setAdapter(articleAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } )

        ;
    }
}