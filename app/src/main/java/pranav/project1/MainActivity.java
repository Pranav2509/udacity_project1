package pranav.project1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progress;
    private String TAG = "MainActivity";
    GridView movies_gv;
    String api_base = "https://api.themoviedb.org/3/movie/";
    String api_key = "api_key=1ec7f215ae2cfb2db9229dd326f48885";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_movies_layout);

        movies_gv = (GridView) findViewById(R.id.movies_gv);
        performWebCall();
    }

    private void performWebCall() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPreferences.getString("orderby","popular");

        Log.i(TAG,api_base+orderBy+"?"+api_key);

        new FetchMovieDetails().execute(api_base+orderBy+"?"+api_key);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.movie_review_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.setting_menu :
                Intent in = new Intent(this, SettingsPreferences.class);
                startActivity(in);


                return true;

            case R.id.refresh_1 :
               performWebCall();


                return true;

            default: return super.onOptionsItemSelected(item);

        }


    }

    public class FetchMovieDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;
            String response="";

            try{
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");

                int status = urlConnection.getResponseCode();

                if(status == 200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";


                    while ((line = br.readLine())!=null){
                        response +=line;
                    }


                }
            }catch (Exception e){
                e.printStackTrace();
            }


            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();

            if(s!=""){

                try {
                    final ArrayList<MovieDescriptionDomain> movieList = ResponseParser.parseMovieDetails(s);

                    if(movieList!=null){

                        MoviesGridViewAdapter adapter = new MoviesGridViewAdapter(movieList, getBaseContext());
                        movies_gv.setAdapter(adapter);

                        movies_gv.setOnItemClickListener(new GridView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Intent in = new Intent(MainActivity.this, MovieDetailActivity.class);
                                MovieDescriptionDomain movie = movieList.get(position);
                                MovieParcelable movieParcelable = new MovieParcelable(movie.getTitle(),
                                        movie.getRelease_date(),
                                        movie.getThumbnail_link(),
                                        movie.getVote_average(),
                                        movie.getOverview());
                                in.putExtra("singleMovie", movieParcelable);
                                startActivity(in);

                            }
                        });

                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }


        }
    }

    private void dismissProgressDialog() {
        if(progress!=null){
            progress.dismiss();
            progress=null;
        }

    }

    private void showProgressDialog() {
        if(progress==null) {
            progress = new ProgressDialog(this);
            if(!isFinishing())
                progress.show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //performWebCall();
    }
}
