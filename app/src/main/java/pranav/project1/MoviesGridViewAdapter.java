package pranav.project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Pranav on 11/06/16.
 */
public class MoviesGridViewAdapter extends BaseAdapter {

    private static final String TAG = "MoviesGridViewAdapter";
    ArrayList<MovieDescriptionDomain> mMoviesList;
    Context mContext;
    String base_url = "http://image.tmdb.org/t/p/w185";


    public MoviesGridViewAdapter(ArrayList<MovieDescriptionDomain> moviesList, Context context){

        mMoviesList = moviesList;
        mContext = context;

    }


    @Override
    public int getCount() {
        return mMoviesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMoviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if(convertView==null){

            gridView = inflater.inflate(R.layout.movie_grid_single, null);
            ImageView movie_poster = (ImageView) gridView.findViewById(R.id.movie_poster);

            Picasso.with(mContext).load(base_url+mMoviesList.get(position).getPoster_path()).into(movie_poster);



        }else{
            gridView = convertView;
        }

        return gridView;
    }
}
