package com.assigment2.audioplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class MediaPlayerActivity extends Activity implements OnClickListener {

	private Button mediaButton;
	private TextView mediaText;
	private ImageButton imageButtonVolumePlus;
	private ImageButton imageButtonVolumeMinus;
	private SeekBar seekbar;
	private Intent intent;
	private float volume = 0.4f;
	private final static int ZERO = 0;
	private final static int TEN = 100;
	private final static float FULL_VOLUME = 1f;
	private final static float VOLUME_ADD = 0.1f;
	private AudioPlayerService audioPlayerService;
	boolean isBound;
	boolean ended = true;
	private ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder binder) {
			audioPlayerService = ((AudioPlayerService.LocalBinder) binder)
					.getService();
			isBound = true;
			audioPlayerService.getMediaPlayer().setOnCompletionListener(
					new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer listener) {
							mediaButton.setText("Play");
							mediaText.setText("Status:Idle");
							ended = true;
							audioPlayerService.setEnded(ended);
							doInitVolume();
						}
					});
			if (audioPlayerService != null) {
				doInitVolume();
				if (audioPlayerService.getEnded()) {
					mediaButton.setText("Play");
					mediaText.setText("Status:Idle");
				} else if (audioPlayerService.isPlaying()) {
					mediaButton.setText("Pause");
					mediaText.setText("Status:Playing");
				} else {
					mediaButton.setText("Play");
					mediaText.setText("Status:Paused");
				}
			}
		}

		public void onServiceDisconnected(ComponentName name) {
			audioPlayerService = null;
			isBound = false;
		}
	};

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
		mediaText.setText("Status:Idle");
		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		seekbar.setClickable(false);
		seekbar.setMax((int) (FULL_VOLUME * TEN));
		seekbar.setProgress((int) (volume * TEN));
		intent = new Intent(this, AudioPlayerService.class);
	}

	public void onResume() {
		super.onResume();
		doBindService();
	}

	public void onPause() {
		super.onPause();
		doUnbindService();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.audio_button:
			if (audioPlayerService.getMediaPlayer() == null) {
				return;
			} else if (audioPlayerService.isPlaying() && isBound) {
				startService(intent);
				mediaButton.setText("Play");
				mediaText.setText("Status:Paused");
				ended = false;
				audioPlayerService.setEnded(ended);
			} else if (!audioPlayerService.isPlaying() && isBound) {
				startService(intent);
				mediaButton.setText("Pause");
				mediaText.setText("Status:Playing");
				ended = false;
				audioPlayerService.setEnded(ended);
			}
			break;
		case R.id.imageButton_volume_plus:
			if (audioPlayerService.getMediaPlayer() != null) {
				volume += VOLUME_ADD;
				if (volume > FULL_VOLUME) {
					volume = FULL_VOLUME;
					audioPlayerService.setVolume(FULL_VOLUME);
					seekbar.setProgress((int) (FULL_VOLUME * TEN));
				}
				audioPlayerService.setVolume(volume);
				seekbar.setProgress((int) (volume * TEN));
			}
			break;
		case R.id.imageButton_volume_minus:
			if (audioPlayerService.getMediaPlayer() != null) {
				volume -= VOLUME_ADD;
				if (volume < ZERO) {
					volume = ZERO;
					audioPlayerService.setVolume(ZERO);
					seekbar.setProgress((int) ZERO);
				}
				audioPlayerService.setVolume(volume);
				seekbar.setProgress((int) (volume * TEN));
			}
			break;
		}
	}

	public void doBindService() {
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);
		isBound = true;
	}

	public void doUnbindService() {
		if (isBound) {
			unbindService(serviceConnection);
			isBound = false;
		}
	}
	
	public void doInitVolume(){
		volume = audioPlayerService.getVolumeCanal();
		audioPlayerService.setVolume(volume);
		seekbar.setProgress((int) (volume * TEN));
	}
}
