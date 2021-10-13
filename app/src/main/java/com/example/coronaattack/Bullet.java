package com.example.coronaattack;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bullet {

    int x,y, bulletHeight, bulletWidth;
    int bulletCounter = 1;
    Boolean isBullet;
    Bitmap bullet1, bullet2, bullet3,bullet4, emptyImg;
    GameView gameView;

    Bullet(Resources resource, GameView gameView , Boolean isBullet){

        this.gameView = gameView;
        bullet1 = BitmapFactory.decodeResource(resource, R.drawable.bullet1);
        bullet2 = BitmapFactory.decodeResource(resource, R.drawable.bullet2);
        bullet3 = BitmapFactory.decodeResource(resource, R.drawable.bullet3);
        bullet4 = BitmapFactory.decodeResource(resource, R.drawable.bullet4);
        emptyImg = BitmapFactory.decodeResource(resource, R.drawable.null_img);

        bulletWidth = bullet1.getWidth();
        bulletHeight = bullet1.getHeight();

        bulletHeight*=GameView.screenHeightRatio;
        bulletWidth*=GameView.screenWidthRatio;

        bulletHeight /=1.1;
        bulletWidth /=1.1;

        bullet1 = Bitmap.createScaledBitmap(bullet1,bulletWidth,bulletHeight,false);
        bullet2 = Bitmap.createScaledBitmap(bullet2,bulletWidth,bulletHeight,false);
        bullet3 = Bitmap.createScaledBitmap(bullet3,bulletWidth,bulletHeight,false);
        bullet4 = Bitmap.createScaledBitmap(bullet4,bulletWidth,bulletHeight,false);

        this.isBullet = isBullet;
    }

    public Bitmap getBullet() {
        if (isBullet) {
            if (bulletCounter == 1) {
                bulletCounter++;
                return bullet1;
            } else if (bulletCounter == 2) {
                bulletCounter++;
                return bullet2;
            }
            bulletCounter = 1;
            return bullet3;
        }
        return emptyImg;
    }
    Rect getCollisionShape(){
        return new Rect(x , y , (int) (x+bulletWidth-15*GameView.screenWidthRatio), (int) (y+bulletHeight-15*GameView.screenWidthRatio));
    }
}