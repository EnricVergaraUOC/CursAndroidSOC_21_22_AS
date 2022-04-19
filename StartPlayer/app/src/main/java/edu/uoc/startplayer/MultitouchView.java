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
    private int x = 0;
    private int y = 0;

    Map <Integer,Vector2D> map =  new HashMap<Integer,Vector2D>();
    public MultitouchView(Context context) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setARGB(255,255,0,0);

        setFocusable(true);
        this.setOnTouchListener(this);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawCircle(x,y,100, paint);


    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int pointerIndex = motionEvent.getActionIndex();
        int pointerID = motionEvent.getPointerId(pointerIndex);
        int maskedAction = motionEvent.getActionMasked();

        switch (maskedAction){
            case MotionEvent.ACTION_DOWN:
                //TODO: registrar el touch con el id y pos
                motionEvent.getX(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                //TODO: actualizar la pos del touch con el id
                break;
            case MotionEvent.ACTION_CANCEL:
                //TODO: eliminar el touch cno el id.
                break;
        }





        invalidate();

        return true;
    }
}
