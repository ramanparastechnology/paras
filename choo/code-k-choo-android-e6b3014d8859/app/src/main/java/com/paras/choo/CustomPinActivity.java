package com.paras.choo;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.github.orangegangsters.lollipin.lib.managers.AppLockActivity;

import uk.me.lewisdeane.ldialogs.BaseDialog;
import uk.me.lewisdeane.ldialogs.CustomDialog;

/**
 * Created by oliviergoutay on dot1/14/15.
 */
public class CustomPinActivity extends AppLockActivity {

    @Override
    public void showForgotDialog() {
        Resources res = getResources();
        // Create the builder with required paramaters - Context, Title, Positive Text
        CustomDialog.Builder builder = new CustomDialog.Builder(this,
                res.getString(R.string.activity_dialog_title),
                res.getString(R.string.activity_dialog_accept));
        builder.content(res.getString(R.string.activity_dialog_content));
        builder.negativeText(res.getString(R.string.activity_dialog_decline));

        //Set theme
        builder.darkTheme(false);
        builder.typeface(Typeface.SANS_SERIF);
        builder.positiveColor(res.getColor(R.color.light_blue_500)); // int res, or int colorRes parameter versions available as well.
        builder.negativeColor(res.getColor(R.color.light_blue_500));
        builder.rightToLeft(false); // Enables right to left positioning for languages that may require so.
        builder.titleAlignment(BaseDialog.Alignment.CENTER);
        builder.buttonAlignment(BaseDialog.Alignment.CENTER);
        builder.setButtonStacking(false);

        //Set text sizes
        builder.titleTextSize((int) res.getDimension(R.dimen.activity_dialog_title_size));
        builder.contentTextSize((int) res.getDimension(R.dimen.activity_dialog_content_size));
        builder.positiveButtonTextSize((int) res.getDimension(R.dimen.activity_dialog_positive_button_size));
        builder.negativeButtonTextSize((int) res.getDimension(R.dimen.activity_dialog_negative_button_size));

        //Build the dialog.
        CustomDialog customDialog = builder.build();
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.setClickListener(new CustomDialog.ClickListener() {
            @Override
            public void onConfirmClick() {
                Toast.makeText(getApplicationContext(), "Yes", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelClick() {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        // Show the dialog.
        customDialog.show();
    }

    @Override
    public void onPinFailure(int attempts) {

    }

    @Override
    public void onPinSuccess(int attempts) {
//        Intent i = new Intent(this,SplashScreenActivity.class);
//        startActivity(i);
//        finish();
    }
@Override
protected void onPinCodeInputed() {
    switch (mType) {
        case AppLock.DISABLE_PINLOCK:
            if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                setResult(RESULT_OK);
                mLockManager.getAppLock().setPasscode(null);
                onPinCodeSuccess();
                finish();
            } else {
                onPinCodeError();
            }
            break;
        case AppLock.ENABLE_PINLOCK:
            mOldPinCode = mPinCode;
            setPinCode("");
            mType = AppLock.CONFIRM_PIN;
            setStepText();
            break;
        case AppLock.CONFIRM_PIN:
            if (mPinCode.equals(mOldPinCode)) {
                setResult(RESULT_OK);
                mLockManager.getAppLock().setPasscode(mPinCode);
                onPinCodeSuccess();
                finish();
            } else {
                mOldPinCode = "";
                setPinCode("");
                mType = AppLock.ENABLE_PINLOCK;
                setStepText();
                onPinCodeError();
            }
            break;
        case AppLock.CHANGE_PIN:
            if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                mType = AppLock.ENABLE_PINLOCK;
                setStepText();
                setPinCode("");
                onPinCodeSuccess();
            } else {
                onPinCodeError();
            }
            break;
        case AppLock.UNLOCK_PIN:
            if (mLockManager.getAppLock().checkPasscode(mPinCode)) {
                setResult(RESULT_OK);
                onPinCodeSuccess();
                Intent i = new Intent(this,SplashScreenActivity.class);
                startActivity(i);
                finish();
            } else {
                onPinCodeError();
            }
            break;
        default:
            break;
    }
}
    @Override
    public int getPinLength() {
        return super.getPinLength();//you can override this method to change the pin length from the default 4
    }
}
