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
	private final static int FULL_VOLUME = 1;
	private final static float VOLUME_ADD = 0.1f;
	private AudioPlayerService audioPlayerService;
	boolean isBound;
	private static String TAG = "Assigment2";
	// boolean ended = true;
	private ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder binder) {
			audioPlayerService = ((AudioPlayerService.LocalBinder) binder)
					.getService();
			isBound = true;
			/*
			 * audioPlayerService.getMediaPlayer().setOnCompletionListener( new
			 * OnCompletionListener() {
			 * 
			 * @Override public void onCompletion(MediaPlayer listener) {
			 * initUI(audioPlayerService.getState()); //
			 * mediaButton.setText("Play"); // mediaText.setText("Status:Idle");
			 * // ended = true; // audioPlayerService.setEnded(ended);
			 * doInitVolume(); Log.d(TAG, "Activity OnComplitionListener"); }
			 * });
			 */
			if (audioPlayerService != null) {
				doInitVolume();
				/*
				 * if (audioPlayerService.getEnded()) {
				 * mediaButton.setText("Play");
				 * mediaText.setText("Status:Idle"); } else if
				 * (audioPlayerService.isPlaying()) {
				 * mediaButton.setText("Pause");
				 * mediaText.setText("Status:Playing"); } else {
				 * mediaButton.setText("Play");
				 * mediaText.setText("Status:Paused"); }
				 */
				initUI(audioPlayerService.getState());
			}
			Log.d(TAG, "Service Connected");
		}

		public void onServiceDisconnected(ComponentName name) {
			audioPlayerService = null;
			isBound = false;
			Log.d(TAG, "Service Disconnected");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Activity Begin created");
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
		Log.d(TAG, "Activity End created");
	}

	public void onResume() {
		super.onResume();
		Log.d(TAG, "Activity onResumeBegin");
		doBindService();
		Log.d(TAG, "Activity onResumeEnd");
	}

	public void onPause() {
		super.onPause();
		Log.d(TAG, "Activity onPauseBegin");
		doUnbindService();
		Log.d(TAG, "Activity onPauseEnd");
	}

	@Override
	public void onClick(View v) {
		Log.d(TAG, "Activity onClick");
		switch (v.getId()) {
		case R.id.audio_button:
			/*
			 * if (audioPlayerService.getMediaPlayer() == null) { return; } else
			 * if (audioPlayerService.isPlaying() && isBound) {
			 * startService(intent); //mediaButton.setText("Play");
			 * //mediaText.setText("Status:Paused"); //ended = false;
			 * //audioPlayerService.setEnded(ended); } else if
			 * (!audioPlayerService.isPlaying() && isBound) {
			 * startService(intent); //mediaButton.setText("Pause");
			 * //mediaText.setText("Status:Playing"); //ended = false;
			 * //audioPlayerService.setEnded(ended); }
			 */
			if (isBound) {
				switch (audioPlayerService.getState()) {
				case 0:
				case 1:
					Log.d(TAG, "Clicked StartButton Begin");
					startService(intent.putExtra("State", "ON"));
					initUI(audioPlayerService.getState());
					Log.d(TAG, "Clicked StartButton End");
					break;
				case 2:
					Log.d(TAG, "Clicked PauseButton Begin");
					startService(intent.putExtra("State", "OFF"));
					initUI(audioPlayerService.getState());
					Log.d(TAG, "Clicked PauseButton End");
					break;
				}
			}
			break;
		case R.id.imageButton_volume_plus:
			Log.d(TAG, "Clicked VolumePlustButton Begin");
			setVolume(FULL_VOLUME);
			Log.d(TAG, "Clicked VolumePlustButton End");
			break;
		case R.id.imageButton_volume_minus:
			Log.d(TAG, "Clicked VolumeMinustButton Begin");
			setVolume(ZERO);
			Log.d(TAG, "Clicked VolumeMinustButton End");
			break;
		}
	}

	public void doBindService() {
		Log.d(TAG, "doBindService Begin");
		bindService(intent, serviceConnection, BIND_AUTO_CREATE);
		isBound = true;
		Log.d(TAG, "doBindService End");
	}

	public void doUnbindService() {
		Log.d(TAG, "doUnBindService Begin");
		if (isBound) {
			unbindService(serviceConnection);
			isBound = false;
			Log.d(TAG, "doUnBindService End");
		}
	}

	public void doInitVolume() {
		volume = audioPlayerService.getVolumeCanal();
		audioPlayerService.setVolume(volume);
		seekbar.setProgress((int) (volume * TEN));
	}

	private void setVolume(int state) {
		// if (audioPlayerService.getMediaPlayer() != null) {
		switch (state) {
		case ZERO:
			volume -= VOLUME_ADD;
			if (volume < ZERO) {
				volume = ZERO;
				audioPlayerService.setVolume(ZERO);
				seekbar.setProgress((int) ZERO);
			}
			break;
		case FULL_VOLUME:
			volume += VOLUME_ADD;
			if (volume > FULL_VOLUME) {
				volume = FULL_VOLUME;
				audioPlayerService.setVolume(FULL_VOLUME);
				seekbar.setProgress((int) (FULL_VOLUME * TEN));
			}
			break;
		}
		audioPlayerService.setVolume(volume);
		seekbar.setProgress((int) (volume * TEN));
	}

	// }

	public void initUI(int state) {
		switch (state) { // 0-IDLE 1-PAUSED 2-PLAYED
		case 0:
			Log.d(TAG, "Activity state Idle");
			mediaButton.setText("Play");
			mediaText.setText("Status:Idle");
			break;
		case 1:
			Log.d(TAG, "Activity state Played");
			mediaButton.setText("Pause");
			mediaText.setText("Status:Playing");
			break;
		case 2:
			Log.d(TAG, "Activity state Paused");
			mediaButton.setText("Play");
			mediaText.setText("Status:Paused");
			break;
		}
	}
}
