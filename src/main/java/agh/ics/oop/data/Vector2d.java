package agh.ics.oop.data;

import java.util.Objects;

public class Vector2d {
    public int x;
    public int y;
    public Vector2d(int x,int y){
        this.x = x;
        this.y = y;
    }

    public Vector2d add(Vector2d other){
        int new_x = this.x + other.x;
        int new_y = this.y + other.y;
        Vector2d added = new Vector2d(new_x,new_y);
        return added;
    }
    //sprawdza rownosc 2 wektorow
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        if (this.x == that.x && this.y == that.y) {
            return true;
        }
        return false;
    }

    public Vector2d opposite(){
        Vector2d reversed = new Vector2d(-this.x,-this.y);
        return reversed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }


}
