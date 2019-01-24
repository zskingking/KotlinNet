package com.example.administrator.kotlindemo

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Created by Administrator on 2019/1/23.
 */
class NetView : View {
    private var mContext:Context
    private lateinit var mPaint: Paint//蜘蛛网画笔
    private lateinit var mTextPaint: Paint//文字画笔
    private lateinit var mPathPaint:Paint//属性画笔

    private var mList = ArrayList<Bean>()

    private var mWidth:Int = 0//宽
    private var mHeight:Int = 0//高

    private var mArrX = FloatArray(6)//记录外圈6个点x轴坐标
    private var mArrY = FloatArray(6)//记录外圈6个点y轴坐标


    private var mNetColor:Int = 0//蜘蛛网颜色
    private var mPathColor:Int = 0//属性区域颜色
    private var mTextColor:Int = 0//文字颜色
    private var mNetWidth:Float = 0.0f//蜘蛛网线宽度
    private var mTextSize:Float = 0.0f//文字大小

    private var radius = 0//网图半径
    private var percentage:Float = 0f//用于属性动画

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        mContext = context
        var type = context.theme.obtainStyledAttributes(attrs,R.styleable.net,defStyleAttr,0)
        mNetColor = type.getColor(R.styleable.net_netColor,Color.GREEN)
        mPathColor = type.getColor(R.styleable.net_pathColor,Color.BLACK)
        mTextColor = type.getColor(R.styleable.net_textColor,Color.BLACK)
        mNetWidth = type.getDimension(R.styleable.net_netWidth,6f)
        mTextSize = type.getDimension(R.styleable.net_textSize,30f)
        type.recycle()

        initCompute()

    }

    private fun initCompute(){
        mPaint = Paint()
        mPaint.color = mNetColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mNetWidth
        mPaint.isAntiAlias = true

        mPathPaint = Paint()
        mPathPaint.style = Paint.Style.FILL
        mPathPaint.color = mPathColor
        mPathPaint.isAntiAlias = true

        mTextPaint = Paint()
        mTextPaint.color = mTextColor
        mTextPaint.textSize = mTextSize
        mTextPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = resolveSize(600,widthMeasureSpec)
        mHeight = resolveSize(400,heightMeasureSpec)
        setMeasuredDimension(mWidth,mHeight)
        radius = mWidth/3
        //获取到6个点
        for (i in 0..5){
            mArrX[i] = (radius*Math.cos((Math.PI/3)*i).toFloat())
            mArrY[i] = (radius*Math.sin((Math.PI/3)*i)).toFloat()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {it.translate((mWidth/2).toFloat(), (mHeight/2).toFloat())}
        drawBack(canvas)
        drawText(canvas)
        drawValue(canvas)
    }

    //绘制底图
    private fun drawBack(canvas: Canvas?){
        var path = Path()
        //绘制网装图
        for (i in 1..5) {
            for (j in 0..5) {
                if (j == 0) {
                    path.moveTo(mArrX[j]*i*0.2.toFloat(), mArrY[j]*i*0.2.toFloat())
                } else {
                    path.lineTo(mArrX[j]*i*0.2.toFloat(), mArrY[j]*i*0.2.toFloat())
                }
            }
            path.close()
        }
        canvas?.let { it.drawPath(path,mPaint) }
        //绘制直线
        for(i in 0..5){
            canvas?.let {it.drawLine(0f,0f,mArrX[i],mArrY[i],mPaint) }
        }
    }

    //绘制文字
    private fun drawText(canvas: Canvas?){
        if(mList.size!=6)return
        for (i in 0..5){
            var content = mList[i].key
            val textBound = Rect()
            mPaint.getTextBounds(content, 0, content.length, textBound)
            canvas?.let{it.drawCircle(mArrX[i],mArrY[i],10f,mPathPaint)}
            canvas?.let{ it.drawText(content,mArrX[i]*1.2f- textBound.width()-14
                    ,mArrY[i]*1.2f+ textBound.height() / 2+7,mTextPaint) }
        }
    }

    //绘制属区域
    private fun drawValue(canvas: Canvas?){
        if(mList.size!=6)return
        var path = Path()
        for (j in 0..5) {
            var value = mList[j].value
            if (j == 0) {
                path.moveTo(mArrX[j]*value*percentage, mArrY[j]*value*percentage)
            } else {
                path.lineTo(mArrX[j]*value*percentage, mArrY[j]*value*percentage)
            }
        }
        path.close()
        canvas?.let{it.drawPath(path,mPathPaint)}
    }

    fun setPercentage(percentage:Float){
        this.percentage = percentage
        postInvalidate()
    }

    fun start(list:ArrayList<Bean>){
        this.mList = list
        var animator = ObjectAnimator.ofFloat(this,"percentage",0f,1f)
        animator.setDuration(1000)
                .start()
    }
}