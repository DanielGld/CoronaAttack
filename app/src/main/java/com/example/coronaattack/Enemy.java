package com.example.coronaattack;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Enemy {

    public int speed = 20;
    int y, x = 0;
    int enemyWidth, enemyHeight, enemyCounter = 1;
    Bitmap enemy1,enemy2,enemy3;

    Enemy(Resources resource){

        enemy1 = BitmapFactory.decodeResource(resource,R.drawable.enemy1);
        enemy2 = BitmapFactory.decodeResource(resource,R.drawable.enemy2);
        enemy3 = BitmapFactory.decodeResource(resource,R.drawable.enemy3);

        enemyWidth = enemy1.getWidth();
        enemyHeight = enemy1.getHeight();

        enemyHeight /=1.6;
        enemyWidth /=1.6;

        enemyHeight *=GameView.screenHeightRatio;
        enemyWidth /=GameView.screenWidthRatio;

        enemy1 = Bitmap.createScaledBitmap(enemy1, enemyWidth, enemyHeight,false);
        enemy2 = Bitmap.createScaledBitmap(enemy2, enemyWidth, enemyHeight,false);
        enemy3 = Bitmap.createScaledBitmap(enemy3, enemyWidth, enemyHeight,false);

        y = -enemyHeight;
    }

    Bitmap getEnemy(){
        if (enemyCounter == 1) {
            enemyCounter++;
            return enemy1;
        } else if (enemyCounter == 2) {
            enemyCounter++;
            return enemy2;
        }
        enemyCounter = 1;
        return enemy3;
    }

    Rect getCollisionShape(){
        return new Rect(x , y , (int) (x+enemyWidth-15*GameView.screenWidthRatio), (int) (y+enemyHeight-15*GameView.screenHeightRatio));
    }
}
