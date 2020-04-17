package com.nitish.vidtest1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.UnsupportedSchemeException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.github.vkay94.dtpv.DoubleTapPlayerView;
import com.github.vkay94.dtpv.YouTubeDoubleTap;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.ExoMediaDrm;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity implements Player.EventListener {
    private static final String TAG = "ExoPlayerActivity";

    private static final String KEY_VIDEO_URI = "video_uri";


    YouTubeDoubleTap youTubeDoubleTap;

    DoubleTapPlayerView videoFullScreenPlayer;
    ProgressBar spinnerVideoDetails;
    ImageView imageViewExit;

    String videoUri;
    SimpleExoPlayer player;
    Handler mHandler;
    Runnable mRunnable;
    String subtitleUri;

    String user_agent="";
    String drmUrl="";
    String referer="";
    Boolean isDrm=false;

    FrameLayout frame_exo ;
    LinearLayout small_holder_exo;

    DefaultHttpDataSourceFactory dataSourceFactory;



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }
    }



    public static Intent getStartIntent(Context context, String videoUri,String subsUri,String userAgent,String referer,String drmUrl) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_VIDEO_URI, videoUri);
        intent.putExtra("SUBS_URI",subsUri);
        intent.putExtra("U-A",userAgent);
        intent.putExtra("REFERER",referer);
        intent.putExtra("DRM",drmUrl);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        getSupportActionBar().hide();

        videoFullScreenPlayer=findViewById(R.id.videoFullScreenPlayer);
        spinnerVideoDetails=findViewById(R.id.spinnerVideoDetails);

        if (getIntent().hasExtra(KEY_VIDEO_URI)) {
            videoUri = getIntent().getStringExtra(KEY_VIDEO_URI);
            subtitleUri=getIntent().getStringExtra("SUBS_URI");
            user_agent=getIntent().getStringExtra("U-A");
            referer=getIntent().getStringExtra("REFERER");
            drmUrl=getIntent().getStringExtra("DRM");
        }

        if (!drmUrl.equals("NONE")){
            isDrm=true;
        }



        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(
                new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                    }
                }
        );
        youTubeDoubleTap=findViewById(R.id.youTubeDoubleTap);

        frame_exo = findViewById(R.id.frame_exo);
        small_holder_exo=findViewById(R.id.small_holder_exo);

        findViewById(R.id.btn_open_full).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog mFullScreenDialog = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen){
                    @Override
                    public void onBackPressed() {
                        super.onBackPressed();
                        Log.i("DIAG_TEST","Clicked");
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        if (frame_exo.getParent()!=null){
                            ((ViewGroup)frame_exo.getParent()).removeView(frame_exo);
                        }
                        small_holder_exo.addView(frame_exo,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    }
                };
                if (frame_exo.getParent()!=null){
                    ((ViewGroup)frame_exo.getParent()).removeView(frame_exo);
                }
                mFullScreenDialog.addContentView(frame_exo,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenDialog.show();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        });

        try {
            setUp();
        } catch (UnsupportedDrmException e) {
            e.printStackTrace();
        } catch (UnsupportedSchemeException e) {
            e.printStackTrace();
        }
    }

    void setUp() throws UnsupportedDrmException, UnsupportedSchemeException {
        initializePlayer();
        if (videoUri == null) {
            return;
        }
        buildMediaSource(Uri.parse(videoUri));
    }



    void initializePlayer() throws UnsupportedSchemeException, UnsupportedDrmException {
        if (player == null) {
            // 1. Create a default TrackSelector
            LoadControl loadControl = new DefaultLoadControl(
                    new DefaultAllocator(true, 16),
                    VideoPlayerConfig.MIN_BUFFER_DURATION,
                    VideoPlayerConfig.MAX_BUFFER_DURATION,
                    VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                    VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER, -1, true);

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);
            // 2. Create the player

            android.util.Log.i("STRM_TYPE","UA  "+user_agent);

            dataSourceFactory = new DefaultHttpDataSourceFactory(user_agent,
                    DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                    DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                    true);

            if (!referer.equals(""))
                dataSourceFactory.getDefaultRequestProperties().set("Referer",referer);




            DefaultRenderersFactory renderersFactory ;
            renderersFactory= new DefaultRenderersFactory(this);
            if(isDrm){
                HttpMediaDrmCallback drmCallback= new HttpMediaDrmCallback(drmUrl,dataSourceFactory);
                DefaultDrmSessionManager drmSessionManager = new DefaultDrmSessionManager(C.WIDEVINE_UUID,
                        FrameworkMediaDrm.newInstance(C.WIDEVINE_UUID), drmCallback, null);
                renderersFactory= new DefaultRenderersFactory(this,drmSessionManager);
            }

            player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(),renderersFactory, trackSelector, loadControl);
            videoFullScreenPlayer.setPlayer(player);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.CONTENT_TYPE_MOVIE)
                    .build();
            player.setAudioAttributes(audioAttributes, /* handleAudioFocus= */ true);
        }

        youTubeDoubleTap
                .setPlayer(videoFullScreenPlayer)
                .setForwardRewindIncrementMs(10000);
        videoFullScreenPlayer
                .activateDoubleTap(true)
                .setDoubleTapDelay(500)
                .setDoubleTapListener(youTubeDoubleTap);


    }

    private void buildMediaSource(Uri mUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
//                user_agent, bandwidthMeter);

//        android.util.Log.i("STRM_TYPE","UA  "+user_agent);
//
//        dataSourceFactory = new DefaultHttpDataSourceFactory(user_agent,
//                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
//                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
//                true);
//
//        if (!referer.equals(""))
//            dataSourceFactory.getDefaultRequestProperties().set("Referer",referer);
//
//
//        HttpMediaDrmCallback drmCallback;
//        if (isDrm)
//            drmCallback= new HttpMediaDrmCallback(drmUrl,dataSourceFactory);


        // This is the MediaSource representing the media to be played.
//        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
//        HlsMediaSource videoSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
//            MediaSource videoSource = new SsMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
//        DashMediaSource videoSource = new DashMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
//        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
        MediaSource videoSource;
        @C.ContentType int type = Util.inferContentType(mUri);
        Log.i("STRM_TYPE","TYPE"+type);
        if (type==C.TYPE_DASH)
            videoSource = new DashMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
        else if (type==C.TYPE_SS)
            videoSource = new SsMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
        else if (type==C.TYPE_HLS)
            videoSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
        else if (type==C.TYPE_OTHER)
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
        else {
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
            throw new IllegalStateException("Unsupported type: " + type);
        }

        // Prepare the player with the source.
        Format subtitleFormat = Format.createTextSampleFormat(
                null, // An identifier for the track. May be null.
                MimeTypes.TEXT_VTT, // The mime type. Must be set correctly.
                0, // Selection flags for the track.
                null); // The subtitle language. May be null.
        MediaSource subtitleSource =
                new SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(subtitleUri),subtitleFormat,C.TIME_UNSET);
// Plays the video with the sideloaded subtitle.
        MergingMediaSource mergedSource =
                new MergingMediaSource(videoSource, subtitleSource);

        player.prepare(videoSource);
//        player.prepare(mergedSource);
        player.setPlayWhenReady(true);
        player.addListener(this);
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void pausePlayer() {
        if (player != null) {
            player.setPlayWhenReady(false);
            player.getPlaybackState();
        }
    }

    private void resumePlayer() {
        if (player != null) {
            player.setPlayWhenReady(true);
            player.getPlaybackState();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        resumePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {

            case Player.STATE_BUFFERING:
                spinnerVideoDetails.setVisibility(View.VISIBLE);
                break;
            case Player.STATE_ENDED:
                // Activate the force enable
                break;
            case Player.STATE_IDLE:

                break;
            case Player.STATE_READY:
                spinnerVideoDetails.setVisibility(View.GONE);

                break;
            default:
                // status = PlaybackStatus.IDLE;
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.i("SPOTTED","error "+error);
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    public class VideoPlayerConfig {
        //Minimum Video you want to buffer while Playing
        public static final int MIN_BUFFER_DURATION = 5 * 60 * 1000;
        //Max Video you want to buffer during PlayBack
        public static final int MAX_BUFFER_DURATION = 10 * 60 * 1000;
        //Min Video you want to buffer before start Playing it
        public static final int MIN_PLAYBACK_START_BUFFER = 1500;
        //Min video You want to buffer when user resumes video
        public static final int MIN_PLAYBACK_RESUME_BUFFER = 5000;

//        public static final String DEFAULT_VIDEO_URL = "https://androidwave.com/media/androidwave-video-exo-player.mp4";
        public static final String DEFAULT_VIDEO_URL = "https://download.eu-west-3.fromsmash.co/transfer/012x81ko---c0";
    }
}
