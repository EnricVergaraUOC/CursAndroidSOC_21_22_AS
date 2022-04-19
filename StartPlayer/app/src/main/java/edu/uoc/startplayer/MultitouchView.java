package edu.uoc.startplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class MultitouchView extends View implements View.OnTouchListener {

    private Paint paint;

    Map <Integer,Vector2D> map =  new HashMap<Integer,Vector2D>();
    public MultitouchView(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setARGB(255,255,0,0);

        this.setOnTouchListener(this);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        for (Map.Entry<Integer, Vector2D> pair: map.entrySet()) {
            int x = pair.getValue().GetPosX();
            int y = pair.getValue().GetPosY();
            canvas.drawCircle(x,y,100, paint);
        }



    }

    @Override
    public boolean onTouch(View view, MotionEvent event){

        int pointerIndex = event.getActionIndex();
        int pointerID = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        switch (maskedAction){
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN: {
                int x = (int) event.getX(pointerIndex);
                int y = (int) event.getY(pointerIndex);
                Vector2D newPoint = new Vector2D(x, y);
                map.put(pointerID, newPoint);
            }

                break;
            case MotionEvent.ACTION_MOVE:{
                int size = event.getPointerCount();
                for (int i = 0; i < size; i++) {
                    Vector2D point = map.get(event.getPointerId(i));
                    if (point != null) {
                        point.SetPosX((int)event.getX(i));
                        point.SetPosY((int)event.getY(i));
                    }
                }

                int x = (int)event.getX(pointerIndex);
                int y = (int)event.getY(pointerIndex);
                map.get(pointerID).SetPosX(x);
                map.get(pointerID).SetPosY(y);
            }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                map.remove(pointerID);
                break;
        }


        invalidate();

        return true;
    }
}
