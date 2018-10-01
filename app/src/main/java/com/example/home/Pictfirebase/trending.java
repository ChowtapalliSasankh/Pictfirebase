package com.example.home.Pictfirebase;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class trending extends AppCompatActivity {
    RecyclerView recyclerView1;
    FirebaseDatabase firebaseDatabase1;
    LinearLayoutManager linearLayoutManager1;
    DatabaseReference mref1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trending);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Trending");
        linearLayoutManager1 = new LinearLayoutManager(this);
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerview2);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        firebaseDatabase1 = FirebaseDatabase.getInstance();
        mref1 = firebaseDatabase1.getReference("data");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Query queryRef = mref1.orderByChild("trending").equalTo("true");
        FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model,ViewHolder>(
                        Model.class,
                        R.layout.row,
                        ViewHolder.class,
                        queryRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getApplicationContext(),model.getTitle(),model.getDescription(),model.getImage());
                    }
                };

        recyclerView1.setAdapter(firebaseRecyclerAdapter);
    }
    private void firebaseSearch(String searchtext)
    {
        Query firebaseSearchQuery = mref1.orderByChild("title").startAt(searchtext).endAt(searchtext+"\uf8ff");
        FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                Model.class,
                R.layout.row,
                ViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Model model, int position)
            {
                recyclerView1.getRecycledViewPool().clear();
                viewHolder.setDetails(getApplicationContext(),model.getTitle(),model.getDescription(),model.getImage());
            }
        };
        recyclerView1.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                firebaseSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                firebaseSearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            startActivity(new Intent(trending.this,upload.class));
            return true;
        }
        if(id == R.id.trending)
        {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
