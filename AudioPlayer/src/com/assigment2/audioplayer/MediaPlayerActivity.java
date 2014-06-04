package com.assigment2.audioplayer;

import android.support.v7.app.ActionBarActivity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MediaPlayerActivity extends ActionBarActivity implements
		OnClickListener {

	MediaPlayer mediaPlayer = new MediaPlayer();
	Button mediaButton;
	TextView mediaText;
	ImageButton imageButtonVolumePlus, imageButtonVolumeMinus;
	private int saveCurrentPosition;
	private final static String POSITION = "currentPosition";
	private final static String PLAY = "isPlayed";
	private final static String VOLUME_LEFT = "leftVolume";
	private final static String VOLUME_RIGHT = "rightVolume";
	private boolean playable;
	private float volumeLeft = 0.4f;
	private float volumeRight = 0.4f;
	private final static int ZERO = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_player_layout);
		mediaButton = (Button) findViewById(R.id.audio_button);
		mediaButton.setOnClickListener(this);
		imageButtonVolumePlus = (ImageButton) findViewById(R.id.imageButton_volume_plus);
		imageButtonVolumePlus.setOnClickListener(this);
		imageButtonVolumeMinus = (ImageButton) findViewById(R.id.imageButton_volume_minus);
		imageButtonVolumeMinus.setOnClickListener(this);
		mediaText = (TextView) findViewById(R.id.status_text);
		mediaPlayer = MediaPlayer.create(this, R.raw.tensec);
		mediaPlayer.setScreenOnWhilePlaying(true);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer listener) {
				/*
				 * mediaPlayer.release(); mediaPlayer = null; mediaPlayer =
				 * MediaPlayer.create(getApplicationContext(), R.raw.tensec);
				 */
				mediaButton.setText("Play");
				mediaText.setText("Status:Idle");
			}
		});
		if (savedInstanceState == null) {
			mediaPlayer.seekTo(ZERO);
			mediaPlayer.setVolume(volumeLeft, volumeRight);
		} else if (savedInstanceState != null
				&& savedInstanceState.containsKey(POSITION)
				&& savedInstanceState.containsKey(PLAY)
				&& savedInstanceState.containsKey(VOLUME_LEFT)
				&& savedInstanceState.containsKey(VOLUME_RIGHT)) {
			mediaPlayer.seekTo(savedInstanceState.getInt(POSITION));
			playable = savedInstanceState.getBoolean(PLAY);
			mediaPlayer.setVolume(savedInstanceState.getFloat(VOLUME_LEFT),
					savedInstanceState.getFloat(VOLUME_RIGHT));
			if (savedInstanceState.getInt(POSITION) == ZERO) {
				mediaButton.setText("Play");
				mediaText.setText("Status:Idle");
			} else if (playable) {
				mediaPlayer.start();
				mediaButton.setText("Pause");
				mediaText.setText("Status:Played");
			} else {
				mediaButton.setText("Play");
				mediaText.setText("Status:Paused");
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.audio_button:
			if (mediaPlayer == null) {
				return;
			} else if (mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
				mediaButton.setText("Play");
				mediaText.setText("Status:Paused");
			} else if (!mediaPlayer.isPlaying()) {
				mediaPlayer.start();
				mediaButton.setText("Pause");
				mediaText.setText("Status:Playing");
			}
			break;
		case R.id.imageButton_volume_plus:
			if (mediaPlayer != null) {
				volumeLeft += 0.1f;
				volumeRight += 0.1f;
				if (volumeLeft > 1 || volumeRight > 1) {
					volumeLeft = 1.0f;
					volumeRight = 1.0f;
					mediaPlayer.setVolume(1.0f, 1.0f);
				}
				mediaPlayer.setVolume(volumeLeft, volumeRight);
			}
			break;
		case R.id.imageButton_volume_minus:
			if (mediaPlayer != null) {
				volumeLeft -= 0.1f;
				volumeRight -= 0.1f;
				if (volumeLeft < ZERO || volumeRight < ZERO) {
					volumeLeft = ZERO;
					volumeRight = ZERO;
					mediaPlayer.setVolume(ZERO, ZERO);
				}
				mediaPlayer.setVolume(volumeLeft, volumeRight);
			}
			break;
		}
	}

	public void onStart() {
		super.onStart();
	}
	public void onPause(){
		super.onPause();
		mediaPlayer.start();
		mediaPlayer.setVolume(volumeLeft, volumeRight);
	}

	public void onSaveInstanceState(Bundle saveInstanceState) {
		super.onSaveInstanceState(saveInstanceState);
		saveCurrentPosition = mediaPlayer.getCurrentPosition();
		playable = mediaPlayer.isPlaying();
		saveInstanceState.putInt(POSITION, saveCurrentPosition);
		saveInstanceState.putBoolean(PLAY, playable);
		saveInstanceState.putFloat(VOLUME_LEFT, volumeLeft);
		saveInstanceState.putFloat(VOLUME_RIGHT, volumeRight);
	}

	public void onStop() {
		super.onStop();
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
