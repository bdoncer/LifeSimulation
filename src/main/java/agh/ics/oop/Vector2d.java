package agh.ics.oop;

import java.util.Objects;

public class Vector2d {
    public int x;
    public int y;
    public Vector2d(int x,int y){
        this.x = x;
        this.y = y;
    }


    public String toString(){
            return "(" + this.x + "," + this.y +")";
    }
    boolean precedes(Vector2d other){
        if (this.x <= other.x && this.y <= other.y){
            return true;
        }
        return false;
    }
    boolean follows(Vector2d other){
        if (this.x >= other.x && this.y >= other.y){
            return true;
        }
        return false;
    }
    Vector2d upperRight(Vector2d other){
        int new_x;
        int new_y;
        if (this.x > other.x){
            new_x = this.x;
        }
        else{
            new_x = other.x;
        }
        if (this.y > other.y){
            new_y = this.y;
        }
        else{
            new_y = other.y;
        }
        Vector2d up = new Vector2d(new_x,new_y);
        return up;
    }
    Vector2d lowerLeft(Vector2d other){
        int new_x;
        int new_y;
        if (this.x < other.x){
            new_x = this.x;
        }
        else{
            new_x = other.x;
        }
        if (this.y < other.y){
            new_y = this.y;
        }
        else{
            new_y = other.y;
        }
        Vector2d up = new Vector2d(new_x,new_y);
        return up;
    }
    Vector2d add(Vector2d other){
        int new_x = this.x + other.x;
        int new_y = this.y + other.y;
        Vector2d added = new Vector2d(new_x,new_y);
        return added;
    }
    Vector2d subtract(Vector2d other){
        Vector2d substracted = new Vector2d(this.x-other.x,this.y-other.y);
        return substracted;
    }
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
    Vector2d opposite(){
        Vector2d reversed = new Vector2d(-this.x,-this.y);
        return reversed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }


}
