package com.shady.popularmovies;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shady on 9/6/2017.
 */

public class ReviewsAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<ReviewClass> list;

    public ReviewsAdapter(Context context, List list) {
        super(context, R.layout.video, list);
        this.context = context;
        this.list = list;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView videoTitle;
        final ReviewClass link = list.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.video, parent, false);

        videoTitle = rowView.findViewById(R.id.videoTitle);
        videoTitle.setText(link.author);
        videoTitle.setCompoundDrawablesWithIntrinsicBounds(null,null,getContext().getResources().getDrawable(R.drawable.ic_open_in_new_black_24dp),null);
        return rowView;
    }
}
