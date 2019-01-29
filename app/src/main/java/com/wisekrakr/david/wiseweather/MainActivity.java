package com.wisekrakr.david.wiseweather;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView searchText;
    private TextView displayText;
    private TextView searchResultsText;
    private TextView errorMessageText;

    private ProgressBar loadingIndicator;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchText = findViewById(R.id.search_box);
        displayText = findViewById(R.id.search_display);
        searchResultsText = findViewById(R.id.search_results);
        errorMessageText = findViewById(R.id.error_message_display);

        loadingIndicator = findViewById(R.id.pb_loading);
        //clearButton = findViewById(R.id.button_clear);
    }

    private void makeSearchQuery(){
        String query = searchText.getText().toString();

        URL searchUrl = DataGetter.buildUrl(query);
        displayText.setText(searchUrl.toString());

        new QueryTask().execute(searchUrl);
    }

    private void showJsonDataView(){
        errorMessageText.setVisibility(View.INVISIBLE);
        searchResultsText.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage(){
        errorMessageText.setVisibility(View.VISIBLE);
        searchResultsText.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("StaticFieldLeak")
    public class QueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String searchResults = null;

            try {
                searchResults = DataGetter.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResults) {
            loadingIndicator.setVisibility(View.INVISIBLE);

            if (searchResults != null && !searchResults.equals("")){
                String parsedData = DataGetter.getDataParsed(searchResults);
                searchResultsText.setText(parsedData);
            }else {
                showErrorMessage();
            }

//            clearButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    searchResultsText.setText("");
//                }
//            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuItem = item.getItemId();

        if (selectedMenuItem == R.id.action_search){
            makeSearchQuery();
        }

        return super.onOptionsItemSelected(item);
    }

}
