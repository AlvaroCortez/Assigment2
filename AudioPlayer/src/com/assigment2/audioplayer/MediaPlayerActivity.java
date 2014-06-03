package com.assigment2.audioplayer;

import java.io.ObjectOutputStream.PutField;

import android.support.v7.app.ActionBarActivity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MediaPlayerActivity extends ActionBarActivity implements OnClickListener {

	MediaPlayer mediaPlayer = new MediaPlayer();
	Button mediaButton;
	TextView mediaText;
	private int saveCurrentPosition;
	private final static String POSITION = "currentPosition";
	private final static String PLAY = "isPlayed";
	private boolean playable;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player_layout);
        mediaButton = (Button)findViewById(R.id.audio_button);
        mediaButton.setOnClickListener(this);
        mediaText = (TextView)findViewById(R.id.status_text);
        mediaPlayer = MediaPlayer.create(this, R.raw.wolfs);
        if (savedInstanceState == null){
        	mediaPlayer.seekTo(0);
        } else if (savedInstanceState != null && savedInstanceState.containsKey(POSITION) && savedInstanceState.containsKey(PLAY)){
        	mediaPlayer.seekTo(savedInstanceState.getInt(POSITION));
        	playable = savedInstanceState.getBoolean(PLAY);
        	if (savedInstanceState.getInt(POSITION)==0){
        		mediaButton.setText("Play");
        		mediaText.setText("Status:Idle");
        	} else if (playable){
        		mediaPlayer.start();
        		mediaButton.setText("Pause");
        		mediaText.setText("Status:Played");
        	} else{
        		mediaButton.setText("Play");
        		mediaText.setText("Status:Paused");
        	}
        }
    }

	@Override
	public void onClick(View v) {
		if (mediaPlayer == null){
			return;
		} else if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
			mediaButton.setText("Play");
			mediaText.setText("Status:Paused");
		} else if(!mediaPlayer.isPlaying()){
			mediaPlayer.start();
			mediaButton.setText("Pause");
			mediaText.setText("Status:Playing");
		}
	}
	
	public void onStart(){
		super.onStart();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

		      @Override
		      public void onCompletion(MediaPlayer listener) {
		    	  mediaPlayer.release();
		    	  mediaPlayer = null;
		    	  mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wolfs);
		    	  mediaButton.setText("Play");
		    	  mediaText.setText("Idle");
		      }
		   });
	}
	
	public void onSaveInstanceState(Bundle saveInstanceState) {
		super.onSaveInstanceState(saveInstanceState);
		saveCurrentPosition = mediaPlayer.getCurrentPosition();
		playable = mediaPlayer.isPlaying();
		saveInstanceState.putInt(POSITION, saveCurrentPosition);
		saveInstanceState.putBoolean(PLAY, playable);
	}
	
	public void onStop(){
		super.onStop();
		mediaPlayer.release();
		mediaPlayer=null;
	}
}
