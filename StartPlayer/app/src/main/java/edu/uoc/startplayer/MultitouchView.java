package edu.uoc.startplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class MultitouchView extends View implements View.OnTouchListener {

    private Paint paint;
    private Paint paintText;
    private static final int SIZE = 200;
    CountDownTimer cTimer = null;
    private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
            Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW };
    private int colorIndex = 0;

    ArrayList<Vector2D> map = new ArrayList<Vector2D>();
    private int playerSelected = -1;

    public MultitouchView(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setARGB(255,255,0,0);

        paintText = new Paint();
        paintText.setColor(Color.GRAY);
        paintText.setTextSize(200);

        this.setOnTouchListener(this);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);


        if (playerSelected == -1){
            for (int i = 0; i < map.size(); i++){
                int x = map.get(i).posX;
                int y = map.get(i).posY;
                int c = map.get(i).color;
                paint.setColor(c);
                canvas.drawCircle(x,y,SIZE, paint);
            }
        }else{
            for (int i = 0; i < map.size(); i++){
                int x = map.get(i).posX;
                int y = map.get(i).posY;
                if(i == playerSelected){
                    paint.setColor(Color.RED);
                }else{
                    paint.setColor(Color.BLACK);
                }
                canvas.drawCircle(x,y,SIZE, paint);
            }
        }

        canvas.drawText("1", 100,200, paintText);



    }

    @Override
    public boolean onTouch(View view, MotionEvent event){
        if (playerSelected!= -1){
            return true;
        }
        int pointerIndex = event.getActionIndex();
        int pointerID = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();
        Log.i("touch_enric", ""+ pointerID);
        switch (maskedAction){
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN: {
                int x = (int) event.getX(pointerIndex);
                int y = (int) event.getY(pointerIndex);
                Vector2D newPoint = new Vector2D(x, y, pointerID, colors[colorIndex% colors.length]);
                colorIndex++;
                map.add(newPoint);

                if (map.size()>1){
                    if (cTimer != null){
                        cTimer.cancel();
                    }
                    cTimer = new CountDownTimer(3000, 1000)
                    {
                        public void onTick(long millisUntilFinished) { }
                        public void onFinish() {

                            int min = 0;
                            int max = map.size();
                            Random random = new Random();
                            playerSelected = random.nextInt(max + min) + min;

                            invalidate();
                            if (cTimer != null){
                                cTimer.cancel();
                            }

                        }
                    };
                    cTimer.start();
                }


            }

                break;
            case MotionEvent.ACTION_MOVE:{
                int size = event.getPointerCount();
                for (int i = 0; i < size; i++) {

                    boolean found = false;
                    int index = 0;
                    while(!found && index < map.size()) {
                        if (map.get(index).ID == event.getPointerId(i)) {
                            map.get(index).posX = (int) event.getX(i);
                            map.get(index).posY = (int) event.getY(i);
                            found = true;
                        }
                        index++;
                    }
                }

            }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                if (cTimer != null){
                    cTimer.cancel();
                }
                boolean found = false;
                int index = 0;
                while(!found && index < map.size()){
                    if (map.get(index).ID == pointerID){
                        map.remove(index);
                        found = true;
                    }
                    index++;
                }
                break;
        }


        invalidate();

        return true;
    }
}
