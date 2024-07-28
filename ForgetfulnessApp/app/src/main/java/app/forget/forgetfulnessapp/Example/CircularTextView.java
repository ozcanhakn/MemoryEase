package app.forget.forgetfulnessapp.Example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

public class CircularTextView extends View {
    private String text = "DENEME";
    private Paint textPaint;
    private Rect textBounds;
    private Path circlePath;
    private int centerX, centerY;
    private float radius;

    public CircularTextView(Context context) {
        super(context);
        init();
    }

    public CircularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER); // Yazının hizalanması

        textBounds = new Rect();
        circlePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(w, h) / 2f - 20; // 20px padding
        circlePath.reset();
        circlePath.addCircle(centerX, centerY, radius, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!text.isEmpty()) {
            textPaint.getTextBounds(text, 0, text.length(), textBounds);
            float hOffset = centerX;
            float vOffset = centerY - (textBounds.top + textBounds.bottom) / 2f; // Metni yukarıya hizalama
            canvas.drawTextOnPath(text, circlePath, 0, vOffset, textPaint);
        }
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
        invalidate();
    }
}