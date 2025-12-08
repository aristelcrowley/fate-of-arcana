package com.aristel.util;

import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, AudioClip> soundCache = new HashMap<>();

    private SoundManager() {
        loadSound("flip", "card_flip.mp3");
        loadSound("match", "match_success.mp3");
        loadSound("fail", "match_fail.mp3");
        loadSound("turn", "your_turn.mp3");
        loadSound("start", "game_start.mp3");
        loadSound("shuffled", "deck_shuffled.mp3");
        loadSound("win", "victory.mp3");
        loadSound("draw", "draw.mp3");
        loadSound("lose", "defeat.mp3");
        loadSound("click", "button_click.mp3");
        loadSound("join", "player_join.mp3");
        loadSound("left", "player_left.mp3");
        loadSound("error", "error.mp3");
        loadSound("disconnect", "player_disconnect.mp3");
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadSound(String key, String filename) {
        try {
            URL resource = getClass().getResource("/com/aristel/assets/sounds/" + filename);
            if (resource != null) {
                soundCache.put(key, new AudioClip(resource.toString()));
            } else {
                System.err.println("Sound missing: " + filename);
            }
        } catch (Exception e) {
            System.err.println("Error loading sound: " + filename);
        }
    }

    public void play(String key) {
        try {
            AudioClip clip = soundCache.get(key);
            if (clip != null) clip.play();
        } catch (Exception e) {}
    }
}