package com.assigment2.audioplayer;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player_layout);
        mediaButton = (Button)findViewById(R.id.audio_button);
        mediaText = (TextView)findViewById(R.id.status_text);
        if (savedInstanceState==null){
        	mediaPlayer = MediaPlayer.create(this, R.raw.wolfs);
        } else{
        	
        }
    }

	@Override
	public void onClick(View v) {
		if (mediaPlayer==null){
			return;
		} else if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
			mediaButton.setText("Play");
			mediaText.setText("Paused");
		} else if(!mediaPlayer.isPlaying()){
			mediaPlayer.start();
			mediaButton.setText("Pause");
			mediaText.setText("Playing");
		}
	}
	public void onStart(){
		super.onStart();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

		      @Override
		      public void onCompletion(MediaPlayer listener) {
		    	  mediaPlayer.release();
		    	  mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.wolfs);
		    	  mediaButton.setText("Play");
		    	  mediaText.setText("Idle");
		      }
		   });
	}
	
	public void onStop(){
		super.onStop();
		mediaPlayer.release();
		mediaPlayer=null;
	}
}
