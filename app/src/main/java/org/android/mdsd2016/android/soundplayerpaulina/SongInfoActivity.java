package org.android.mdsd2016.android.soundplayerpaulina;

/**
 * Created by paulinaberger on 2017-04-01.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

import org.android.mdsd2016.android.soundplayerpaulina.Song;

public class SongInfoActivity extends AppCompatActivity {

    public static final String KEY = "song";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_song_info);

        Song song = (Song) getIntent().getSerializableExtra(KEY);

        ((TextView) findViewById(R.id.tv_title)).setText(song.getTitle());
        ((TextView) findViewById(R.id.tv_duration)).setText(song.getDuration());
        ((TextView) findViewById(R.id.tv_country)).setText(song.getCountry());
        ((TextView) findViewById(R.id.tv_comment)).setText(song.getComment());
    }
}
