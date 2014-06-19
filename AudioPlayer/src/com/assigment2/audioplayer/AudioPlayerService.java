package com.assigment2.audioplayer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AudioPlayerService extends Service {

	private float volumeCanal = 0.4f;
	private boolean ended = true;
	private final IBinder mBinder = new LocalBinder();
	private MediaPlayer mediaPlayer = null;

	@Override
	public void onCreate() {
		super.onCreate();
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(this, R.raw.tensec);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
		} else {
			mediaPlayer.start();
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
	
	public MediaPlayer getMediaPlayer(){
		return mediaPlayer;
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}
	
	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);
		volumeCanal = volume;
	}

	public float getVolumeCanal() {
		return volumeCanal;
	}
	
	public boolean getEnded(){
		return ended;
	}
	
	public void setEnded(boolean end){
		ended = end;
	}
}
