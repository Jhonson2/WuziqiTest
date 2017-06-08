package com.example.dellc.wuziqitest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dellc on 2017/6/8.
 */

public class WuziqiPanel extends View {
    private int mPanelWidth;//定义棋盘
    private float mLineHeight;
    private int MAX_LINE=10;

    private Paint mPaint=new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    //自定义：棋子的比例3：4
    private float ratioPieceOfLineHeight=3 * 1.0f / 4;

    //白棋先手 当前轮到白棋
    private boolean mIsWhite=true;
    //定义黑白棋的坐标数组列表
    private List<Point> mWhiteArray=new ArrayList<>();
    private List<Point> mBlackArray=new ArrayList<>();

    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        //棋盘线画笔设置
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        //黑白棋子
        mWhitePiece=BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece=BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);

        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        int width=Math.min(widthSize,heightSize);

        if(widthMode==MeasureSpec.UNSPECIFIED){
            width=heightSize;
        }else if(heightMode==MeasureSpec.UNSPECIFIED){
            width=widthSize;
        }
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth=w;
        mLineHeight=mPanelWidth * 1.0f / MAX_LINE;

        //修改棋子在棋盘LineHeight的尺寸
        int pieceWidth= (int) (mLineHeight*ratioPieceOfLineHeight);

        mWhitePiece=Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece=Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        if(action==MotionEvent.ACTION_UP){
            int x= (int) event.getX();
            int y= (int) event.getY();

            //新建棋子坐标
           /* Point p=new Point(x,y);  具体坐标行不通*/

            Point p=getValidPoint(x,y);//要设置（0，0）（0，1）...这样的坐标

            //当前棋盘的位置是否已经存在棋子
            if(mWhiteArray.contains(p) || mBlackArray.contains(p)){
                return false;
            }

            if(mIsWhite){
               mWhiteArray.add(p); //白棋坐标放在白棋数组列表
            }else{
                mBlackArray.add(p);// 否则放在黑棋数组列表
            }
            invalidate();//请求重回
            mIsWhite=!mIsWhite;//黑白棋互变

        }
            return true;
    }
    //要设置（0，0）（0，1）...这样的坐标
    private Point getValidPoint(int x, int y) {
        return new Point((int)(x / mLineHeight),(int)(y/mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBorad(canvas);//绘制棋盘
    }

    private void drawBorad(Canvas canvas) {
        int w=mPanelWidth;
        float lineHeight=mLineHeight;

        for(int i=0;i<MAX_LINE;i++){
            int startX= (int) (lineHeight/2);
            int endX= (int) (w-lineHeight/2);

            int y= (int) ((0.5+i)*lineHeight);

            canvas.drawLine(startX,y,endX,y,mPaint);
            canvas.drawLine(y,startX,y,endX,mPaint);
        }

    }
}
