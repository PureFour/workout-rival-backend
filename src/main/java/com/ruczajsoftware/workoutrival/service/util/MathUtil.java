package com.ruczajsoftware.workoutrival.service.util;

public final class MathUtil {

    public static float convertCentimetersToMeters(float centimeters){
        final float meters = centimeters / 100f;
        return (float)Math.round(meters * 100) / 100f;
    }

}
