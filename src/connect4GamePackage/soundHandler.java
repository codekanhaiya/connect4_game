package connect4GamePackage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.Clip;
import javax.swing.*;

import java.io.IOException;
import java.net.URL;


class soundHandler {

	private static boolean isMusicPlay = false;
	private static Clip copyClip;

	static void checkMusic(URL music) {
		if (!isMusicPlay) {
			JOptionPane.showMessageDialog(null, "Hit OK to resume music");
			runMusic(music);
		} else {
			JOptionPane.showMessageDialog(null, "Hit OK to pause music.");
			runMusic(music);
		}

	}

	static void runMusic(URL music) {
		 if (!isMusicPlay) {
			try {
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(music);
				Clip clip = AudioSystem.getClip();
				clip.open(inputStream);
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				isMusicPlay = true;
				copyClip = clip;
				System.out.println("Successfully music play.");
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				System.out.println("Error in music playing.");
			}
		} else {
			copyClip.stop();
			isMusicPlay = false;
			System.out.println("Successfully music paused.");
		}

	}
}