package com.assigment2.audioplayer;

public class VolumeClass {

	private static float volumeCanal = 0.4f;

	private VolumeClass() {
	}

	public static float getVolume() {
		return volumeCanal;
	}

	public static void setVolume(float volume) {
		volumeCanal = volume;
	}
}
