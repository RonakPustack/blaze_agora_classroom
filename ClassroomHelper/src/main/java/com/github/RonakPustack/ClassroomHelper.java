package com.github.RonakPustack;

import android.content.Context;
import android.content.Intent;

public class ClassroomHelper {

    public static void printRonak(){
        System.out.println("JAVA");
    }

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

}
