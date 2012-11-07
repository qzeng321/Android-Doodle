package zeng.doodle;

import java.util.ArrayList;
 
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new Panel(this));
    }
 
    class Panel extends SurfaceView implements SurfaceHolder.Callback {
        private TutorialThread _thread;
        private ArrayList<GraphicObject> _graphics = new ArrayList<GraphicObject>();
 
        public Panel(Context context) {
            super(context);
            getHolder().addCallback(this);
            _thread = new TutorialThread(getHolder(), this);
            setFocusable(true);
        }
 
private ArrayList _graphics = new ArrayList();
private Paint mPaint;
@Override
public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(new DrawingPanel(this));
  mPaint = new Paint();
  mPaint.setDither(true);
  mPaint.setColor(0xFFFFFF00);
  mPaint.setStyle(Paint.Style.STROKE);
  mPaint.setStrokeJoin(Paint.Join.ROUND);
  mPaint.setStrokeCap(Paint.Cap.ROUND);
  mPaint.setStrokeWidth(3);
}
        @Override
public boolean onTouchEvent(MotionEvent event) {
  synchronized (_thread.getSurfaceHolder()) {
    if(event.getAction() == MotionEvent.ACTION_DOWN){
      path = new Path();
      path.moveTo(event.getX(), event.getY());
      path.lineTo(event.getX(), event.getY());
    }else if(event.getAction() == MotionEvent.ACTION_MOVE){
      path.lineTo(event.getX(), event.getY());
    }else if(event.getAction() == MotionEvent.ACTION_UP){
      path.lineTo(event.getX(), event.getY());
      _graphics.add(path);
    }
    return true;
  }
}
@Override
public void onDraw(Canvas canvas) {
  for (Path path : _graphics) {
    //canvas.drawPoint(graphic.x, graphic.y, mPaint);
    canvas.drawPath(path, mPaint);
  }
}

 
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
        }
 
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            _thread.setRunning(true);
            _thread.start();
        }
 
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // simply copied from sample application LunarLander:
            // we have to tell thread to shut down & wait for it to finish, or else
            // it might touch the Surface after we return and explode
            boolean retry = true;
            _thread.setRunning(false);
            while (retry) {
                try {
                    _thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // we will try it again and again...
                }
            }
        }
    }
 
    class TutorialThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;
 
        public TutorialThread(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }
 
        public void setRunning(boolean run) {
            _run = run;
        }
 
        public SurfaceHolder getSurfaceHolder() {
            return _surfaceHolder;
        }
 
        @Override
        public void run() {
            Canvas c;
            while (_run) {
                c = null;
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        _panel.onDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
 
    class GraphicObject {
        /**
         * Contains the coordinates of the graphic.
         */
        public class Coordinates {
            private int _x = 100;
            private int _y = 0;
 
            public int getX() {
                return _x + _bitmap.getWidth() / 2;
            }
 
            public void setX(int value) {
                _x = value - _bitmap.getWidth() / 2;
            }
 
            public int getY() {
                return _y + _bitmap.getHeight() / 2;
            }
 
            public void setY(int value) {
                _y = value - _bitmap.getHeight() / 2;
            }
 
            public String toString() {
                return "Coordinates: (" + _x + "/" + _y + ")";
            }
        }
 
        private Bitmap _bitmap;
        private Coordinates _coordinates;
 
        public GraphicObject(Bitmap bitmap) {
            _bitmap = bitmap;
            _coordinates = new Coordinates();
        }
 
        public Bitmap getGraphic() {
            return _bitmap;
        }
 
        public Coordinates getCoordinates() {
            return _coordinates;
        }
    }
}
