package com.busteamproject.api;

public class UserGPS {
    static double x,y;

    public static double getX() {
        return x;
    }

    public static void setLocation(double x, double y) {
        UserGPS.x= x;
        UserGPS.y=y;
    }

    public static double getY() {
        return y;
    }
}
