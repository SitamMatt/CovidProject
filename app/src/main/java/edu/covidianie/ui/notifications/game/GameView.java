package edu.covidianie.ui.notifications.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends View implements View.OnTouchListener{

    private final List<Virus> virusList;
    private final Paint paint = new Paint();
    private long tmpTimeStamp;
    private long startTimeStamp;
    private boolean finished = false;
    Thread screenRefresher;
    private int points = 0;


    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        virusList = new ArrayList<>();
        virusList.add(new Virus());
        paint.setColor(virusList.get(0).color);
        screenRefresher = new Thread() {
            @Override
            public void run() {
                tmpTimeStamp = System.currentTimeMillis();
                startTimeStamp = tmpTimeStamp;
                while (true) {
                    try {
                        if(System.currentTimeMillis() - startTimeStamp > 15000)
                        {
                            break;
                        }
                        Thread.sleep(2);
                        invalidate();
                        if(System.currentTimeMillis() - tmpTimeStamp > 1000){
                            Random rand = new Random();
                            int r = rand.nextInt(255);
                            int g = rand.nextInt(255);
                            int b = rand.nextInt(255);
                           tmpTimeStamp = System.currentTimeMillis();
                           virusList.add(new Virus(Color.rgb(r,g,b)));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                virusList.clear();
                finished = true;
                invalidate();
            }
        };
        screenRefresher.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float touchX  = event.getX();
        float touchY  = event.getY();
        for(Virus vir : virusList){
            boolean touched = true;
            touched &= Math.abs(vir.position.x - touchX) < 15;
            touched &= Math.abs(vir.position.y - touchY) < 15;
            if(touched){
                virusList.remove(vir);
                points ++;
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(Virus v : virusList) {
            paint.setColor(v.color);
            canvas.drawCircle(v.position.x, v.position.y, v.size, paint);
        }
        if(finished) {
            paint.setColor(Color.parseColor("#FF6200EE"));
            paint.setTextSize(25);
            canvas.drawText("You have catched: " + String.valueOf(points) + " viruses", 50, 50, paint);
        }
    }
    class Virus{
        Point position = new Point();
        int color;
        float size = 15.0f;
        double moveDistanceX = 0.02;
        double moveDistanceY = 0.02;
        Thread init;

        public Virus() {
            color = Color.GREEN;
            position.x = 10;
            position.y = 30;
            init = new Thread(){
                @Override
                public void run() {
                    while (true) {


                    try {
                        sleep(4);
                        move();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    }
                }
            };
            init.start();
        }
        public Virus(int color) {
            this();
            this.color = color;
        }

        void move(){
            boolean bounced = false;
            if(position.x < 14) {
                moveDistanceX = Math.abs(moveDistanceX);;
                bounced = true;
            }
            if(position.y < 14) {
                moveDistanceY = Math.abs(moveDistanceY);
                bounced = true;
            }
            if(position.x >getWidth()-14) {
                moveDistanceX = -1 * Math.abs(moveDistanceX);
                bounced = true;
            }
            if(position.y >getHeight()-14) {
                moveDistanceY = -1 * Math.abs(moveDistanceY);
                bounced = true;
            }
            position.x += moveDistanceX;
            position.y += moveDistanceY;
            if(bounced) {
                moveDistanceX += randomDouble(0.01, 0.02);
                moveDistanceY += randomDouble(0.01, 0.02);
            }
        }
    }
    public double randomDouble(double rangeMin, double rangeMax){
        Random r = new Random();
        return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
    }
}


