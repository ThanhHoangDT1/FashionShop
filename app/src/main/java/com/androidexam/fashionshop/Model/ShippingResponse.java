package com.androidexam.fashionshop.Model;


public class ShippingResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data {
        private int feeShip;

        public int getFeeShip() {
            return feeShip;
        }
    }
}
