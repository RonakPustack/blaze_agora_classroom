package io.agora.education;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

public class NavigationHelper {

    public static Intent goToMainActivity(Context context, HashMap roomCredentials) {
        Intent intent = new Intent(context, MainActivity.class);

        String userName = (String) roomCredentials.get("user_name");
        String roomId = (String) roomCredentials.get("room_id");

        intent.putExtra("user_name", userName);
        intent.putExtra("room_id", roomId);

        return intent;
    }

    public static Intent goToSecondMainActivity(Context context, HashMap roomCredentials) {
        Intent intent = new Intent(context, MainActivity2.class);

        String userName = (String) roomCredentials.get("user_name");
        String roomId = (String) roomCredentials.get("room_id");

        intent.putExtra("user_name", userName);
        intent.putExtra("room_id", roomId);

        return intent;
    }



}
