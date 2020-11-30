package com.github.RonakPustack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.github.RonakPustack.util.Util;

import io.agora.education.api.EduCallback;
import io.agora.education.api.manager.EduManager;
import io.agora.education.api.manager.EduManagerOptions;
import io.agora.education.api.room.data.RoomType;
import io.agora.education.api.user.data.EduUserRole;

public class ClassroomHelper {

    private final int REQUEST_CODE_RTC = 101;
    private final static int REQUEST_CODE_RTE = 909;
    private static final String CODE = "code";
    public static final String REASON = "reason";
    private final int EDULOGINTAG = 999;

    private final String app_name =  "Agora Cloud Class";
    private final String hint_room_name =  "Room Name";
    private final String hint_room_type =  "Room Type";
    private final String room_name_should_not_be_empty =  "Room name should not be empty.";
    private final String room_type_should_not_be_empty =  "Room type should not be empty.";
    private final String one2one_class =  "One to One Classroom";
    private final String small_class =  "Small Classroom";
    private final String large_class =  "Lecture Hall";
    private final String breakout =  "Breakout Classroom";


    public Intent getIntent(Context context) {
        Intent intent = new Intent();

        try {
            intent = new Intent(context,
                    Class.forName("io.agora.education.MainActivity"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return intent;
    }

//    public void onJoinAttempt(){
//
//        if (Util.isFastClick()) {
//            return;
//        }
//        if (Util.checkAndRequestAppPermission(this, new String[]{
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.CAMERA,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//        }, REQUEST_CODE_RTC)) {
//            startJoinAttempt();
//        }
//
//    }
//
//    private int getClassType(String roomTypeStr) {
//        if (roomTypeStr.equals(one2one_class)) {
//            return RoomType.ONE_ON_ONE.getValue();
//        } else if (roomTypeStr.equals(small_class)) {
//            return RoomType.SMALL_CLASS.getValue();
//        } else if (roomTypeStr.equals(large_class)) {
//            return RoomType.LARGE_CLASS.getValue();
//        } else {
//            return RoomType.BREAKOUT_CLASS.getValue();
//        }
//    }
//
//    private void startJoinAttempt(String roomTypeStr) {
//        /**userUuid和roomUuid需用户自己指定，并保证唯一性*/
//        int roomType = getClassType(roomTypeStr);
//        String userUuid = yourNameStr + EduUserRole.STUDENT.getValue();
//        String roomUuid = roomNameStr + roomType;
//
//        EduManagerOptions options = new EduManagerOptions(this, getAppId(), userUuid, yourNameStr);
//        options.setCustomerId(getCustomerId());
//        options.setCustomerCertificate(getCustomerCer());
//        options.setLogFileDir(getCacheDir().getAbsolutePath());
//        options.setTag(EDULOGINTAG);
//        EduManager.init(options, new EduCallback<EduManager>() {
//            @Override
//            public void onSuccess(EduManager res) {
//                if (res != null) {
//                    setManager(res);
//                    createRoom(yourNameStr, userUuid, roomNameStr, roomUuid, roomType);
//                }
//            }
//
//            @Override
//            public void onFailure(int code, String reason) {
//                Log.e("Failed to join channel", "Error-> code:" + code + ",reason:" + reason);
//            }
//        });
//    }

}
