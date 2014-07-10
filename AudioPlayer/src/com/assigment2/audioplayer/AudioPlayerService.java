package com.assigment2.audioplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;

public class AudioPlayerService extends Service {

	private final IBinder mBinder = new LocalBinder();
	private MediaPlayer mediaPlayer = null;
	private int state = 0;

	enum VolumeState {
		IDLE(0), PAUSED(1), PLAYED(2);

		private int stateVolume;

		VolumeState(int s) {
			stateVolume = s;
		}

		int getStateVolume() {
			return stateVolume;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(this, R.raw.tensec);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer listener) {
					Intent i = new Intent(MediaPlayerActivity.BROADCAST_ACTION);
					sendBroadcast(i);
					setState(VolumeState.IDLE.getStateVolume());
				}
			});
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public class LocalBinder extends Binder {
		AudioPlayerService getService() {
			return AudioPlayerService.this;
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String startCommand = intent.getStringExtra("State");
		switch (startCommand) {
		case "ON":
			mediaPlayer.start();
			setState(VolumeState.PLAYED.getStateVolume());
			break;
		case "OFF":
			mediaPlayer.pause();
			setState(VolumeState.PAUSED.getStateVolume());
			break;
		}
		return Service.START_STICKY;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);
		VolumeClass.setVolume(volume);
	}

	public float getVolumeCanal() {
		return VolumeClass.getVolume();
	}

	private void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}
}
