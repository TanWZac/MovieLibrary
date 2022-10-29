package com.example.movielibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.movielibrary.provider.MovieViewModel;

import java.util.Locale;

public class Filter extends AppCompatActivity {
    int flag;
    Button btn;
    EditText genre, minYear, maxYear;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.filter);

        btn = findViewById(R.id.SubmitFilter);
        genre = findViewById(R.id.filterGenre);
        minYear = findViewById(R.id.editTextNumberMin);
        maxYear = findViewById(R.id.editTextNumber2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                flag = 1;
                Intent intent = new Intent(view.getContext(), recycle.class);
                if (genre.getText().toString().length() > 0){
                    intent.putExtra("intVar", genre.getText().toString());
                }
                else if (Integer.parseInt(minYear.getText().toString())>0 &&
                        Integer.parseInt(maxYear.getText().toString())>0 &&
                        minYear != null && maxYear != null){
                    Bundle bundle = new Bundle();
                    bundle.putString("min", minYear.getText().toString());
                    bundle.putString("max", maxYear.getText().toString());
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });
    }
}
