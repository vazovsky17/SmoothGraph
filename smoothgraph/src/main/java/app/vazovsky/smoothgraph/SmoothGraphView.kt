package app.vazovsky.smoothgraph

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import app.vazovsky.smoothgraph.model.Point

/**
 * A custom smooth graph with animation.
 * */
class SmoothGraphView : View {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private val defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12F, resources.displayMetrics)
    private val defaultElementSize by lazy { resources.getDimensionPixelSize(R.dimen.default_size_dp) }
    private val defaultMargin = resources.getDimensionPixelSize(R.dimen.default_margin).toFloat()
    private val defaultColor = Color.GRAY
    private val defaultBoolean = true
    private val defaultCountVisiblePoints = 0
    private val defaultHeight = resources.getDimensionPixelSize(R.dimen.default_graph_height)
    private val defaultWidth = resources.getDimensionPixelSize(R.dimen.default_graph_width)

    var pointColor = defaultColor
        set(value) {
            field = value
            invalidate()
        }
    var lineColor = defaultColor
        set(value) {
            field = value
            invalidate()
        }
    var titleColor = defaultColor
        set(value) {
            field = value
            invalidate()
        }
    var valueColor = defaultColor
        set(value) {
            field = value
            invalidate()
        }

    var titleShow = defaultBoolean
        set(value) {
            field = value
            invalidate()
        }
    var valueShow = defaultBoolean
        set(value) {
            field = value
            invalidate()
        }
    var pointShow = defaultBoolean
        set(value) {
            field = value
            invalidate()
        }

    var titleSize = defaultTextSize
        set(value) {
            field = value
            invalidate()
        }
    var valueSize = defaultTextSize
        set(value) {
            field = value
            invalidate()
        }
    var pointRadius = defaultElementSize.toFloat()
        set(value) {
            field = value
            invalidate()
        }
    var lineWidth = defaultElementSize.toFloat()
        set(value) {
            field = value
            invalidate()
        }

    var countVisiblePoints = defaultCountVisiblePoints
        set(value) {
            field = value
            invalidate()
        }

    private var points = emptyList<Point>()
    private var maxValue = 0F

    private var valueAnim = 1F
    private var animation: ValueAnimator? = null

    private val titlePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = titleColor
            textSize = titleSize
            textAlign = Paint.Align.CENTER
        }
    }

    private val valuePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = valueColor
            textSize = valueSize
            textAlign = Paint.Align.CENTER
        }
    }

    private val pointPaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = pointColor
            style = Paint.Style.FILL_AND_STROKE
        }
    }
    private val linePaint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = lineColor
            style = Paint.Style.STROKE
            strokeWidth = lineWidth
            pathEffect = CornerPathEffect(3.0F)
        }
    }

    private val titleBound = Rect()
    private val valueBound = Rect()


    private fun init(context: Context, attrs: AttributeSet?) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.SmoothGraphView, 0, 0)
        try {
            setColors(attributes)
            setShowConfig(attributes)
            setSizes(attributes)
            setCountPoints(attributes)
        } finally {
            attributes.recycle()
        }
    }

    /**
     * Colors initialization
     * */
    private fun setColors(attributes: TypedArray) = with(attributes) {
        pointColor = getColor(R.styleable.SmoothGraphView_graph_pointColor, defaultColor)
        lineColor = getColor(R.styleable.SmoothGraphView_graph_lineColor, defaultColor)
        titleColor = getColor(R.styleable.SmoothGraphView_graph_titleColor, defaultColor)
        valueColor = getColor(R.styleable.SmoothGraphView_graph_valueColor, defaultColor)
    }

    /**
     * Showing initialization
     * */
    private fun setShowConfig(attributes: TypedArray) = with(attributes) {
        titleShow = getBoolean(R.styleable.SmoothGraphView_graph_titleShow, defaultBoolean)
        valueShow = getBoolean(R.styleable.SmoothGraphView_graph_valueShow, defaultBoolean)
        pointShow = getBoolean(R.styleable.SmoothGraphView_graph_pointShow, defaultBoolean)
    }

    /**
     * Sizes initialization
     * */
    private fun setSizes(attributes: TypedArray) = with(attributes) {
        titleSize = getDimensionPixelSize(R.styleable.SmoothGraphView_graph_titleSize, defaultTextSize.toInt()).toFloat()
        valueSize = getDimensionPixelSize(R.styleable.SmoothGraphView_graph_valueSize, defaultTextSize.toInt()).toFloat()
        pointRadius = getDimensionPixelSize(R.styleable.SmoothGraphView_graph_pointRadius, defaultElementSize).toFloat()
        lineWidth = getDimensionPixelSize(R.styleable.SmoothGraphView_graph_lineWidth, defaultElementSize).toFloat()
    }

    /**
     * VisiblePointsCount initialization
     * */
    private fun setCountPoints(attributes: TypedArray) = with(attributes) {
        countVisiblePoints = getInteger(R.styleable.SmoothGraphView_graph_countVisiblePoints, defaultCountVisiblePoints)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val desiredWidth = defaultWidth
        val desiredHeight = defaultHeight

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width: Int = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> desiredWidth.coerceAtMost(widthSize)
            else -> desiredWidth
        }
        val height: Int = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> desiredHeight.coerceAtMost(heightSize)
            else -> desiredHeight
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (points.isNotEmpty()) {
            drawView(canvas)
        }
    }

    /**
     * Drawing the SmoothGraphView
     * */
    private fun drawView(canvas: Canvas) {
        val widthView = width - paddingStart - paddingEnd
        val countVisiblePoints =
            if (countVisiblePoints != 0 && countVisiblePoints <= points.size) countVisiblePoints else points.size
        val widthPart = (widthView / countVisiblePoints).toFloat()

        val copyPoints = points.subList(0, countVisiblePoints)

        val viewStartY = paddingTop.toFloat() + defaultMargin
        val viewEndY = (height - paddingBottom).toFloat() - defaultMargin

        drawLine(canvas, copyPoints, viewStartY, viewEndY, widthPart)
        drawOtherElements(canvas, copyPoints, viewStartY, viewEndY, widthPart)
    }

    /**
     * Drawing Texts and Points in SmoothGraph
     * */
    private fun drawOtherElements(canvas: Canvas, points: List<Point>, viewStartY: Float, viewEndY: Float, widthPart: Float) {
        var centerX = paddingStart.toFloat() + widthPart / 2
        for (i in points.indices) {
            getTextsBounds(points[i])

            val minY: Float = if (valueShow) viewStartY + valueBound.height() + defaultMargin else viewStartY
            val maxY: Float = if (titleShow) viewEndY - titleBound.height() - defaultMargin else viewEndY
            val endPointY = getPointY(points[i], minY, maxY)

            drawTexts(
                canvas, points[i], centerX,
                titleY = maxY + titleBound.height() + defaultMargin,
                valueY = endPointY - defaultMargin
            )
            if (pointShow) drawPoint(canvas = canvas, x = centerX, y = endPointY)
            centerX += widthPart
        }
    }

    /**
     * Drawing line in SmoothGraph
     * */
    private fun drawLine(canvas: Canvas, points: List<Point>, viewStartY: Float, viewEndY: Float, widthPart: Float) {
        var centerX = paddingStart.toFloat() + widthPart / 2
        var startPointX = centerX
        var startPointY = (height - paddingBottom).toFloat() - defaultMargin

        val line = Path()
        for (i in points.indices) {
            getTextsBounds(points[i])

            val minY: Float = if (valueShow) viewStartY + valueBound.height() + defaultMargin else viewStartY
            val maxY: Float = if (titleShow) viewEndY - titleBound.height() - defaultMargin else viewEndY
            val endPointY = getPointY(points[i], minY, maxY)

            // Если точка первая, то перемещаемся к ее координате, если нет и valueAnim == 0F, то рисуем график
            if (i == 0) line.moveTo(startPointX, endPointY)
            else if (valueAnim != 0F) line.cubicTo(startPointX, startPointY, startPointX, endPointY, centerX, endPointY)

            startPointX = centerX + widthPart / 2
            startPointY = endPointY
            centerX += widthPart
        }
        canvas.drawPath(line, linePaint)
    }

    /**
     * Setup TextBounds into Text Paints
     * */
    private fun getTextsBounds(point: Point) {
        titlePaint.getTextBounds(point.title, 0, point.title.length, titleBound)
        valuePaint.getTextBounds(point.value.toString(), 0, point.value.toString().length, valueBound)
    }

    /**
     * Found coordinate Y of Point
     * */
    private fun getPointY(point: Point, minY: Float, maxY: Float): Float {
        return if (valueAnim in 0F..0.1F) maxY - pointRadius
        else maxY - (maxY - minY) * point.value / maxValue * valueAnim + pointRadius
    }

    /**
     * Drawing title text and value text, if they are showing
     * */
    private fun drawTexts(canvas: Canvas, point: Point, x: Float, titleY: Float, valueY: Float) {
        if (titleShow)
            canvas.drawText(point.title, x, titleY, titlePaint)
        if (valueShow)
            canvas.drawText(point.value.toString(), x, valueY, valuePaint)
    }

    /**
     * Drawing Point in canvas
     * */
    private fun drawPoint(canvas: Canvas, x: Float, y: Float) {
        canvas.drawCircle(x, y, pointRadius, pointPaint)
    }

    /**
     * Setup data list in SmoothGraph
     * */
    fun setData(data: List<Point>) {
        points = data
        maxValue = points.maxOfOrNull { it.value } ?: 100F
        invalidate()
    }

    /**
     * Start animation
     * */
    fun startAnim() {
        animation?.cancel()
        animation = ValueAnimator.ofFloat(0F, 1F).apply {
            duration = 1000
            addUpdateListener { animation ->
                valueAnim = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }
}