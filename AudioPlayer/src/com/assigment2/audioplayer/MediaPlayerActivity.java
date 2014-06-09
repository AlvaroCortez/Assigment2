package com.assigment2.audioplayer;

import android.support.v7.app.ActionBarActivity;

//import android.media.AudioManager;
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

	Button mediaButton;
	TextView mediaText;
	ImageButton imageButtonVolumePlus, imageButtonVolumeMinus;
	private boolean ended;
	private float volumeLeft = 0.4f;
	private float volumeRight = 0.4f;
	private final static int ZERO = 0;
	private final static float FULL_LEFT_VOLUME = 1f, FULL_RIGHT_VOLUME = 1f;
	private final static float VOLUME_ADD = 0.1f;
	AudioPlayerSingleton audioPlayer = AudioPlayerSingleton
			.getAudioPlayerInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ended = audioPlayer.getEnded();
		setContentView(R.layout.media_player_layout);
		mediaButton = (Button) findViewById(R.id.audio_button);
		mediaButton.setOnClickListener(this);
		imageButtonVolumePlus = (ImageButton) findViewById(R.id.imageButton_volume_plus);
		imageButtonVolumePlus.setOnClickListener(this);
		imageButtonVolumeMinus = (ImageButton) findViewById(R.id.imageButton_volume_minus);
		imageButtonVolumeMinus.setOnClickListener(this);
		mediaText = (TextView) findViewById(R.id.status_text);
		mediaText.setText("Status:Idle");
		audioPlayer.create(this, R.raw.tensec);
		audioPlayer.mediaPlayer
				.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer listener) {
						mediaButton.setText("Play");
						mediaText.setText("Status:Idle");
						ended = true;
						audioPlayer.setEnded(ended);
					}
				});
		volumeLeft = audioPlayer.getLeftVolumeCanal();
		volumeRight = audioPlayer.getRightVolumeCanal();
		audioPlayer.setVolume(volumeLeft, volumeRight);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.audio_button:
			if (audioPlayer.mediaPlayer == null) {
				return;
			} else if (audioPlayer.isPlaying()) {
				audioPlayer.pause();
				mediaButton.setText("Play");
				mediaText.setText("Status:Paused");
				ended = false;
				audioPlayer.setEnded(ended);
			} else if (!audioPlayer.isPlaying()) {
				audioPlayer.play();
				mediaButton.setText("Pause");
				mediaText.setText("Status:Playing");
				ended = false;
				audioPlayer.setEnded(ended);
			}
			break;
		case R.id.imageButton_volume_plus:
			if (audioPlayer.mediaPlayer != null) {
				volumeLeft += VOLUME_ADD;
				volumeRight += VOLUME_ADD;
				if (volumeLeft > FULL_LEFT_VOLUME || volumeRight > FULL_RIGHT_VOLUME) {
					volumeLeft = FULL_LEFT_VOLUME;
					volumeRight = FULL_RIGHT_VOLUME;
					audioPlayer.setVolume(FULL_LEFT_VOLUME, FULL_RIGHT_VOLUME);
				}
				audioPlayer.setVolume(volumeLeft, volumeRight);
			}
			break;
		case R.id.imageButton_volume_minus:
			if (audioPlayer.mediaPlayer != null) {
				volumeLeft -= VOLUME_ADD;
				volumeRight -= VOLUME_ADD;
				if (volumeLeft < ZERO || volumeRight < ZERO) {
					volumeLeft = ZERO;
					volumeRight = ZERO;
					audioPlayer.setVolume(ZERO, ZERO);
				}
				audioPlayer.setVolume(volumeLeft, volumeRight);
			}
			break;
		}
	}

	public void onResume() {
		super.onResume();
		if (audioPlayer.getEnded()) {
			mediaButton.setText("Play");
			mediaText.setText("Status:Idle");
		} else if (audioPlayer.isPlaying()) {
			mediaButton.setText("Pause");
			mediaText.setText("Status:Playing");
		} else {
			mediaButton.setText("Play");
			mediaText.setText("Status:Paused");
		}
	}
}
