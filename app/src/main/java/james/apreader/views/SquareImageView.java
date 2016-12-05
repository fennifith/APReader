package james.apreader.views;

import android.content.Context;
import android.util.AttributeSet;

public class SquareImageView extends CustomImageView {

    public static final int VERTICAL = 0, HORIZONTAL = 1;
    private int orientation = VERTICAL;

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int size = getMeasuredWidth();
        if (orientation == HORIZONTAL) size = getMeasuredHeight();
        setMeasuredDimension(size, size);
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}