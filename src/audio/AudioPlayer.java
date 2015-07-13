package audio;

import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class AudioPlayer {
	public static void play(String path) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		
		Player playMP3 = new Player(fis);

		playMP3.play();
	}
}
