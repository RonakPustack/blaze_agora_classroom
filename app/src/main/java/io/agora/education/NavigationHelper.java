package io.agora.education;

import android.content.Context;
import android.content.Intent;

public class NavigationHelper {

    public static Intent goToMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    public static Intent goToBlankActivity(Context context) {
        Intent intent = new Intent(context, BlankActivity.class);
        return intent;
    }



}
