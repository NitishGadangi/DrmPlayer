package nitish.vidflix1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.vkay94.dtpv.DoubleTapPlayerView;
import com.github.vkay94.dtpv.YouTubeDoubleTap;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

public class StreamPlayer extends AppCompatActivity implements Player.EventListener {
    private static final String TAG = "ExoPlayerActivity";

    private static final String KEY_VIDEO_URI = "video_uri";


//    PlayerView videoFullScreenPlayer;

    YouTubeDoubleTap youTubeDoubleTap;
    DoubleTapPlayerView videoFullScreenPlayer;

    ProgressBar spinnerVideoDetails;
    ImageView imageViewExit;

    String videoUri;
    SimpleExoPlayer player;
    Handler mHandler;
    Runnable mRunnable;
    TextView tv_head;
    String cv_head;

    Boolean isStreched=false;

    Button strechBtn;

    ImageView screen_img;
    String main_screenUrl="FAILED";





    public static Intent getStartIntent(Context context, String videoUri,String head,String screenUrl) {
        Intent intent = new Intent(context, StreamPlayer.class);
        intent.putExtra(KEY_VIDEO_URI, videoUri);
        intent.putExtra("KEY_HEAD",head);
        intent.putExtra("SCREEN_URL",screenUrl);
        return intent;

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stream_player);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        videoFullScreenPlayer=findViewById(R.id.videoFullScreenPlayer);
        spinnerVideoDetails=findViewById(R.id.spinnerVideoDetails);

        videoFullScreenPlayer.setKeepScreenOn(true);
//        videoFullScreenPlayer.setControllerShowTimeoutMs(3000);

        screen_img=findViewById(R.id.screen_img);

        if (getIntent().hasExtra(KEY_VIDEO_URI)) {
            videoUri = getIntent().getStringExtra(KEY_VIDEO_URI);
            cv_head=getIntent().getStringExtra("KEY_HEAD");
            main_screenUrl=getIntent().getStringExtra("SCREEN_URL");
        }

        Glide.with(getApplicationContext()).load(main_screenUrl).centerCrop().placeholder(R.drawable.dummy_img).into(screen_img);

        tv_head=findViewById(R.id.tv_cv_head);
        tv_head.setText(cv_head);

        findViewById(R.id.btn_cv_bak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//
                onBackPressed();
            }
        });

        strechBtn=findViewById(R.id.btn_strech_screen);

        strechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //------Animation-----------//
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(500);
                view.startAnimation(animation1);
                //-------------------------//

                if (isStreched){
                    isStreched=false;
                    videoFullScreenPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                    strechBtn.setBackground(getDrawable(R.drawable.ic_strech));
                }else {
                    isStreched=true;
                    videoFullScreenPlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                    strechBtn.setBackground(getDrawable(R.drawable.ic_unstrech));
                }

            }
        });

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

        youTubeDoubleTap=findViewById(R.id.doubleTap);

        setUp();
    }

    private void setUp() {
        initializePlayer();
        if (videoUri == null) {
            return;
        }
        buildMediaSource(Uri.parse(videoUri));
    }



    private void initializePlayer() {
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
            player = ExoPlayerFactory.newSimpleInstance(getApplicationContext(),new DefaultRenderersFactory(this), trackSelector, loadControl);
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
//                Util.getUserAgent(this, getString(R.string.app_name)), bandwidthMeter);


        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(Util.getUserAgent(this,
                getString(R.string.app_name)),
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true);

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
        player.prepare(videoSource);
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
                screen_img.setVisibility(View.GONE);
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
        Log.i("STRM_TYPE","TYPE"+error);
        if (error.toString().contains("Unable to connect")){
            findViewById(R.id.tv_error).setVisibility(View.VISIBLE);
        }
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

