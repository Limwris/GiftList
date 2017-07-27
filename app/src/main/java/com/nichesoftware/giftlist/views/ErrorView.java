package com.nichesoftware.giftlist.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nichesoftware.giftlist.R;

/**
 * View displayed if an error happened
 */
public class ErrorView extends LinearLayout {
    private static final String TAG = ErrorView.class.getSimpleName();

    /**
     * TextView contenant le message d'erreur à afficher
     */
    private TextView messageTextView;

    /**
     * Constructeur
     * @param context
     */
    public ErrorView(Context context) {
        super(context);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * Méthode d'initialisation de la vue
     * @param context
     */
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.error_view, this, true);

        messageTextView = (TextView) findViewById(R.id.error_view_text);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.ErrorView, 0, 0);
            final String message = a.getString(R.styleable.ErrorView_message);
            messageTextView.setText(message);
            a.recycle();
        }
    }

    /**
     * Méthode permettant d'afficher le message d'erreur
     * @param message
     */
    public void setMessage(final String message) {
        if (messageTextView != null) {
            messageTextView.setText(message);
        }
    }
}
