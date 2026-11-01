package com.limelight.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import com.limelight.TestLogSuppressor;
import com.limelight.nvstream.StreamConfiguration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@Config(sdk = {33}, shadows = {com.limelight.shadows.ShadowMoonBridge.class, com.limelight.shadows.ShadowGameManager.class})
@RunWith(RobolectricTestRunner.class)
public class AudioCodecPreferenceTest {
    // Values from moonlight-common-c Limelight.h (independent of the Java constants under test)
    private static final int AUDIO_CODEC_OPUS = 0;
    private static final int AUDIO_CODEC_PCM = 1;
    private static final int CAPABILITY_PCM_AUDIO = 0x80;

    private Context ctx;

    @BeforeClass
    public static void suppressInvalidIdLogs() {
        TestLogSuppressor.install();
    }

    @Before
    public void setup() {
        ctx = ApplicationProvider.getApplicationContext();
    }

    private SharedPreferences freshPrefs() {
        SharedPreferences prefs = ctx.getSharedPreferences("audio_codec_test", Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
        return prefs;
    }

    @Test
    public void audioCodecDefaultsToPcm() {
        PreferenceConfiguration cfg = PreferenceConfiguration.readPreferences(ctx, freshPrefs());
        assertEquals(AUDIO_CODEC_PCM, cfg.audioCodec);
    }

    @Test
    public void audioCodecOpusWhenChosen() {
        SharedPreferences prefs = freshPrefs();
        prefs.edit().putString("list_audio_codec", "opus").commit();
        PreferenceConfiguration cfg = PreferenceConfiguration.readPreferences(ctx, prefs);
        assertEquals(AUDIO_CODEC_OPUS, cfg.audioCodec);
    }

    @Test
    public void audioCodecPcmWhenChosen() {
        SharedPreferences prefs = freshPrefs();
        prefs.edit().putString("list_audio_codec", "pcm").commit();
        PreferenceConfiguration cfg = PreferenceConfiguration.readPreferences(ctx, prefs);
        assertEquals(AUDIO_CODEC_PCM, cfg.audioCodec);
    }

    @Test
    public void streamDeclaresPcmCapabilityOnlyForPcm() {
        StreamConfiguration pcm = new StreamConfiguration.Builder()
                .setAudioCodec(AUDIO_CODEC_PCM).build();
        StreamConfiguration opus = new StreamConfiguration.Builder()
                .setAudioCodec(AUDIO_CODEC_OPUS).build();

        assertEquals(CAPABILITY_PCM_AUDIO, pcm.getAudioCapabilities() & CAPABILITY_PCM_AUDIO);
        assertEquals(0, opus.getAudioCapabilities() & CAPABILITY_PCM_AUDIO);
    }
}
