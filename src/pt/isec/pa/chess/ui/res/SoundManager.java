package pt.isec.pa.chess.ui.res;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class SoundManager {
    private SoundManager() { }

    private static MediaPlayer mp;

    private static final String ROOT = "sounds/";

    public static final String EN = "en/";

    public static final String PT = "br/";

    public static boolean play(String filename,String language) {
        try {
            var url = SoundManager.class.getResource(ROOT + language + filename + ".mp3");
            if (url == null) return false;
            String path = url.toExternalForm();
            Media music = new Media(path);
            stop();
            mp = new MediaPlayer(music);
            mp.setStartTime(Duration.ZERO);
            mp.setStopTime(music.getDuration());
            mp.setAutoPlay(true);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isPlaying() {
        return mp != null && mp.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public static void stop() {
        if (mp != null && mp.getStatus() == MediaPlayer.Status.PLAYING)
            mp.stop();
    }

    public static void playSequence(String language, String... filenames) {
        if (filenames == null || filenames.length == 0) return;
        playNext(language, filenames, 0);
    }

    private static void playNext(String language, String[] filenames, int index) {
        if (index >= filenames.length) return;

        try {
            var url = SoundManager.class.getResource(ROOT + language + filenames[index] + ".mp3");
            if (url == null) return;

            String path = url.toExternalForm();
            Media music = new Media(path);
            stop();

            mp = new MediaPlayer(music);
            mp.setStartTime(Duration.ZERO);
            mp.setStopTime(music.getDuration());
            mp.setAutoPlay(true);

            mp.setOnEndOfMedia(() -> playNext(language, filenames, index + 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<String> getSoundList() {
        File soundsDir = new File(Objects.requireNonNull(SoundManager.class.getResource(ROOT)).getFile());
        return Arrays.stream(Objects.requireNonNull(soundsDir.listFiles())).map(File::getName).toList();
    }
}