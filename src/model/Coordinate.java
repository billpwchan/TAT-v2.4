/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * @author tmorin
 */
public class Coordinate {

    private final int x;
    private final int y;

    /**
     * @param int1
     * @param int2
     */
    public Coordinate(int int1, int int2) {
        this.x = int1;
        this.y = int2;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate cood = (Coordinate) o;
        return x == cood.x && y == cood.y;

    }

    @Override
    public int hashCode() {
        int result = this.x;
        result = 31 * result + y;
        return result;
    }


}
