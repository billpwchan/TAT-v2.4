/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * @author T0155040
 */
public class util {

    static long tempsDebut, tempsFin;

    /**
     *
     */
    protected util() {

    }

    /**
     *
     */
    public static void startTime() {
        tempsDebut = System.currentTimeMillis();
    }

    /**
     *
     */
    public static void endTime() {
        tempsFin = System.currentTimeMillis();
        float seconds = (tempsFin - tempsDebut) / 1000F;
        //System.out.println("TEMPS = " + Float.toString(seconds));
    }

}
