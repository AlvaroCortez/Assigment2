package com.assigment2.audioplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
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
	private boolean isBound;
	public final static String BROADCAST_ACTION = "action";
	private BroadcastReceiver br = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			mediaButton.setText("Play");
			mediaText.setText("Status:Idle");
		}
	};
	private ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder binder) {
			audioPlayerService = ((AudioPlayerService.LocalBinder) binder)
					.getService();
			isBound = true;
			if (audioPlayerService != null) {
				doInitVolume();
				if(audioPlayerService.getState()==0){
					mediaButton.setText("Play");
					mediaText.setText("Status:Idle");
				} else if (audioPlayerService.getState()==1){
					initUI(2);
				} else{
					initUI(1);
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
		IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
		registerReceiver(br, intFilt);
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
			if (isBound) {
				switch (audioPlayerService.getState()) {
				case 0:
				case 1:
					startService(intent.putExtra("State", "ON"));
					initUI(audioPlayerService.getState());
					break;
				case 2:
					startService(intent.putExtra("State", "OFF"));
					initUI(audioPlayerService.getState());
					break;
				}
			}
			break;
		case R.id.imageButton_volume_plus:
			setVolume(FULL_VOLUME);
			break;
		case R.id.imageButton_volume_minus:
			setVolume(ZERO);
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

	public void doInitVolume() {
		volume = audioPlayerService.getVolumeCanal();
		audioPlayerService.setVolume(volume);
		seekbar.setProgress((int) (volume * TEN));
	}

	private void setVolume(int state) {
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

	public void initUI(int state) {
		switch (state) { 
		case 0:
		case 1:
			mediaButton.setText("Pause");
			mediaText.setText("Status:Playing");
			break;
		case 2:
			mediaButton.setText("Play");
			mediaText.setText("Status:Paused");
			break;
		}
	}
	
	 @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    unregisterReceiver(br);
	  }
}
