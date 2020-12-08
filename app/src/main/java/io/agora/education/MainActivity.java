package io.agora.education;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import io.agora.base.ToastManager;
import io.agora.base.callback.ThrowableCallback;
import io.agora.base.network.BusinessException;
import io.agora.base.network.RetrofitManager;
import io.agora.education.api.EduCallback;
import io.agora.education.api.manager.EduManager;
import io.agora.education.api.manager.EduManagerOptions;
import io.agora.education.api.room.data.RoomCreateOptions;
import io.agora.education.api.room.data.RoomType;
import io.agora.education.api.statistics.AgoraError;
import io.agora.education.api.user.data.EduUserRole;
import io.agora.education.base.BaseActivity;
import io.agora.education.classroom.BaseClassActivity;
import io.agora.education.classroom.BreakoutClassActivity;
import io.agora.education.classroom.LargeClassActivity;
import io.agora.education.classroom.OneToOneClassActivity;
import io.agora.education.classroom.SmallClassActivity;
import io.agora.education.classroom.bean.channel.Room;
import io.agora.education.service.CommonService;
import io.agora.education.service.bean.ResponseBody;
import io.agora.education.service.bean.request.RoomCreateOptionsReq;
import io.agora.education.util.AppUtil;

import static io.agora.education.EduApplication.getAppId;
import static io.agora.education.EduApplication.getCustomerCer;
import static io.agora.education.EduApplication.getCustomerId;
import static io.agora.education.EduApplication.setManager;

import static io.agora.education.api.BuildConfig.API_BASE_URL;
import static io.agora.education.classroom.BaseClassActivity.RESULT_CODE;

public class MainActivity extends EduApplication {
    private static final String TAG = "MainActivity";

    private final int REQUEST_CODE_RTC = 101;
    public final static int REQUEST_CODE_RTE = 909;
    public static final String CODE = "code";
    public static final String REASON = "reason";
    private final int EDULOGINTAG = 999;

    private String USER_NAME;
    private String ROOM_NAME;

    // Got the basic one to one activity to work
//    @BindView(R.id.et_room_name)
//    protected EditText et_room_name;
//    @BindView(R.id.et_your_name)
//    protected EditText et_your_name;
//    @BindView(R.id.et_room_type)
//    protected EditText et_room_type;
//    @BindView(R.id.card_room_type)
//    protected CardView card_room_type;

    protected ProgressBar progressBar;
    protected TextView textView;

    private String url;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        Log.d(TAG, "Init data method");

        Intent intent = getIntent();

        String userName = intent.getStringExtra("user_name");
        String roomId = intent.getStringExtra("room_id");

        USER_NAME = userName;
        ROOM_NAME = roomId;

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.i(TAG, "This'll run 100 milliseconds later");
                        checkPermissionsAndJoinRoom();
                    }
                },
                100);
    }

    void checkPermissionsAndJoinRoom(){
        if (AppUtil.checkAndRequestAppPermission(this, new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE_RTC)) {
            start();
        }
    }

    @Override
    protected void initView() {
//        new PolicyDialog().show(getSupportFragmentManager(), null);
//        if (BuildConfig.DEBUG) {
//            et_room_name.setText("123");
//            et_room_name.setSelection(et_room_name.length());
//            et_your_name.setText("123");
//        }

        progressBar = findViewById(R.id.progressBarMainActivity);
        textView = findViewById(R.id.pleaseWaitTV);

        textView.setText("Please Wait...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Finish the current activity when navigating to a new one
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                ToastManager.showShort(R.string.no_enough_permissions);
                finish();
                return;
            }
        }
        switch (requestCode) {
            case REQUEST_CODE_RTC:
                start();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == REQUEST_CODE_RTE && resultCode == RESULT_CODE) {
            int code = data.getIntExtra(CODE, -1);
            String reason = data.getStringExtra(REASON);
            ToastManager.showShort(String.format(getString(R.string.function_error), code, reason));
        }
    }
//
//    @OnTouch(R.id.et_room_type)
//    public void onTouch(View view, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (card_room_type.getVisibility() == View.GONE) {
//                card_room_type.setVisibility(View.VISIBLE);
//            } else {
//                card_room_type.setVisibility(View.GONE);
//            }
//        }
//    }

    private void joinChannelAttempt(String roomName, String userName, String classRoomType) {
        /**userUuid和roomUuid需用户自己指定，并保证唯一性*/
        int roomType = getClassType(classRoomType);
        String userUuid = userName + EduUserRole.STUDENT.getValue();
        String roomUuid = roomName + roomType;

        Log.d(TAG, "Join channel method attempt");
        System.out.println(getAppId());

        EduManagerOptions options = new EduManagerOptions(this, getAppId(), userUuid, userName);
        options.setCustomerId(getCustomerId());
        options.setCustomerCertificate(getCustomerCer());
        options.setLogFileDir(getCacheDir().getAbsolutePath());
        options.setTag(EDULOGINTAG);
        EduManager.init(options, new EduCallback<EduManager>() {
            @Override
            public void onSuccess(@Nullable EduManager res) {
                if (res != null) {
                    Log.e(TAG, "初始化EduManager成功");
                    setManager(res);
                    createRoom(userName, userUuid, roomName, roomUuid, roomType);
                }
            }

            @Override
            public void onFailure(int code, @Nullable String reason) {
                Log.e(TAG, "初始化EduManager失败-> code:" + code + ",reason:" + reason);
            }
        });
    }

    private void start() {
        String CLASS_ROOM_TYPE = "One to One Classroom";
        int roomType = getClassType(CLASS_ROOM_TYPE);

        String userUuid = USER_NAME + EduUserRole.STUDENT.getValue();
        String roomUuid = ROOM_NAME + roomType;

        EduManagerOptions options = new EduManagerOptions(this, getAppId(), userUuid, USER_NAME);
        options.setCustomerId(getCustomerId());
        options.setCustomerCertificate(getCustomerCer());
        options.setLogFileDir(getCacheDir().getAbsolutePath());
        options.setTag(EDULOGINTAG);
        EduManager.init(options, new EduCallback<EduManager>() {
            @Override
            public void onSuccess(@Nullable EduManager res) {
                if (res != null) {
                    Log.e(TAG, "初始化EduManager成功");
                    setManager(res);
                    createRoom(USER_NAME, userUuid, USER_NAME, roomUuid, roomType);
                }
            }

            @Override
            public void onFailure(int code, @Nullable String reason) {
                Log.e(TAG, "初始化EduManager失败-> code:" + code + ",reason:" + reason);
            }
        });
    }

    @Room.Type
    private int getClassType(String roomTypeStr) {
        if (roomTypeStr.equals(getString(R.string.one2one_class))) {
            return RoomType.ONE_ON_ONE.getValue();
        } else if (roomTypeStr.equals(getString(R.string.small_class))) {
            return RoomType.SMALL_CLASS.getValue();
        } else if (roomTypeStr.equals(getString(R.string.large_class))) {
            return RoomType.LARGE_CLASS.getValue();
        } else {
            return RoomType.BREAKOUT_CLASS.getValue();
        }
    }

    private void createRoom(String yourNameStr, String yourUuid, String roomNameStr, String roomUuid, int roomType) {
        /**createClassroom时，room不存在则新建，存在则返回room信息(此接口非必须调用)，
         * 只要保证在调用joinClassroom之前，classroom在服务端存在即可*/
        RoomCreateOptions options = new RoomCreateOptions(roomUuid, roomNameStr, roomType);
        Log.e(TAG, "调用scheduleClass函数");
        RetrofitManager.instance().getService(API_BASE_URL, CommonService.class)
                .createClassroom(getAppId(), options.getRoomUuid(),
                        RoomCreateOptionsReq.convertRoomCreateOptions(options))
                .enqueue(new RetrofitManager.Callback<>(0, new ThrowableCallback<ResponseBody<String>>() {
                    @Override
                    public void onSuccess(@Nullable ResponseBody<String> res) {
                        Log.e(TAG, "调用scheduleClass函数成功");
                        Intent intent = createIntent(yourNameStr, yourUuid, roomNameStr, roomUuid, roomType);
                        startActivityForResult(intent, REQUEST_CODE_RTE);
//                        finish();
                    }

                    @Override
                    public void onFailure(@Nullable Throwable throwable) {
                        BusinessException error;
                        if (throwable instanceof BusinessException) {
                            error = (BusinessException) throwable;
                        } else {
                            error = new BusinessException(throwable.getMessage());
                        }
                        Log.e(TAG, "调用scheduleClass函数失败->" + error.getCode() + ", reason:" +
                                error.getMessage());
                        if (error.getCode() == AgoraError.ROOM_ALREADY_EXISTS.getValue()) {
                            Intent intent = createIntent(yourNameStr, yourUuid, roomNameStr, roomUuid, roomType);
                            startActivityForResult(intent, REQUEST_CODE_RTE);
//                            finish();
                        } else {
                            Log.e(TAG, "排课失败");
                        }
                    }
                }));
    }

    private Intent createIntent(String yourNameStr, String yourUuid, String roomNameStr,
                                String roomUuid, @Room.Type int roomType) {
        RoomEntry roomEntry = new RoomEntry(yourNameStr, yourUuid, roomNameStr, roomUuid, roomType);

        Intent intent = new Intent();
        if (roomType == RoomType.ONE_ON_ONE.getValue()) {
            intent.setClass(this, OneToOneClassActivity.class);
        } else if (roomType == RoomType.SMALL_CLASS.getValue()) {
            intent.setClass(this, SmallClassActivity.class);
        } else if (roomType == RoomType.LARGE_CLASS.getValue()) {
            intent.setClass(this, LargeClassActivity.class);
        } else {
            intent.setClass(this, BreakoutClassActivity.class);
        }
        intent.putExtra(BaseClassActivity.ROOMENTRY, roomEntry);
        return intent;
    }

}
