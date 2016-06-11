package pranav.project1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Pranav on 11/06/16.
 */
public class MovieDetailActivity extends AppCompatActivity {

    ImageView movie_thumbnail;
    TextView movie_title_tv, overview_tv, release_date_tv, vote_averagae_tv;
    String base_url = "http://image.tmdb.org/t/p/w500";
    String TAG = "MovieDetailActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detailed_layout);

        movie_thumbnail = (ImageView) findViewById(R.id.movie_thumbnail);
        movie_title_tv = (TextView) findViewById(R.id.movie_title_tv);
        overview_tv = (TextView) findViewById(R.id.overview_tv);
        release_date_tv = (TextView) findViewById(R.id.release_date_tv);
        vote_averagae_tv = (TextView) findViewById(R.id.vote_averagae_tv);

        Intent in = getIntent();
        MovieParcelable movie = in.getParcelableExtra("singleMovie");

        //Log.i(TAG, movie.overview);
        Log.i(TAG, base_url+movie.movie_poster);

        Picasso.with(getBaseContext()).load(base_url+movie.movie_poster).into(movie_thumbnail);
        movie_title_tv.setText(movie.title);
        overview_tv.setText(movie.overview);
        release_date_tv.setText("Release Date : "+movie.release_date);
        vote_averagae_tv.setText("Rating : " + movie.vote_average);
    }
}
