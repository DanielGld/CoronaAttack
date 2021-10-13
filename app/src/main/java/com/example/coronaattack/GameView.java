package com.example.coronaattack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class  GameView extends SurfaceView implements Runnable{

    static float screenWidthRatio, screenHeightRatio;

    private int backgroundSpeed = 15;
    private int screenWidth,screenHeight;
    int playerScore = 0 , enemyMaxSpeed = 40;
    private Thread thread;
    private Paint paint;
    private Boolean isPlaying = true, isGameOver = false;
    private Background background1, background2;

    private PlayerAvatar playerAvatar;
    private List<Bullet> bulletList, bulletsToDestroy;

    private Bullet bullet;
    private  Enemy[] enemiesList;
    private int numOfEnemies = 4;

    Random random;
    private SharedPreferences preferences;
    private GameActivity activity;

    SoundPool soundPool;
    private int sound;

    public GameView(GameActivity activity, int screenWidth, int screenHeight) {
        super(activity);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME).build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes).build();
        }
        else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        sound = soundPool.load(activity,R.raw.shoot_sound,1);

        this.activity = activity;
        preferences = activity.getSharedPreferences("game",Context.MODE_PRIVATE);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        screenWidthRatio = 2088f/screenWidth;
        screenHeightRatio = 1080f/screenHeight;
        background1 = new Background(screenWidth,screenHeight, getResources());
        background2 = new Background(screenWidth,screenHeight, getResources());
        background2.x = screenWidth;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.RED);
        playerAvatar = new PlayerAvatar(screenHeight,getResources());
        bulletList = new ArrayList<>();
        bulletsToDestroy = new ArrayList<>();
        enemiesList = new Enemy[numOfEnemies];
        for(int i = 0 ; i < numOfEnemies; i++){
            Enemy enemy = new Enemy(getResources());
            enemiesList[i] = enemy;
        }
        random = new Random();
    }

    @Override
    public void run() {

        while (isPlaying){
            update();
            draw();
            sleep();
        }
    }

    private void draw() {

        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(background1.backGroundImg, background1.x, background1.y,paint);
            canvas.drawBitmap(background2.backGroundImg, background2.x, background2.y,paint);

            canvas.drawText(playerScore + "", screenWidth-200, 130,paint);
            for(Enemy enemy : enemiesList){
                canvas.drawBitmap(enemy.getEnemy(),enemy.x,enemy.y,paint);
            }
            canvas.drawBitmap(playerAvatar.avatar,playerAvatar.x,playerAvatar.y,paint);

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(playerAvatar.avatar, playerAvatar.x, playerAvatar.y, paint);
                saveHighestScore();
                onExiting();
                getHolder().unlockCanvasAndPost(canvas);
                return;
            }
            for(int i = 0 ; i<bulletList.size(); i++){
                canvas.drawBitmap(bulletList.get(i).getBullet(),bulletList.get(i).x, bulletList.get(i).y,paint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void update() {

        background1.x -= backgroundSpeed;
        background2.x -= backgroundSpeed;

        if(background1.x + background1.backGroundImg.getWidth() < 0 ){
            background1.x = screenWidth;
        }
        if(background2.x + background2.backGroundImg.getWidth() < 0 ){
            background2.x = screenWidth;
        }

        if(playerAvatar.isGoingUp){
            playerAvatar.y -= 35*screenHeightRatio;
        }
        else {
            playerAvatar.y += 35*screenHeightRatio;
        }

        if(playerAvatar.y >= screenHeight - playerAvatar.avatarHeight) {
            playerAvatar.y = screenHeight - playerAvatar.avatarHeight;
        }
        if(playerAvatar.y < 0 ) {
            playerAvatar.y = 0;
        }

        for(Bullet bullet : bulletList){
            if(bullet.x > screenWidth){
                bulletsToDestroy.add(bullet);
                bullet.isBullet = false;
            }

            bullet.x +=30*screenWidthRatio;
            for(Enemy enemy : enemiesList){
                if(Rect.intersects(enemy.getCollisionShape(), bullet.getCollisionShape())){
                    enemy.x -= screenWidth*2;
                    bullet.x += screenWidth*2;
                    playerScore++;
                    if(playerScore%10 == 0){
                        enemyMaxSpeed+=3;
                        backgroundSpeed+=2;
                    }
                }
            }
        }

        for(Bullet bullet : bulletsToDestroy){
            bulletList.remove(bullet);
        }

        for (Enemy enemy : enemiesList){
            enemy.x -= enemy.speed;
            if(enemy.x + enemy.enemyWidth < 0){
                int speed = (int) (enemyMaxSpeed*screenWidthRatio);
                enemy.speed = random.nextInt((int) (speed/screenWidthRatio));
                enemy.speed = enemy.speed < 10*screenWidthRatio ? 10 : enemy.speed;
                enemy.x = screenWidth;
                enemy.y = random.nextInt(screenHeight - enemy.enemyHeight);
            }
            if(Rect.intersects(playerAvatar.getCollisionShape(), enemy.getCollisionShape())){
                isGameOver = true;
                return;
            }
        }
    }

    private void sleep() {

        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){

        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            {
                if(event.getX()< screenWidth / 2){
                    playerAvatar.isGoingUp = true;
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                playerAvatar.isGoingUp = false;
                if(event.getX() > screenWidth/2){
                    newShoot();
                }
                break;
            }
        }
        return true;
    }
    public void newShoot(){

        if(!preferences.getBoolean("isMute",false)){
            soundPool.play(sound,1,1,0,0,1);
        }
        Bullet bullet = new Bullet(getResources(), this , true);
        bullet.x = playerAvatar.x + playerAvatar.avatarWidth - (int)(40*screenWidthRatio);
        bullet.y = playerAvatar.y + playerAvatar.avatarHeight /4;
        bullet.getBullet();
        bulletList.add(bullet);
    }

    private void saveHighestScore() {
        if(preferences.getInt("highestScore", 0 ) < playerScore){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highestScore", playerScore);
            editor.apply();
        }
    }

    private void onExiting(){
        Intent intent = new Intent(activity, GameOverActivity.class);
        intent.putExtra("score",playerScore);
        intent.putExtra("name" , activity.getIntent().getStringExtra("name"));

        activity.startActivity(intent);
        activity.finish();
    }
}
