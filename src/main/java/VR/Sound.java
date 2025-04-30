package VR;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;

public class Sound {
    private static Clip musicClip;

    public static void playMusic(String path) {
        try {
            if (musicClip == null || !musicClip.isActive()) {
                InputStream audioSrc = Sound.class.getResourceAsStream(path);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
                musicClip = AudioSystem.getClip();
                musicClip.open(audioStream);
                musicClip.loop(Clip.LOOP_CONTINUOUSLY); // 🔁 en boucle
                musicClip.start();
            }
        } catch (Exception e) {
            System.err.println("Erreur musique : " + e.getMessage());
        }
    }

    public static void playEffect(String path) {
        try {
            InputStream audioSrc = Sound.class.getResourceAsStream(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            Clip effectClip = AudioSystem.getClip();
            effectClip.open(audioStream);
            effectClip.start();
        } catch (Exception e) {
            System.err.println("Erreur effet sonore : " + e.getMessage());
        }
    }

    public static void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }
}
