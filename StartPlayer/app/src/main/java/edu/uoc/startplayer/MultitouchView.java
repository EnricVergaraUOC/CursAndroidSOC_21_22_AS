package edu.uoc.startplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class MultitouchView extends View implements View.OnTouchListener {
    private Paint paint;
    private int x = 0;
    private int y = 0;
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
        x = (int) motionEvent.getX();
        y = (int) motionEvent.getY();
        invalidate();

        return true;
    }
}
