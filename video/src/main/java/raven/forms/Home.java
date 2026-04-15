package raven.forms;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Home extends JPanel {

    private List<ModelLocation> locations;
    private int index = 0;
    private HomeOverlay homeOverlay;

    private MediaPlayerFactory factory;
    private EmbeddedMediaPlayer mediaPlayer;

    public Home() {
        init();
        loadZooData();
    }

    private void init() {
        factory = new MediaPlayerFactory();
        mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
        Canvas canvas = new Canvas();
        mediaPlayer.videoSurface().set(factory.videoSurfaces().newVideoSurface(canvas));
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                if (newTime >= mediaPlayer.status().length() - 1000) {
                    mediaPlayer.controls().setPosition(0);
                }
            }
        });
        setLayout(new BorderLayout());
        add(canvas);
    }

    private void loadZooData() {
        locations = new ArrayList<>();

        locations.add(new ModelLocation(
                "welcome!",
                "Wildlife is the living beauty of nature, full of diverse animals and ecosystems. It is precious because it keeps the Earth balanced and supports all life, including ours. Protecting it means protecting our future.",
                "video/home.mp4"));

        locations.add(new ModelLocation(
                "Asian Elephant",
                "The African elephant is a majestic symbol of the wild, known for its intelligence, strong family bonds, and gentle nature. It is precious because it helps shape ecosystems by spreading seeds and creating habitats, making it vital for the balance of nature.",
                "video/elephant.mp4"));

        locations.add(new ModelLocation(
                "Bengal Tiger",
                "The Bengal tiger (Panthera tigris tigris) is the most numerous tiger subspecies and " +
                        "a national symbol of both India and Bangladesh. As solitary, territorial hunters, " +
                        "tigers rely on stealth and power to ambush prey such as deer and wild boar. " +
                        "Their distinctive orange coat with dark stripes provides excellent camouflage in " +
                        "the dense forests and mangroves they call home.",
                "video/tiger.mp4"));
    }

    public void initOverlay(JFrame frame) {
        homeOverlay = new HomeOverlay(frame, locations);
        homeOverlay.getOverlay().setEventHomeOverlay(index1 -> {
            play(index1);
        });
        mediaPlayer.overlay().set(homeOverlay);
        mediaPlayer.overlay().enable(true);
    }

    public void play(int index) {
        this.index = index;
        ModelLocation location = locations.get(index);
        if (mediaPlayer.status().isPlaying()) {
            mediaPlayer.controls().stop();
        }
        mediaPlayer.media().play(location.getVideoPath());
        mediaPlayer.controls().play();
        homeOverlay.getOverlay().setIndex(index);
    }

    public void stop() {
        mediaPlayer.controls().stop();
        mediaPlayer.release();
        factory.release();
    }
}