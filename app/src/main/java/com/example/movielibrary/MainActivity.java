package com.example.movielibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.app.ActivityCompat;

import androidx.lifecycle.ViewModelProvider;

import com.example.movielibrary.provider.Movie;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import com.example.movielibrary.provider.MovieViewModel;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button response;
    Button clear;

    DatabaseReference mRef;
    DatabaseReference mCondition;

    int flag;
    private EditText Title, Year, Country, Genre, Cost, Keyword;
    private String title, country, genre, keyword;
    private int year, cost;

    private long timeCheckInterval = 1;

    private static final String TAG = "Lifecycle";

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private ListView listView;

    MyRecyclerAdapter Adapter;
    ArrayList<Movie> data = new ArrayList<Movie>();

    private static final String KEY_MOVIE = "Movie";
    private static String KEY_TITLE = "TITLE";
    private static int KEY_YEAR = 0;
    private static String KEY_COUNTRY = "COUNTRY";
    private static String KEY_GENRE = "GENRE";
    private static int KEY_COST = 0;
    private static String KEY_KEYWORD = "KEYWORD";

    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    Toolbar toolbar;

    private MovieViewModel movieViewModel;
    private final BroadCast broadCast = new BroadCast();

    private GestureDetectorCompat mDetector;

    private int x,y;

    private long here;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);
        Log.d(TAG, "onCreate");

        TextView name = findViewById(R.id.Name);
        name.setText("Zac");
        name.getText();

        View viewer = findViewById(R.id.frame_layout_id);
        Title = findViewById(R.id.EditTextTitle);
        Year = findViewById(R.id.EditTextYear);
        Country = findViewById(R.id.EditTextCountry);
        Genre = findViewById(R.id.EditTextGenre);
        Keyword = findViewById(R.id.EditTextMultilineKeyword);
        Cost = findViewById(R.id.EditTextCost);

        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRef = FirebaseDatabase.getInstance().getReference();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close); //strings.xml
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // below code is for the main activity list view
        listView = findViewById(R.id.listViewContent);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);


        Detect mDetect = new Detect();
        mDetector = new GestureDetectorCompat(this, mDetect);
        mDetector.setOnDoubleTapListener(mDetect);

        sendData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddMovieInfo();
                Log.d(TAG, "onClick: "+movieViewModel.getAllMovie().toString());
                Snackbar.make(view, "Movie has been added to list", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { RemoveLast(); }
                }).show();
            }
        });

        response = (Button) findViewById(R.id.response);
        response.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { AddMovieInfo(); }
        });

        clear = findViewById(R.id.ClearButton);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearMovieInfo();
            }
        });

        viewer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                onTouchEvent(event);
//                or
                mDetector.onTouchEvent(event);
                int action = event.getActionMasked();
                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        x = (int) event.getX();
                        y = (int) event.getY();
                        here = System.currentTimeMillis();
                        break;
                    case (MotionEvent.ACTION_UP):
                        float finalX = event.getX();
                        float finalY = event.getY();

                        int distX = (int) (finalX - x);
                        int distY = (int) (finalY - y);
                        long check = (System.currentTimeMillis() - here);
                        if (Math.abs(distX) > Math.abs(distY)) {
                            if (x < finalX){ // left to right
                                if (check<250 && check > 150){
                                    Log.i(TAG, "onTouch: "+check);
                                    AddMovieInfo();
                                }

                            }
                        }else{
                            if (y < finalY && distY != 0){ // up to down
                                ClearMovieInfo();
                            }
                        }
                        return true;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mDetector.onTouchEvent(event);
        return true;
    }

///////// Shared Preference /////////

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putString("TITLE", Title.getText().toString());
        if (Year != null && !Year.getText().toString().isEmpty()) {
            outState.putInt("YEAR", Integer.parseInt(Year.getText().toString()));
        }
        outState.putString("COUNTRY", Country.getText().toString());
        outState.putString("GENRE", Genre.getText().toString());
        outState.putString("KEYWORD", Keyword.getText().toString());
        if (Cost != null && !Cost.getText().toString().isEmpty()) {
            outState.putInt("COST", Integer.parseInt(Cost.getText().toString()));
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState");
        title = savedInstanceState.getString("TITLE", "").toUpperCase();
        Title.setText(title);
        year = savedInstanceState.getInt("YEAR");
        country = savedInstanceState.getString("COUNTRY", "");
        genre = savedInstanceState.getString("GENRE", "").toLowerCase();
        Genre.setText(genre);
        keyword = savedInstanceState.getString("KEYWORD", "");
        cost = savedInstanceState.getInt("COST", 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//////// private functions /////////

    private void sendData() {
        Adapter = new MyRecyclerAdapter();
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getAllMovie().observe(this, newData ->{
            Adapter.setMovies(newData);
            Adapter.notifyDataSetChanged();
        });
    }

    public void AddMovieInfo(){

        KEY_TITLE = Title.getText().toString();
        if (Year != null && !Year.getText().toString().isEmpty()) {
            KEY_YEAR = Integer.parseInt(Year.getText().toString());
        }
        KEY_COUNTRY = Country.getText().toString();
        KEY_GENRE = Genre.getText().toString();
        KEY_KEYWORD = Keyword.getText().toString();
        if (Cost != null && !Cost.getText().toString().isEmpty()) {
            KEY_COST = Integer.parseInt(Cost.getText().toString());
        }
        Movie movie = new Movie(KEY_TITLE,KEY_YEAR, KEY_COUNTRY,KEY_GENRE, KEY_KEYWORD, KEY_COST);
        movieViewModel.insert(movie); // internal database
        mCondition.push().setValue(movie);  // firebase
        listItems.add(KEY_TITLE+" | "+KEY_YEAR);    // display as list Items
        adapter.notifyDataSetChanged();     // notify changes on arraylist
        data.add(movie);    // insert to arraylist
    }

    private void RemoveLast(){
        adapter.remove(adapter.getItem(adapter.getCount() - 1));
        adapter.notifyDataSetChanged();
        movieViewModel.deleteLastMovie();
    }

    private void RemoveAll(){
        movieViewModel.deleteAll();
        mCondition.setValue(null);
    }

    private void ClearMovieInfo(){
        Title.setText("");
        Year.setText("0");
        Country.setText("");
        Genre.setText("");
        Keyword.setText("");
        Cost.setText("0");
    }

/////// Lifecycles ////////

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        // request permission from the user to access send sms, receive sms and read sms
        registerReceiver(broadCast, new IntentFilter(SMSReceiver.SMS_FILTER)); // IntentFilter can fetch activity information on os or other app activities
        // register the receiver
        mCondition = mRef.child("status");
        mCondition.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                data.add(dataSnapshot.getValue(Movie.class));
                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { mCondition.setValue(null); }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
            });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        unregisterReceiver(broadCast);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

///////// BroadCast Section ////////

    class BroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);    // receive the string using the key

            StringTokenizer sT = new StringTokenizer(msg, ";");
            String titleMsg = sT.nextToken();       // accessing the next item(token)
            String YearMsg = sT.nextToken();
            String CountryMsg = sT.nextToken();
            String GenreMsg = sT.nextToken();
            String CostMsg = sT.nextToken();
            String KeywordMsg = sT.nextToken();
            String AddCost = sT.nextToken();
            Integer val = Integer.parseInt(AddCost) + Integer.parseInt(CostMsg);

            Title.setText(titleMsg);
            Year.setText(String.valueOf(YearMsg));
            Country.setText(CountryMsg);
            Genre.setText(GenreMsg);
            Cost.setText(String.valueOf(val));
            Keyword.setText(KeywordMsg);
        }
    }

///////// Menu Function ///////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optional_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_fields) {
            ClearMovieInfo();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addMovie) {
            AddMovieInfo();
        } else if (id == R.id.popMovie) {
            if (adapter.getCount() > 0 && Adapter.getItemCount() > 0) {
                RemoveLast();
            }
        } else if (id == R.id.clearMovie) {
            adapter.clear();
            adapter.notifyDataSetChanged();
            RemoveAll();
        } else if (id == R.id.listAllMovies) {
            Intent intent = new Intent(this, recycle.class);
            startActivity(intent);
        } else if (id == R.id.filter){
            Intent intent = new Intent(this, Filter.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


///////// Gesture Detector ///////////

    private class Detect extends GestureConvenience {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Cost = findViewById(R.id.EditTextCost);
            if (Cost != null && !Cost.getText().toString().isEmpty()) {
                KEY_COST = Integer.parseInt(Cost.getText().toString());
            }
            else{
                KEY_COST = 0;
            }
            KEY_COST += 150;
            Cost.setText(Integer.toString(KEY_COST));
            int x = (int) e.getX();
            int y = (int) e.getY();
            if ( (0 <= x && x < 50) && ( 0 <= y && y < 50 )) {
                flag = 1;
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            int x = (int) e.getX();
            int y = (int) e.getY();
            if (flag == 1 && (1000 <= x && x < 1100) && ( 250 <= y && y < 300 ) ){
                Title.setText("STAR WARS");
                flag = 0;
            }
            else {
                Title.setText("Star Wars");
            }
            Year.setText("2000");
            Country.setText("US");
            Genre.setText("Sci-fi");
            Keyword.setText("Lucas");
            Cost.setText("100");
            flag = 0;
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            long check = System.currentTimeMillis() - here;
            if( check<150){
                moveTaskToBack(true);
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            ClearMovieInfo();
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            float distX = motionEvent1.getX() - motionEvent.getX();
            long check = System.currentTimeMillis() - here;
            if (v != 0 && v1 == 0 && check > 250){
                Year = findViewById(R.id.EditTextYear);
                if (Year != null && !Year.getText().toString().isEmpty()) {
                    KEY_YEAR = Integer.parseInt(Year.getText().toString());
                }
                else{
                    KEY_YEAR = 0;
                }
                KEY_YEAR += distX/100;
                if (KEY_YEAR >= 2022){
                    KEY_YEAR = 2022;
                }
                else if (KEY_YEAR <= 1888){
                    KEY_YEAR = 1888;
                }
                Year.setText(Integer.toString(KEY_YEAR));
                return true;
            }
            else{
                return false;
            }
        }


    }
}

