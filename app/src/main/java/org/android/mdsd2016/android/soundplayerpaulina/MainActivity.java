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
        mSongList.add(new Song("1", "Brazil Samba.mp3", "04:00", "Brazil", "Samba is a Brazilian musical genre and dance style, with its roots in Africa via the West African slave trade and African religious traditions, particularly of Angola", 14.2350f, 51.9253f));
        mSongList.add(new Song("2", "Country Boy.mp3", "03:27", "USA", "Country music is a genre of American popular music that originated in the Southern United States in the 1920s", 37.0902f, 95.7129f));
        mSongList.add(new Song("3", "India.mp3", "04:13", "India", "The music of India includes multiple varieties of folk music, pop, and Indian classical music. India's classical music tradition, including Hindustani music and Carnatic, has a history spanning millennia and developed over several eras", 56.667083f, -110.101676f));
        mSongList.add(new Song("4", "Little Planet.mp3", "06:36", "Iceland", "The music of Iceland includes vibrant folk and pop traditions. Well-known artists from Iceland include medieval music group Voces Thules, alternative rock band The Sugarcubes, singers Björk and Emiliana Torrini, post-rock band Sigur Rós and indie folk/indie pop band Of Monsters and Men", 20.5937f, 78.9629f));
        mSongList.add(new Song("4", "Psychadellic.mp3", "03:56", "South Korea", " The Music of South Korea has evolved over the course of the decades since the end of the Korean War, and has its roots in the music of the Korean people, who have inhabited the Korean peninsula for over a millennium. Contemporary South Korean music can be divided into three different main categories: Traditional Korean folk music, popular music, or K-pop, and Western-influenced non-popular music ", 35.9078f, 127.7669f));
        mSongList.add(new Song("5", "Relaxing.mp3", "04:48", "Indonesia", "The music of Indonesia demonstrates its cultural diversity, the local musical creativity, as well as subsequent foreign musical influences that shaped contemporary music scenes of Indonesia. Nearly thousands of Indonesian islands having its own cultural and artistic history and character", 0.7893f, 113.9213f));
        mSongList.add(new Song("6", "The Elevator Bossa Nova.mp3", "04:14", "Brazil", " Samba is a Brazilian musical genre and dance style, with its roots in Africa via the West African slave trade and African religious traditions, particularly of Angola ", 14.2350f, 51.9253f));

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

        /* To simplify touch events on recycler view, I've used a class called RecyclerItemClickListener
           It's just to simplify the touch events. I've used to to get to two main click events:
           onItemClick() and onItemLongClick(). If you have any doubt about that class, feel free to contact me
        */
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
        //Running the below code on a separate thread so that main thread doesn't take much load.
        //You can remove the thread code and it will still work fine
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentPosition = position;
                //The if statement handles if we should resume the current song or play the specified song chosen by user
                //current song variable holds the current song title. If user chooses a song which matches current song and has been
                //played once then we resume it
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

    /*
    Updates player control buttons depending upon the song playing.
    If first song is playing, previous button is disabled,
    if last song is playing, next button is disabled
    Here again we are changing resource of play button because click on list item invokes this method
     */
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
