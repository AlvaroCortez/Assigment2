package com.assigment2.audioplayer;

import android.app.Service;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AudioPlayerService extends Service {

	// private float volumeCanal = 0.4f;
	// private boolean ended = true;
	private final IBinder mBinder = new LocalBinder();
	private MediaPlayer mediaPlayer = null;
	private int state = 1;// 0-IDLE 1-PAUSED 2-PLAYED
	private static String TAG = "Assigment2";

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
		Log.d(TAG, "Service Begin created");
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(this, R.raw.tensec);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer listener) {
					setState(VolumeState.IDLE.getStateVolume());
					Log.d(TAG, "Service OnComplitionListener");
				}
			});
		}
		Log.d(TAG, "Service End created");
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "Service&Activity Bound");
		return mBinder;
	}

	public class LocalBinder extends Binder {
		AudioPlayerService getService() {
			return AudioPlayerService.this;
		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "Service&Activity UnBound");
		return super.onUnbind(intent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "Service Begin started");
		String startCommand = intent.getStringExtra("State");
		switch (startCommand) {
		case "ON":
			mediaPlayer.start();
			setState(VolumeState.PLAYED.getStateVolume());
			Log.d(TAG, "Service Player Started");
			break;
		case "OFF":
			mediaPlayer.pause();
			setState(VolumeState.PAUSED.getStateVolume());
			Log.d(TAG, "Service Player Paused");
			break;
		}
		/*
		 * if (mediaPlayer.isPlaying()) { mediaPlayer.pause();
		 * setState(VolumeState.PAUSED.getStateVolume()); Log.d(TAG,
		 * "Service Player Paused"); } else { mediaPlayer.start();
		 * setState(VolumeState.PLAYED.getStateVolume()); Log.d(TAG,
		 * "Service Player Started"); }
		 */
		return Service.START_STICKY;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
		Log.d(TAG, "Service destroyed");
	}

	// public MediaPlayer getMediaPlayer() {
	// Log.d(TAG, "Service getMediaPlayer");
	// return mediaPlayer;
	// }

	// public boolean isPlaying() {
	// return mediaPlayer.isPlaying();
	// }

	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);
		// volumeCanal = volume;
		VolumeClass.setVolume(volume);
	}

	public float getVolumeCanal() {
		// return volumeCanal;
		return VolumeClass.getVolume();
	}

	// public boolean getEnded() {
	// return ended;
	// }

	// public void setEnded(boolean end) {
	// ended = end;
	// }

	private void setState(int state) {
		this.state = state;
		Log.d(TAG, "Service setState");
	}

	public int getState() {
		Log.d(TAG, "Service getState");
		return state;
	}
}
