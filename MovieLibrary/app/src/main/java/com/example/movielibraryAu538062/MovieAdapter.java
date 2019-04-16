package com.example.movielibraryAu538062;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends ArrayAdapter<MovieModel> {
    Context ct;

    MovieAdapter(Context context,MovieModel[] Movies) {
        super(context, 0, Movies);
        ct = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final MovieModel Movies = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_view_of_movie, parent, false);
        }
        // Lookup view for data population
        TextView VDTitle = (TextView) convertView.findViewById(R.id.textView_item_Title);
        TextView VDRating = (TextView) convertView.findViewById(R.id.textView_item_IMDBRating);
        TextView VDURating = (TextView) convertView.findViewById(R.id.textView_item_UserRating);
        CheckBox VDWactched = (CheckBox) convertView.findViewById(R.id.checkBox_item_watched);
        ImageView VDIcon = (ImageView) convertView.findViewById(R.id.imageView_item_Icon);
        ConstraintLayout VDLayout = (ConstraintLayout) convertView.findViewById(R.id.layout_item_ClickHere);


        // Populate the data into the template view using the data object
        VDTitle.setText(Movies.Title);
        VDRating.setText(convertView.getResources().getString(R.string.String_IMDB) + " " + Movies.Rating);
        if (Movies.UserRating != null) {
            VDURating.setText(convertView.getResources().getString(R.string.String_User ) + " " + Double.toString(Movies.UserRating));
        }



        VDWactched.setChecked(Movies.Watched);
        VDIcon.setImageResource(Movies.Icon);

        // set clickers
        VDLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make intent
                Intent GoesTo = new Intent(ct, DetailsActivity.class);
                GoesTo.putExtra("Movie", (Parcelable) Movies);
                ct.startActivity(GoesTo);
            }
        });

        VDLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // make intent
                Intent GoesTo = new Intent(ct, EditActivity.class);
                GoesTo.putExtra("Movie", (Parcelable) Movies);
                ct.startActivity(GoesTo);
                return true;
            }
        });
        return convertView;
    }
}
