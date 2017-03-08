package com.nichesoftware.giftlist.views.addimage;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

import com.nichesoftware.giftlist.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Add image dialog
 */
public class AddImageDialog extends AppCompatDialog {
    // Constants   ---------------------------------------------------------------------------------
    private final static String TAG = AddImageDialog.class.getSimpleName();

    // Fields   ------------------------------------------------------------------------------------
    /**
     * Listener
     */
    private OnDialogListener mListener;
    /**
     * Unbinder Butter Knife
     */
    private Unbinder mButterKnifeUnbinder;

    @OnClick(R.id.add_gift_add_image_select_picture)
    void onSelectPictureClick() {
        if (mListener != null) {
            mListener.onSelectPicture();
        }
    }

    @OnClick(R.id.add_gift_add_image_take_picture)
    void onTakePictureClick() {
        if (mListener != null) {
            mListener.onTakePicture();
        }
    }

    public AddImageDialog(Context context, final OnDialogListener listener, final String title) {
        super(context, R.style.AppTheme_Dark_Dialog);

        this.mListener = listener;

        setTitle(title);

        setContentView(R.layout.add_gift_add_image_dialog);
        mButterKnifeUnbinder = ButterKnife.bind(this);

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Inner class                                         //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public interface OnDialogListener {
        void onSelectPicture();
        void onTakePicture();
    }
}
