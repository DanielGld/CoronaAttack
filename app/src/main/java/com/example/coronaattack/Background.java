package com.example.coronaattack;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Calendar;

public class Background {

    int x = 0, y = 0;
    Bitmap backGroundImg;

    Background(int screenWidth , int screenHeight , Resources resource) {

        int timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (timeOfDay > 6 && timeOfDay < 18) {
            backGroundImg = BitmapFactory.decodeResource(resource, R.drawable.game_background_day);
            backGroundImg = Bitmap.createScaledBitmap(backGroundImg, screenWidth, screenHeight, false);
        }
        else{
            backGroundImg = BitmapFactory.decodeResource(resource, R.drawable.game_background_night);
            backGroundImg = Bitmap.createScaledBitmap(backGroundImg, screenWidth, screenHeight, false);
        }

    }
}
