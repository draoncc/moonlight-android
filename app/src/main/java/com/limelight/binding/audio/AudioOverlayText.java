package com.limelight.binding.audio;

import android.content.Context;

import com.limelight.R;
import com.limelight.nvstream.jni.MoonBridge;

public final class AudioOverlayText {
    private AudioOverlayText() {}

    // Formats the performance overlay line for the negotiated audio codec and channel layout,
    // e.g. "Audio: PCM 7.1". Returns null if no audio has been negotiated yet.
    public static String format(Context context, int audioCodec, MoonBridge.AudioConfiguration config) {
        if (config == null) {
            return null;
        }

        String codec = audioCodec == MoonBridge.AUDIO_CODEC_PCM ? "PCM" : "Opus";
        String layout;
        switch (config.channelCount) {
            case 2:
                layout = context.getString(R.string.perf_overlay_audio_stereo);
                break;
            case 6:
                layout = "5.1";
                break;
            case 8:
                layout = "7.1";
                break;
            default:
                layout = context.getString(R.string.perf_overlay_audio_channels, config.channelCount);
                break;
        }

        return context.getString(R.string.perf_overlay_audio, codec, layout);
    }
}
