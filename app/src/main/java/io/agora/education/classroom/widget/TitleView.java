package io.agora.education.classroom.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.agora.education.R;
import io.agora.education.api.statistics.NetworkQuality;
import io.agora.education.classroom.BaseClassActivity;
import io.agora.rtc.Constants;

import static io.agora.education.api.statistics.NetworkQuality.*;

public class TitleView extends ConstraintLayout {

    private String TAG = "TitleView";

//    @Nullable
//    @BindView(R.id.iv_quality)
    protected ImageView iv_quality;
//    @BindView(R.id.tv_room_name)
    protected TextView tv_room_name;
//    @BindView(R.id.time_view)
    protected TimeView time_view;

    protected AppCompatImageView iv_close;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        ButterKnife.bind(this);
    }

    private void init() {
        int layoutResId;

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutResId = R.layout.layout_title_portrait;
        } else {
            layoutResId = R.layout.layout_title_landscape;
        }
        View view = LayoutInflater.from(getContext()).inflate(layoutResId, this, true);

        iv_quality = view.findViewById(R.id.iv_quality);
        tv_room_name = view.findViewById(R.id.tv_room_name);
        time_view = view.findViewById(R.id.time_view);
        iv_close = view.findViewById(R.id.iv_close);

        iv_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Context context = getContext();
                Log.e(TAG, "Clicked the cross button");

                if (context instanceof BaseClassActivity) {
                    ((BaseClassActivity) context).showLeaveDialog();
                }

            }
        });
    }

    public void hideTime() {
        if (time_view != null) {
            time_view.setVisibility(GONE);
        }
    }

    public void setTitle(String title) {

        if(tv_room_name != null){
            ((Activity) getContext()).runOnUiThread(() -> tv_room_name.setText(title));
        }else{
            Log.d(TAG, "tv_room_name was null inside setTitle");
        }

    }

    public void setNetworkQuality(NetworkQuality quality) {
        ((Activity) getContext()).runOnUiThread(() -> {
            if (iv_quality != null) {
                switch (quality) {
                    case GOOD:
                        iv_quality.setImageResource(R.drawable.ic_signal_good);
                        break;
                    case BAD:
                        iv_quality.setImageResource(R.drawable.ic_signal_bad);
                        break;
                    case POOR:
                    case UNKNOWN:
                    default:
                        iv_quality.setImageResource(R.drawable.ic_signal_normal);
                        break;
                }
            }
        });
    }

    public void setTimeState(boolean start, long time) {
        ((Activity) getContext()).runOnUiThread(() -> {
            if (time_view != null) {
                Log.d(TAG, "Time view is not null");
                if (start) {
                    if (!time_view.isStarted()) {
                        time_view.start();
                    }
                    if(time_view != null){
                        Log.d(TAG, "Time view is not null and starting the clock");
                        time_view.setTime(time);
                    }

                } else {
                    Log.d(TAG, "Stopping the time view");
                    time_view.stop();
                }
                time_view.setTime(time);
            }else{
                Log.d(TAG, "Time view is null");
            }
        });
        Log.d(TAG, "Setting time");
    }

//    @OnClick({R.id.iv_close, R.id.iv_uploadLog})
//    public void onCrossTap(View view) {
//        Context context = getContext();
//        if (context instanceof BaseClassActivity) {
//            if(R.id.iv_close == view.getId()){
//                ((BaseClassActivity) context).showLeaveDialog();
//            }
////            switch (view.getId()) {
////                case R.id.iv_close:
////                    ((BaseClassActivity) context).showLeaveDialog();
////                    break;
////                case R.id.iv_uploadLog:
////                    ((BaseClassActivity) context).uploadLog();
////                    break;
////                default:
////                    break;
////            }
//        }
//    }

}
