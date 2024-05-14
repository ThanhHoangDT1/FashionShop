package com.androidexam.fashionshop.Fragment.Cart;

public class SharedData {
    private static boolean loadingFirstTime = true;

    public static boolean isLoadingFirstTime() {
        return loadingFirstTime;
    }

    public static void setLoadingFirstTime(boolean value) {
        loadingFirstTime = value;
    }
}
