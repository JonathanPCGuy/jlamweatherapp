package com.hp.jlam.practice;

/**
 * Created by lamjon on 7/12/2014.
 */
public class Utilities
{
    private static String TEMP_UNIT_CELSIUS = "°C";
    private static String TEMP_UNIT_FAHRENHEIT = "°F";
    private static String TEMP_UNIT_KELVIN = "°K";

    public static long ConvertToF(double temp)
    {
        return Math.round((ConvertToC(temp)) * 1.8 + 32);
    }

    public static double ConvertToC(double temp)
    {
        return (temp) - 273.15;
    }

    // we could add an option to not include the unit of measurement
    public static String GetFormattedTempString(double value, TempDisplayUnit displayUnit)
    {
        // switch doesn't work for this type of stuff with java version used in android.
        // going to use if/else

        if(displayUnit == TempDisplayUnit.CELSIUS)
        {
            double convertedTempValue = ConvertToC(value);
            return String.format("%.1f %s", convertedTempValue,TEMP_UNIT_CELSIUS);
        }
        else if (displayUnit == TempDisplayUnit.FAHRENHEIT)
        {
            long convertedTempValue = ConvertToF(value);
            return String.format("%d %s", convertedTempValue, TEMP_UNIT_FAHRENHEIT);
        }
        else
        {
            return String.format("%.0f %s", value, TEMP_UNIT_KELVIN);
        }
    }
}
