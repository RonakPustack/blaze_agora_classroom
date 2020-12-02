package io.agora.education;

import android.content.Context;
import android.content.Intent;

public class NavigationHelper {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

}
