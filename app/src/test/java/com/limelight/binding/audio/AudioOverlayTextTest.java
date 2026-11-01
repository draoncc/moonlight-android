package com.limelight.binding.audio;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.limelight.TestLogSuppressor;
import com.limelight.nvstream.jni.MoonBridge;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@Config(sdk = {33}, shadows = {com.limelight.shadows.ShadowMoonBridge.class})
@RunWith(RobolectricTestRunner.class)
public class AudioOverlayTextTest {
    // Values from moonlight-common-c Limelight.h (independent of the Java constants)
    private static final int AUDIO_CODEC_OPUS = 0;
    private static final int AUDIO_CODEC_PCM = 1;

    private static final MoonBridge.AudioConfiguration STEREO = new MoonBridge.AudioConfiguration(2, 0x3);
    private static final MoonBridge.AudioConfiguration SURROUND_51 = new MoonBridge.AudioConfiguration(6, 0x3F);
    private static final MoonBridge.AudioConfiguration SURROUND_71 = new MoonBridge.AudioConfiguration(8, 0x63F);

    private Context ctx;

    @BeforeClass
    public static void suppressInvalidIdLogs() {
        TestLogSuppressor.install();
    }

    @Before
    public void setup() {
        ctx = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void pcm71() {
        assertEquals("Audio: PCM 7.1", AudioOverlayText.format(ctx, AUDIO_CODEC_PCM, SURROUND_71));
    }

    @Test
    public void pcm51() {
        assertEquals("Audio: PCM 5.1", AudioOverlayText.format(ctx, AUDIO_CODEC_PCM, SURROUND_51));
    }

    @Test
    public void pcmStereo() {
        assertEquals("Audio: PCM Stereo", AudioOverlayText.format(ctx, AUDIO_CODEC_PCM, STEREO));
    }

    @Test
    public void opus51() {
        assertEquals("Audio: Opus 5.1", AudioOverlayText.format(ctx, AUDIO_CODEC_OPUS, SURROUND_51));
    }

    @Test
    public void opusStereo() {
        assertEquals("Audio: Opus Stereo", AudioOverlayText.format(ctx, AUDIO_CODEC_OPUS, STEREO));
    }

    @Test
    public void noLineBeforeAudioIsNegotiated() {
        assertNull(AudioOverlayText.format(ctx, AUDIO_CODEC_OPUS, null));
    }
}
