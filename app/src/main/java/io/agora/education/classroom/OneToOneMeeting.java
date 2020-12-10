package io.agora.education.classroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.agora.education.R;
import io.agora.education.api.EduCallback;
import io.agora.education.api.message.EduChatMsg;
import io.agora.education.api.message.EduMsg;
import io.agora.education.api.room.EduRoom;
import io.agora.education.api.room.data.EduRoomChangeType;
import io.agora.education.api.statistics.ConnectionState;
import io.agora.education.api.statistics.NetworkQuality;
import io.agora.education.api.stream.data.EduStreamEvent;
import io.agora.education.api.stream.data.EduStreamInfo;
import io.agora.education.api.stream.data.EduStreamStateChangeType;
import io.agora.education.api.user.EduStudent;
import io.agora.education.api.user.data.EduUserEvent;
import io.agora.education.api.user.data.EduUserInfo;
import io.agora.education.api.user.data.EduUserStateChangeType;
import io.agora.education.classroom.bean.channel.Room;
import io.agora.education.classroom.widget.RtcVideoView;

public class OneToOneMeeting extends BaseClassActivity {

    private static final String TAG = OneToOneMeeting.class.getSimpleName();

    protected RtcVideoView video_teacher;
    protected RtcVideoView video_student;
    protected View layout_im;
    protected AppCompatImageView messageIndicator;
    protected AppCompatImageView iv_float;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_one_to_one_meeting;
    }

    @Override
    protected void initData() {
        super.initData();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        video_teacher = findViewById(R.id.layout_video_teacher);
        video_student = findViewById(R.id.layout_video_student);
        layout_im = findViewById(R.id.layout_im);
        messageIndicator = findViewById(R.id.message_indicator);
        iv_float = findViewById(R.id.iv_float);

        joinRoom(getMainEduRoom(), roomEntry.getUserName(), roomEntry.getUserUuid(), true, true, true,
                new EduCallback<EduStudent>() {
                    @Override
                    public void onSuccess(@org.jetbrains.annotations.Nullable EduStudent res) {
                        runOnUiThread(() -> showFragmentWithJoinSuccess());
                    }

                    @Override
                    public void onFailure(int code, @org.jetbrains.annotations.Nullable String reason) {
                        joinFailed(code, reason);
                    }
                });
    }

    @Override
    protected void initView() {
        super.initView();

        if (video_teacher != null) {
            video_teacher.init(R.layout.layout_video_one2one_class, true);
        } else {
            Log.d(TAG, "video_teacher is null");
        }

        if (video_student != null) {
            video_student.init(R.layout.layout_video_one2one_class, true);
            video_student.setOnClickAudioListener(v -> OneToOneMeeting.this.muteLocalAudio(!video_student.isAudioMuted()));
            video_student.setOnClickVideoListener(v -> OneToOneMeeting.this.muteLocalVideo(!video_student.isVideoMuted()));
        } else {
            Log.d(TAG, "video_student is null");
        }
    }

    @Override
    protected int getClassType() {
        return Room.Type.ONE2ONE;
    }

    public void onFloatClick(View view) {
        boolean isSelected = !view.isSelected();

        view.setSelected(isSelected);

        if(isSelected){
            layout_im.setVisibility(View.VISIBLE);
        }else{
            layout_im.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRemoteUsersInitialized(@NotNull List<? extends EduUserInfo> users, @NotNull EduRoom classRoom) {
        super.onRemoteUsersInitialized(users, classRoom);
        Log.d(TAG, "inside onRemoteUsersInitialized");

        video_student.setName(getLocalUserInfo().getUserName());
        title_view.setTitle(String.format(Locale.getDefault(), "%s", getLocalUserName()));
//        runOnUiThread(() -> {
//            /**一对一，默认学生可以针对白板进行输入*/
//            whiteboardFragment.disableCameraTransform(false);
//            whiteboardFragment.disableDeviceInputs(false);
//        });
    }

    @Override
    public void onRemoteUsersJoined(@NotNull List<? extends EduUserInfo> users, @NotNull EduRoom classRoom) {
        super.onRemoteUsersJoined(users, classRoom);
        title_view.setTitle(String.format(Locale.getDefault(), "%s", getLocalUserName()));
    }

    @Override
    public void onRemoteUserLeft(@NotNull EduUserEvent userEvent, @NotNull EduRoom classRoom) {
        super.onRemoteUserLeft(userEvent, classRoom);
        title_view.setTitle(String.format(Locale.getDefault(), "%s", getLocalUserName()));
    }

    @Override
    public void onRemoteUserUpdated(@NotNull EduUserEvent userEvent, @NotNull EduUserStateChangeType type,
                                    @NotNull EduRoom classRoom) {
        super.onRemoteUserUpdated(userEvent, type, classRoom);

    }

    /**
     * 群聊自定义消息回调
     */
    @Override
    public void onRoomMessageReceived(@NotNull EduMsg message, @NotNull EduRoom classRoom) {
        super.onRoomMessageReceived(message, classRoom);
    }

    /**
     * 私聊自定义消息回调
     */
    @Override
    public void onUserMessageReceived(@NotNull EduMsg message) {
        super.onUserMessageReceived(message);
    }

    /**
     * 群聊消息回调
     */
    @Override
    public void onRoomChatMessageReceived(@NotNull EduChatMsg eduChatMsg, @NotNull EduRoom classRoom) {
        super.onRoomChatMessageReceived(eduChatMsg, classRoom);

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                // Stuff that updates the UI
                if(!layout_im.isShown()){
                    iv_float.setSelected(true);
                    layout_im.setVisibility(View.VISIBLE);
                }

            }
        });
    }


    /**
     * 私聊消息回调
     */
    @Override
    public void onUserChatMessageReceived(@NotNull EduChatMsg chatMsg) {
        super.onUserChatMessageReceived(chatMsg);
    }

    @Override
    public void onRemoteStreamsInitialized(@NotNull List<? extends EduStreamInfo> streams,
                                           @NotNull EduRoom classRoom) {
        super.onRemoteStreamsInitialized(streams, classRoom);
        Log.e(TAG, "onRemoteStreamsInitialized");
//        EduStreamInfo streamInfo = getTeacherStream();
//        Log.e(TAG, "老师的流信息:" + new Gson().toJson(streamInfo));
//        if (streamInfo != null) {
//            video_teacher.setName(streamInfo.getPublisher().getUserName());
//            renderStream(getMainEduRoom(), streamInfo, video_teacher.getVideoLayout());
//            video_teacher.muteVideo(!streamInfo.getHasVideo());
//            video_teacher.muteAudio(!streamInfo.getHasAudio());
//        }
        for (EduStreamInfo streamInfo : streams) {
            /**In one scenario, the remote stream is the teacher’s stream*/
            switch (streamInfo.getVideoSourceType()) {
                case CAMERA:
                    video_teacher.setName(streamInfo.getPublisher().getUserName());
                    renderStream(getMainEduRoom(), streamInfo, video_teacher.getVideoLayout());
                    video_teacher.muteVideo(!streamInfo.getHasVideo());
                    video_teacher.muteAudio(!streamInfo.getHasAudio());
                    break;
                case SCREEN:
                    /**
                     If there is a screen sharing stream entered,
                     it means that the teacher turned on screen sharing,
                     and this stream is rendered at this time
                     **/
                    runOnUiThread(() -> {
                        layout_whiteboard.setVisibility(View.GONE);
                        layout_share_video.setVisibility(View.VISIBLE);
                        layout_share_video.removeAllViews();
                        renderStream(getMainEduRoom(), streamInfo, layout_share_video);
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRemoteStreamsAdded
            (@NotNull List<EduStreamEvent> streamEvents, @NotNull EduRoom classRoom) {
        super.onRemoteStreamsAdded(streamEvents, classRoom);
        for (EduStreamEvent streamEvent : streamEvents) {
            EduStreamInfo streamInfo = streamEvent.getModifiedStream();
            /**In one scenario, the remote stream is the teacher’s stream*/
            switch (streamInfo.getVideoSourceType()) {
                case CAMERA:
                    video_teacher.setName(streamInfo.getPublisher().getUserName());
                    renderStream(getMainEduRoom(), streamInfo, video_teacher.getVideoLayout());
                    video_teacher.muteVideo(!streamInfo.getHasVideo());
                    video_teacher.muteAudio(!streamInfo.getHasAudio());
                    break;
//                case SCREEN:
//                    /**有屏幕分享的流进入，说明是老师打开了屏幕分享，此时把这个流渲染出来*/
//                    runOnUiThread(() -> {
//                        layout_whiteboard.setVisibility(View.GONE);
//                        layout_share_video.setVisibility(View.VISIBLE);
//                        layout_share_video.removeAllViews();
//                        renderStream(getMainEduRoom(), streamInfo, layout_share_video);
//                    });
//                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRemoteStreamUpdated(@NotNull EduStreamEvent
                                              streamEvent, @NotNull EduStreamStateChangeType type,
                                      @NotNull EduRoom classRoom) {
        super.onRemoteStreamUpdated(streamEvent, type, classRoom);
        EduStreamInfo streamInfo = streamEvent.getModifiedStream();
        switch (streamInfo.getVideoSourceType()) {
            case CAMERA:
                video_teacher.setName(streamInfo.getPublisher().getUserName());
                renderStream(getMainEduRoom(), streamInfo, video_teacher.getVideoLayout());
                video_teacher.muteVideo(!streamInfo.getHasVideo());
                video_teacher.muteAudio(!streamInfo.getHasAudio());
                break;
            default:
                break;
        }
    }

    @Override
    public void onRemoteStreamsRemoved
            (@NotNull List<EduStreamEvent> streamEvents, @NotNull EduRoom classRoom) {
        super.onRemoteStreamsRemoved(streamEvents, classRoom);
        for (EduStreamEvent streamEvent : streamEvents) {
            EduStreamInfo streamInfo = streamEvent.getModifiedStream();
            switch (streamInfo.getVideoSourceType()) {
                case CAMERA:
                    video_teacher.setName(streamInfo.getPublisher().getUserName());
                    renderStream(getMainEduRoom(), streamInfo, null);
                    video_teacher.muteVideo(!streamInfo.getHasVideo());
                    video_teacher.muteAudio(!streamInfo.getHasAudio());
                    break;
//                case SCREEN:
//                    /**老师关闭了屏幕分享，移除屏幕分享的布局*/
//                    runOnUiThread(() -> {
//                        layout_whiteboard.setVisibility(View.VISIBLE);
//                        layout_share_video.setVisibility(View.GONE);
//                        layout_share_video.removeAllViews();
//                        renderStream(getMainEduRoom(), streamInfo, null);
//                    });
//                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRoomStatusChanged(@NotNull EduRoomChangeType event, @NotNull EduUserInfo
            operatorUser,
                                    @NotNull EduRoom classRoom) {
        super.onRoomStatusChanged(event, operatorUser, classRoom);
    }

    @Override
    public void onRoomPropertyChanged(@NotNull EduRoom
                                              classRoom, @Nullable Map<String, Object> cause) {
        super.onRoomPropertyChanged(classRoom, cause);
//        runOnUiThread(() -> {
//            /**小班课，默认学生可以针对白板进行输入*/
//            whiteboardFragment.disableCameraTransform(false);
//            whiteboardFragment.disableDeviceInputs(false);
//        });
    }

    @Override
    public void onRemoteUserPropertyUpdated(@NotNull EduUserInfo userInfo, @NotNull EduRoom
            classRoom,
                                            @Nullable Map<String, Object> cause) {
    }

    @Override
    public void onNetworkQualityChanged(@NotNull NetworkQuality quality, @NotNull EduUserInfo
            user,
                                        @NotNull EduRoom classRoom) {
        super.onNetworkQualityChanged(quality, user, classRoom);
        title_view.setNetworkQuality(quality);
    }

    @Override
    public void onConnectionStateChanged(@NotNull ConnectionState state, @NotNull EduRoom
            classRoom) {
        super.onConnectionStateChanged(state, classRoom);
    }

    @Override
    public void onLocalUserUpdated(@NotNull EduUserEvent
                                           userEvent, @NotNull EduUserStateChangeType type) {
        super.onLocalUserUpdated(userEvent, type);
    }

    @Override
    public void onLocalUserPropertyUpdated(@NotNull EduUserInfo
                                                   userInfo, @Nullable Map<String, Object> cause) {
        super.onLocalUserPropertyUpdated(userInfo, cause);
    }

    @Override
    public void onLocalStreamAdded(@NotNull EduStreamEvent streamEvent) {
        super.onLocalStreamAdded(streamEvent);
        EduStreamInfo streamInfo = streamEvent.getModifiedStream();
        renderStream(getMainEduRoom(), streamInfo, video_student.getVideoLayout());
        video_student.muteVideo(!streamInfo.getHasVideo());
        video_student.muteAudio(!streamInfo.getHasAudio());
        Log.e(TAG, "Local stream is added：" + getLocalCameraStream().getHasAudio() + "," + streamInfo.getHasVideo());
    }

    @Override
    public void onLocalStreamUpdated(@NotNull EduStreamEvent
                                             streamEvent, @NotNull EduStreamStateChangeType type) {
        super.onLocalStreamUpdated(streamEvent, type);
        EduStreamInfo streamInfo = streamEvent.getModifiedStream();
        video_student.muteVideo(!streamInfo.getHasVideo());
        video_student.muteAudio(!streamInfo.getHasAudio());
        Log.d(TAG, "\n" + "Local stream is modified：" + streamInfo.getHasAudio() + "," + streamInfo.getHasVideo());
    }

    @Override
    public void onLocalStreamRemoved(@NotNull EduStreamEvent streamEvent) {
        super.onLocalStreamRemoved(streamEvent);
        /**
         In a scenario, this callback is called
         to indicate that the classroom is over and
         the staff quit; so this callback can be left unprocessed*/
        EduStreamInfo streamInfo = streamEvent.getModifiedStream();
        renderStream(getMainEduRoom(), streamInfo, null);
        video_student.muteVideo(true);
        video_student.muteAudio(true);
        Log.d(TAG, "Local stream is removed");
    }
}