import android.content.Context
import android.media.MediaPlayer
import kotlin.apply

class MusicaPrincipal(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    fun reproducirSnd(snd: Int) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, snd).apply {
                isLooping = true
                setVolume(0.2f, 0.2f)
                start()
            }
        }
    }

    fun detenerMusica() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }
}
