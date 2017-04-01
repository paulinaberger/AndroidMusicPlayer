package org.android.mdsd2016.android.soundplayerpaulina;

/**
 * Created by paulinaberger on 2017-04-01.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongViewHolder>{

    Context mContext;
    List<Song> mSongs;

    public SongListAdapter(Context context, List<Song> songs){
        mContext = context;
        mSongs = songs;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mSongs.get(position).getId());
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, null);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        //Setting values for the list
        holder.songTitle.setText(String.valueOf(mSongs.get(position).getTitle()));
        holder.songDuration.setText(String.valueOf(mSongs.get(position).getDuration()));
    }

    @Override
    public int getItemCount() {
        return (mSongs != null ? mSongs.size() : 0);
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {

        TextView songTitle;
        TextView songDuration;

        public SongViewHolder(View itemView) {
            super(itemView);
            songTitle = (TextView) itemView.findViewById(R.id.song_track_title);
            songDuration = (TextView) itemView.findViewById(R.id.song_track_duration);
        }
    }

}

