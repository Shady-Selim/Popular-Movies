package com.shady.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shady on 8/17/2017.
 */

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    Context mContext;
    MoviesAdapter.OnItemClickListener mItemClickListener;
    private List<MovieClass> list;

    public MoviesAdapter(Context baseContext, List<MovieClass> body) {
        this.mContext = baseContext;
        this.list = body;
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie, parent, false);
        view.setFocusable(true);
        return new MoviesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.ViewHolder holder, int position) {
        final MovieClass link = list.get(position);
        holder.movieName.setText(link.title);
        Picasso.with(mContext)
                .load(mContext.getString(R.string.thumb_path) + link.posterPath)
                .into(holder.movieImg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final MoviesAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView movieName;
        public ImageView movieImg;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            movieName = itemView.findViewById(R.id.tv);
            movieImg = itemView.findViewById(R.id.iv);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }
}
