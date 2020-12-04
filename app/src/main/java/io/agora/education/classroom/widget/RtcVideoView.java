package io.agora.education.classroom.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.agora.education.R;

public class RtcVideoView extends ConstraintLayout {
    final private String TAG = "RTCVideoView";

    //    @BindView(R.id.tv_name)
    protected TextView tv_name;
    //    @BindView(R.id.ic_audio)
    protected RtcAudioView ic_audio;
    //    @BindView(R.id.ic_video)
    protected ImageView ic_video;
    //    @BindView(R.id.layout_place_holder)
    protected FrameLayout layout_place_holder;
    //    @BindView(R.id.layout_video)
    protected FrameLayout layout_video;

    public RtcVideoView(Context context) {
        super(context);
    }

    public RtcVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RtcVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(@LayoutRes int layoutResId, boolean showVideo) {
        tv_name = findViewById(R.id.tv_name);
        ic_audio = findViewById(R.id.ic_audio);
        ic_video = findViewById(R.id.ic_video);
        layout_place_holder = findViewById(R.id.layout_place_holder);
        layout_video = findViewById(R.id.layout_video);

        inflate(getContext(), layoutResId, this);
//        ButterKnife.bind(this);
        if (ic_video != null) {
            ic_video.setVisibility(showVideo ? VISIBLE : GONE);
        }
    }

    public void setViewVisibility(int visibility) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setVisibility(visibility);
            }
        });
    }

    public void setName(String name) {
        if (tv_name != null) {
            ((Activity) getContext()).runOnUiThread(() -> tv_name.setText(name));
        } else {
            Log.d(TAG, "TV Name is null");
        }
    }

    public void muteAudio(boolean muted) {
        ((Activity) getContext()).runOnUiThread(() -> {
            if (ic_audio != null) {
                ic_audio.setState(muted ? RtcAudioView.State.CLOSED : RtcAudioView.State.OPENED);
            } else {
                Log.d(TAG, "muteAudio : Ic Audio is null");
            }

        });

    }

    public boolean isAudioMuted() {
        if (ic_audio != null) {
            return ic_audio.getState() == RtcAudioView.State.CLOSED;
        } else {
            Log.d(TAG, "isAudioMuted : Ic Audio is null");
            return false;
        }
    }

    public void muteVideo(boolean muted) {
        ((Activity) getContext()).runOnUiThread(() -> {
            if (ic_video != null) {
                ic_video.setSelected(!muted);
            } else {
                Log.d(TAG, "ic_video is null");
            }
            if (layout_video != null) {
                layout_video.setVisibility(muted ? GONE : VISIBLE);
            } else {
                Log.d(TAG, "layout_video is null");
            }
            if (layout_place_holder != null) {
                layout_place_holder.setVisibility(muted ? VISIBLE : GONE);
            } else {
                Log.d(TAG, "layout_place_holder is null");
            }


            Log.e("RtcVideoView", "muteVideo：" + muted);
        });
    }

    public boolean isVideoMuted() {
        if (ic_video != null) {
            return !ic_video.isSelected();
        }
        return true;
    }

    public FrameLayout getVideoLayout() {
        return layout_video;
    }

    public TextView getTv_name() {
        return tv_name;
    }

    //    public SurfaceView getSurfaceView() {
//        if (layout_video.getChildCount() > 0) {
//            return (SurfaceView) layout_video.getChildAt(0);
//        }
//        return null;
//    }
//
//    public void setSurfaceView(SurfaceView surfaceView) {
//        layout_video.removeAllViews();
//        if (surfaceView != null) {
//            layout_video.addView(surfaceView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        }
//    }

    public void setOnClickAudioListener(OnClickListener listener) {
//        ic_audio.setOnClickListener(listener);
    }

    public void setOnClickVideoListener(OnClickListener listener) {
        if (ic_video != null) {
            ic_video.setOnClickListener(listener);
        }
    }

//    public void showLocal() {
//        VideoMediator.setupLocalVideo(this);
//    }
//
//    public void showRemote(int uid) {
//        VideoMediator.setupRemoteVideo(this, uid);
//    }

}
