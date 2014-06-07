package com.assigment2.audioplayer;

import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayerSingleton {
	
	MediaPlayer mediaPlayer;
	private float leftVolumeCanal, rightVolumeCanal;

	private AudioPlayerSingleton(){
		
	}
	
	private static class AudioPlayerSingletonHolder{
		private final static AudioPlayerSingleton audioPlayerInstance = new AudioPlayerSingleton();
	}
	
	public static AudioPlayerSingleton getAudioPlayerInstance(){
		return AudioPlayerSingletonHolder.audioPlayerInstance;
	}
	
	public void create(MediaPlayer player, Context context, int id){
		player = MediaPlayer.create(context, id);
	}
	
	public void play(){
		if (isPlaying()){
			return;
		}
		mediaPlayer.start();
	}
	
	public void pause(){
		mediaPlayer.pause();
	}
	
	public boolean isPlaying(){
		return mediaPlayer.isPlaying();
	}
	
	public void setVolume(float leftVolume, float rightVolume){
		mediaPlayer.setVolume(leftVolume, rightVolume);
		leftVolumeCanal = leftVolume;
		rightVolumeCanal = rightVolume;
	}
	
	public float getLeftVolumeCanal(){
		return leftVolumeCanal;
	}
	
	public float getRightVolumeCanal(){
		return rightVolumeCanal;
	}
	
	public void dispose(){
		if (isPlaying()){
			mediaPlayer.stop();
		}
		mediaPlayer.release();
		mediaPlayer = null;
	}
	
	public void seekAudio(int currentPosition){// не факт что нужна
		mediaPlayer.seekTo(currentPosition);
	}
}
