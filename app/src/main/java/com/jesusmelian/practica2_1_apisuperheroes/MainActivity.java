package com.jesusmelian.practica2_1_apisuperheroes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jesusmelian.practica2_1_apisuperheroes.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText search_box;
    TextView urlDisplay;
    TextView searchResults;
    TextView errorMessageDisplay;
    ProgressBar requestProgress;

    public class GitHubQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            requestProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            String gitHubSearchResolve = null;
            try {
                gitHubSearchResolve = NetworkUtils.getResponseFromHttpUrl(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return gitHubSearchResolve;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("MAIN ACTIVITY", "onPostExecute: " +s);
            if (s != null && !s.equals("")){
                showJsonData();
                searchResults.setText(s);
            }else{
                showErrorMessage();
            }
            super.onPostExecute(s);
        }
    }
    //Para importar el menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //Siempre hay que devolver true para que se muestre el menú
        return true;
    }

    //obtener el item pulsado del menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.search){
            NetworkUtils network = new NetworkUtils();

            //Aqui si no le pasas nada no filtra y si no filtra por otro filtra por otro id
            URL githubUrl = NetworkUtils.buildUrl(search_box.getText().toString());
            urlDisplay.setText(githubUrl.toString());

            new GitHubQueryTask().execute(githubUrl);


        }
        if(itemId == R.id.clear){
            clear();
        }
        //Devuelve true para que se ejecute4
        return true;
    }

    private void showJsonData() {
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        searchResults.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        searchResults.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void clear(){
        urlDisplay.setText("Github URL HERE");
        searchResults.setText("Your results Here");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlDisplay = (TextView) findViewById(R.id.url_display);
        searchResults = (TextView) findViewById(R.id.github_search_results);
        search_box = (EditText) findViewById(R.id.search_box);
        errorMessageDisplay = (TextView) findViewById(R.id.error_message_display);
        requestProgress = (ProgressBar) findViewById(R.id.request_progress);
        //searchResults.setText("HIOLAAA");
    }
}