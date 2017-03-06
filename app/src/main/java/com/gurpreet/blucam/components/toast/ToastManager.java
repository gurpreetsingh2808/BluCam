package com.gurpreet.blucam.components.toast;

import android.widget.Toast;

import com.gurpreet.blucam.BlucamApplication;

/**
 * Created by Gurpreet on 04-03-2017.
 */

public class ToastManager {

    public static void showToast(String msg) {
        Toast.makeText(BlucamApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
