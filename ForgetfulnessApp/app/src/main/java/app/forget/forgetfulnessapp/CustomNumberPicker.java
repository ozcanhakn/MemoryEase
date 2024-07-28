package app.forget.forgetfulnessapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomNumberPicker extends View {
    private int minValue = 0;
    private int maxValue = 23;
    private int value = 0;
    private Paint paint;
    private Paint dimPaint; // Soluk renk için
    private int lastTouchY;
    private boolean isDragging = false;
    private int visibleItemCount = 5;
    private int halfVisibleItemCount = visibleItemCount / 2;
    private float scrollStep = 0.2f; // Duyarlılık
    private static final int SCROLL_DELAY = 16; // Kaydırma Hızı
    private float itemSpacing = 15f; // Sayılar arasındaki boşluk

    private Handler scrollHandler;


    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {


        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50 * getResources().getDisplayMetrics().scaledDensity); // Set text size to 16sp
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)); // Set font to "br_firma_bold"

        dimPaint = new Paint();
        dimPaint.setColor(Color.GRAY); // Soluk renk için gri kullanabilirsiniz
        dimPaint.setTextSize(50 * getResources().getDisplayMetrics().scaledDensity);
        dimPaint.setTextAlign(Paint.Align.CENTER);
        dimPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        scrollHandler = new ScrollHandler(this);
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
        invalidate();
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        invalidate();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = Math.max(minValue, Math.min(maxValue, value));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // Draw visible items
        for (int i = -halfVisibleItemCount; i <= halfVisibleItemCount; i++) {
            int displayedValue = value + i;
            float y = centerY + i * (paint.descent() - paint.ascent() + itemSpacing); // Boşluğu ayarla

            // Draw yellow frame for the selected item
            if (i == 0) {
                drawSelectionFrame(canvas, centerX, y);
            } else {
                // Draw dimmed text for other items
                drawDimmedText(canvas, displayedValue, centerX, y);
            }
        }
    }
    private void drawSelectionFrame(Canvas canvas, float x, float y) {
        Paint framePaint = new Paint();
        framePaint.setColor(Color.BLACK);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(5); // Adjust the frame width as needed

        String displayText = formatNumber(value + halfVisibleItemCount); // Format the number
        float halfTextWidth = paint.measureText(displayText) / 2;
        float halfTextHeight = (paint.descent() - paint.ascent()) / 2;

        float left = x - halfTextWidth - 10; // Adjust the frame padding as needed
        float top = y - halfTextHeight - 20; // Adjust the frame padding as needed
        float right = x + halfTextWidth + 10; // Adjust the frame padding as needed
        float bottom = y + halfTextHeight - 40; // Yükseltilen değer

        canvas.drawRect(left, top, right, bottom, framePaint);
        canvas.drawText(displayText, x, y, paint);
    }

    private void drawDimmedText(Canvas canvas, int value, float x, float y) {
        String displayText = formatNumber(value + halfVisibleItemCount); // Format the number
        canvas.drawText(displayText, x, y, dimPaint);
    }


    private String formatNumber(int number) {
        // Format the number as two digits
        return String.format("%02d", number);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isInsideNumberPicker(x, y)) {
                    lastTouchY = (int) y;
                    isDragging = true;
                    return true; // İşleme devam et
                } else {
                    return false; // Sayı alanının dışında dokunma işlemi
                }
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    int deltaY = (int) y - lastTouchY;
                    updateValue(deltaY);
                    lastTouchY = (int) y;
                }
                return true; // İşleme devam et
            case MotionEvent.ACTION_UP:
                isDragging = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isInsideNumberPicker(float x, float y) {
        // Sayı alanının sınırlarını belirleyerek kontrol et
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = centerX; // Sayı alanının yarı çapını alabilirsiniz

        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        return distance <= radius;
    }

    private void updateValue(int deltaY) {
        int step = (int) (deltaY * scrollStep);
        setValue(value - step);
        scrollHandler.removeMessages(ScrollHandler.SCROLL_MESSAGE);
        scrollHandler.sendMessageDelayed(Message.obtain(scrollHandler, ScrollHandler.SCROLL_MESSAGE, -step, 0), SCROLL_DELAY);
    }




    private static class ScrollHandler extends Handler {
        static final int SCROLL_MESSAGE = 1;

        private CustomNumberPicker customNumberPicker;

        ScrollHandler(CustomNumberPicker customNumberPicker) {
            this.customNumberPicker = customNumberPicker;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SCROLL_MESSAGE) {
                int step = msg.arg1;
                customNumberPicker.setValue(customNumberPicker.getValue() + step);
            }
        }
    }

}