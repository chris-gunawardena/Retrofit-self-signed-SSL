package com.ibm.chris.retrofitssl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView txt = (TextView) findViewById(R.id.txt);
        UxApi.getInstance(this).getLatestAutoSuggestItems().enqueue(new Callback<AutocompleteHolder>() {
            @Override
            public void onResponse(Call<AutocompleteHolder> call, Response<AutocompleteHolder> response) {
                txt.setText(response.body().autocomplete.data.size() + "");
            }

            @Override
            public void onFailure(Call<AutocompleteHolder> call, Throwable t) {

            }
        });

    }



}
