package com.example.coronaattack;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class PlayerAvatar {

    boolean isGoingUp = false;
    int x,y, avatarWidth, avatarHeight;
    Bitmap avatar , gameOverAvatar;

    PlayerAvatar(int screenHeight, Resources resource){

        avatar = BitmapFactory.decodeResource(resource, R.drawable.player_avatar);
        gameOverAvatar = BitmapFactory.decodeResource(resource,R.drawable.broken_syringe);
        avatarWidth = avatar.getWidth();
        avatarHeight = avatar.getHeight();

        avatarHeight /=1.2;
        avatarWidth /=1.2;

        avatarWidth *=GameView.screenWidthRatio;
        avatarHeight *=GameView.screenHeightRatio;

        avatar = Bitmap.createScaledBitmap(avatar, avatarWidth, avatarHeight, false);
        y = screenHeight/2;
        x = (int)(16* GameView.screenWidthRatio);
    }
    Rect getCollisionShape(){
        return new Rect(x , y , (int) (x+ avatarWidth -100*GameView.screenWidthRatio), (int) (y+ avatarHeight -15*GameView.screenHeightRatio));
    }
}
