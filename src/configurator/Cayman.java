/*
 * Developer: Jack Loss
 * File Name: Cayman.java
 * Date: October 2, 2017
 * Purpose: Creates Cayman class for instantiation in configurator GUI (MainWindow.java)
 */
package configurator;

import java.text.DecimalFormat;

/**
 * @author Jack
 */
public class Cayman {
    private static final DecimalFormat DF = new DecimalFormat("$#,###");
    
    private String extColorName;
    private int extColorPrice;
    private String extColorPath;
    private String wheelStyleName;
    private int wheelStylePrice;
    private String wheelStylePath;
    private String intColorName;
    private int intColorPrice;
    private String intColorPath;
    private String transType;
    private int transPrice;
    private String transPath;
    private StringBuilder optionNames = new StringBuilder();
    private StringBuilder optionPrices = new StringBuilder();
    
    public Cayman() {
        extColorName = "Guards Red";
        extColorPrice = 0;
        extColorPath = "guards-red";
        wheelStyleName = "19\" Cayman S Wheels";
        wheelStylePrice = 0;
        wheelStylePath = "19-cayman-s-wheels";
        intColorName = "Standard Black Interior";
        intColorPrice = 0;
        intColorPath = "black-interior";
        transType = "Manual";
        transPrice = 0;
        transPath = "manual-trans";
        optionNames.append("N/A");
        optionNames.append("N/A");
    }
    
    public String getExtColorName() {
        return extColorName;
    }
    
    public int getExtColorPrice() {
        return extColorPrice;
    }
    
    public String getExtColorPath() {
        return extColorPath;
    }
    
    public String getWheelStyleName() {
        return wheelStyleName;
    }
    
    public int getWheelStylePrice() {
        return wheelStylePrice;
    }
    
    public String getWheelStylePath() {
        return wheelStylePath;
    }
    
    public String getIntColorName() {
        return intColorName;
    }
    
    public int getIntColorPrice() {
        return intColorPrice;
    }
    
    public String getIntColorPath() {
        return intColorPath;
    }
    
    public String getTransType() {
        return transType;
    }
    
    public int getTransPrice() {
        return transPrice;
    }
    
    public String getTransPath() {
        return transPath;
    }
    
    public StringBuilder getOptionNames() {
        return optionNames;
    }
    
    public StringBuilder getOptionPrices() {
        return optionPrices;
    }
    
    public void setExtColor(String extColorName, int extColorPrice, String extColorPath) {
        this.extColorName = extColorName;
        this.extColorPrice = extColorPrice;
        this.extColorPath = extColorPath;
    }
    
    public void setWheelStyle(String wheelStyleName, int wheelStylePrice, String wheelStylePath) {
        this.wheelStyleName = wheelStyleName;
        this.wheelStylePrice = wheelStylePrice;
        this.wheelStylePath = wheelStylePath;
    }
    
    public void setIntColor(String intColorName, int intColorPrice, String intColorPath) {
        this.intColorName = intColorName;
        this.intColorPrice = intColorPrice;
        this.intColorPath = intColorPath;
    }
    
    public void setTrans(String transType, int transPrice, String transPath) {
        this.transType = transType;
        this.transPrice = transPrice;
        this.transPath = transPath;
    }
    
    public void addOption(String optionName, int optionPrice) {
        optionNames.append(optionName);
        optionNames.append("<br>");
        optionPrices.append(DF.format(optionPrice));
        optionPrices.append("<br>");
    }
}