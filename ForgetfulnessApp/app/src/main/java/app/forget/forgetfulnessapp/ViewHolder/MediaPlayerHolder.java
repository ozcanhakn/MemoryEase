package app.forget.forgetfulnessapp.ViewHolder;

import android.media.MediaPlayer;

public class MediaPlayerHolder {
    private static final MediaPlayerHolder instance = new MediaPlayerHolder();
    private MediaPlayer mediaPlayer4;

    private MediaPlayerHolder() {
        // Özel kurucu metod
        mediaPlayer4 = new MediaPlayer();
        //mediaPlayer4 = MediaPlayer.create(AppContext.getContext(), R.raw.remembering);
    }

    public static MediaPlayerHolder getInstance() {
        return instance;
    }

    public MediaPlayer getMediaPlayer4() {
        return mediaPlayer4;
    }
}
