package edu.uoc.startplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MultitouchView extends View implements View.OnTouchListener {

    private Paint paint;
    private Paint paintText;
    private static final int SIZE_CIRCLE = 200;
    private static final int SIZE_TEXT = 190;
    CountDownTimer cTimer = null;
    private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
            Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW };
    private int colorIndex = 0;

    ArrayList<Vector2D> map = new ArrayList<Vector2D>();
    private int playerSelected = -1;
    TextView lbl_info = null;
    Button btn_restart = null;

    public MultitouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setARGB(255,255,0,0);


        paintText = new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(SIZE_TEXT);

        this.setOnTouchListener(this);
    }

    public void initView(MainActivity activity){
        btn_restart = activity.findViewById(R.id.btn_restart);
        lbl_info = activity.findViewById(R.id.lbl_info);
        lbl_info.setText("");
        btn_restart.setVisibility(INVISIBLE);
        btn_restart.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                map.clear();
                playerSelected = -1;
                invalidate();
                btn_restart.setVisibility(INVISIBLE);
                lbl_info.setText("");
            }
        });
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
                canvas.drawCircle(x,y,SIZE_CIRCLE, paint);

            }
        }else{
            int theOthers = 2;
            String text = "";
            for (int i = 0; i < map.size(); i++){
                int x = map.get(i).posX;
                int y = map.get(i).posY;
                if(i == playerSelected){
                    paint.setColor(Color.RED);
                    text = "1";
                }else{
                    paint.setColor(Color.BLACK);
                    text = ""+theOthers;
                    theOthers++;
                }
                canvas.drawCircle(x,y,SIZE_CIRCLE, paint);
                canvas.drawText(text, x - (int)(SIZE_TEXT*0.25),y+(int)(SIZE_TEXT*0.25), paintText);
            }
        }





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
                    lbl_info.setText("Calculating player positions in...3");
                    cTimer = new CountDownTimer(3000, 1000)
                    {
                        public void onTick(long millisUntilFinished) {
                            int seconds = (int)(millisUntilFinished / 1000);
                            lbl_info.setText("Calculating player positions in..."+seconds);

                        }
                        public void onFinish() {

                            btn_restart.setVisibility(VISIBLE);
                            lbl_info.setText("");

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
                lbl_info.setText("");
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
