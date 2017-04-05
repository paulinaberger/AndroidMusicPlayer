package org.android.mdsd2016.android.soundplayerpaulina;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    RecyclerView songListView;
    List<Song> mSongList;
    SongListAdapter mSongListAdapter;
    DividerItemDecoration mDividerItemDecoration;
    LinearLayoutManager mLayoutManager;
    MediaPlayer mediaPlayer;
    ImageButton btnPlay, btnPrev, btnNext;
    int currentPosition = 0, length = 0;
    boolean playedAtLeastOnce = false;
    String currentSong, chosenSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing views and other classes
        init();

        //Adding objects to the list
        mSongList.add(new Song("1", "song1.mp3", "04:00", "CAD", "Dummytex Dummytex Dummytex Dummytex Dummytex Dummytex ", 56.667083f, -110.101676f));
        mSongList.add(new Song("2", "song2.mp3", "03:27", "CHILE", "Dummytex Dummytex Dummytex Dummytex Dummytex Dummytex ", -28.132082f, -70.375113f));
        mSongList.add(new Song("3", "song3.mp3", "04:13", "CAD", "Dummytex Dummytex Dummytex Dummytex Dummytex Dummytex ", 56.667083f, -110.101676f));

        //Setting up adapter and recycler view
        mSongListAdapter = new SongListAdapter(this, mSongList);
        mLayoutManager = new LinearLayoutManager(this);
        songListView = (RecyclerView) findViewById(R.id.recycler_view);
        songListView.setLayoutManager(mLayoutManager);
        songListView.setAdapter(mSongListAdapter);
        //This code right here is used to add divider lines to the list
        mDividerItemDecoration = new DividerItemDecoration(songListView.getContext(),
                mLayoutManager.getOrientation());
        songListView.addItemDecoration(mDividerItemDecoration);

        songListView.addOnItemTouchListener(new RecyclerItemClickListener(this, songListView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                chosenSong = mSongList.get(position).getTitle();
                playSongAt(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking if music is already playing and accordingly changing image and getting song length in case of pause
                if (mediaPlayer.isPlaying()) {
                    btnPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                    playedAtLeastOnce = true;
                    mediaPlayer.pause();
                    length = mediaPlayer.getCurrentPosition();
                } else {
                    btnPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                    playSongAt(currentPosition);
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Easy if statement checks
                if (currentPosition < mSongList.size() && currentPosition > 0) {
                    playSongAt(currentPosition - 1);
                } else if (currentPosition == 0) {
                    updatePlayerControls();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking which song is playing
                if (currentPosition >= 0 && currentPosition < mSongList.size() - 1) {
                    playSongAt(currentPosition + 1);
                } else if (currentPosition == mSongList.size() - 1) {
                    updatePlayerControls();
                }
            }
        });
    }

    public void init() {
        btnPlay = (ImageButton) findViewById(R.id.btn_play);
        btnPrev = (ImageButton) findViewById(R.id.btn_prev);
        btnNext = (ImageButton) findViewById(R.id.btn_next);
        mediaPlayer = new MediaPlayer();
        mSongList = new ArrayList<>();
    }

    private void playSongAt(final int position) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentPosition = position;

                if (!mediaPlayer.isPlaying() && playedAtLeastOnce && chosenSong.equals(currentSong)) {
                    mediaPlayer.seekTo(length);
                    mediaPlayer.start();
                    updatePlayerControls();
                } else {

                    currentSong = mSongList.get(position).getTitle();

                    try {
                        AssetFileDescriptor descriptor = getAssets().openFd(currentSong);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        mediaPlayer.prepare();
                        NotificationUtil.showNotification(getApplicationContext(), mSongList.get(position));
                        descriptor.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        mediaPlayer.start();
                        updatePlayerControls();
                    }
                }
            }
        });

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playedAtLeastOnce = true;
    }


    public void updatePlayerControls() {
        if (mediaPlayer.isPlaying()) {
            playedAtLeastOnce = true;
            btnPlay.setImageResource(R.drawable.ic_pause_white_48dp);
        } else {
            btnPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        }
        if (currentPosition == mSongList.size() - 1) {
            btnNext.setImageResource(R.drawable.ic_skip_next_white_36dp_disabled);
            btnNext.setClickable(false);
        } else {
            btnNext.setImageResource(R.drawable.ic_skip_next_white_36dp);
            btnNext.setClickable(true);
        }
        if (currentPosition == 0) {
            btnPrev.setImageResource(R.drawable.ic_skip_previous_white_36dp_disabled);
            btnPrev.setClickable(false);
        } else {
            btnPrev.setImageResource(R.drawable.ic_skip_previous_white_36dp);
            btnPrev.setClickable(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_info:
                DialogUtil.showAlertDialog(this, mSongList.get(currentPosition).getComment());
                break;
            case R.id.action_map:
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("lat",mSongList.get(currentPosition).getLat());
                intent.putExtra("lng",mSongList.get(currentPosition).getLng());
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}