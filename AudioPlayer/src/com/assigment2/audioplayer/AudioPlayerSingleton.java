package com.assigment2.audioplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class AudioPlayerSingleton {

	MediaPlayer mediaPlayer = null;// = MediaPlayer.create(getContext(),
									// R.raw.tensec);//;
	private float leftVolumeCanal = 0.4f, rightVolumeCanal = 0.4f;
	Context context;
	boolean ended = true;

	private AudioPlayerSingleton() {

	}

	private static class AudioPlayerSingletonHolder {
		private final static AudioPlayerSingleton audioPlayerInstance = new AudioPlayerSingleton();
	}

	public static AudioPlayerSingleton getAudioPlayerInstance() {
		return AudioPlayerSingletonHolder.audioPlayerInstance;
	}

	public void setContext(Context context) {// !
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void create(Context context, int id) {
		if (mediaPlayer == null) {
			mediaPlayer = MediaPlayer.create(context, id);
			mediaPlayer.setScreenOnWhilePlaying(true);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
	}

	public void play() {
		if (isPlaying()) {
			return;
		}
		mediaPlayer.start();
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	public void setVolume(float leftVolume, float rightVolume) {
		mediaPlayer.setVolume(leftVolume, rightVolume);
		leftVolumeCanal = leftVolume;
		rightVolumeCanal = rightVolume;
	}

	public float getLeftVolumeCanal() {
		return leftVolumeCanal;
	}

	public float getRightVolumeCanal() {
		return rightVolumeCanal;
	}

	public void dispose() {
		if (isPlaying()) {
			mediaPlayer.stop();
		}
		mediaPlayer.release();
		mediaPlayer = null;
	}

	public boolean getEnded(){
		return ended;
	}
	
	public void setEnded(boolean end){
		ended = end;
	}
}
