package agh.ics.oop;

public enum MapDirection {
    N,NE,E,SE,S,SW,W,NW;
    public String toString(){
        switch(this) {
            case N:
                return "Północ";
            case S:
                return "Południe";
            case W:
                return "Zachód";
            case E:
                return "Wschód";
            case NE:
                return "Północny-wschód";
            case SE:
                return "Południowy-wschód";
            case SW:
                return "Południowy-zachód";
            case NW:
                return "Północny-wschód";

        }
        return null;
    }
    MapDirection next(){
        switch(this) {
            case N:
                return MapDirection.NE;
            case S:
                return MapDirection.SW;
            case W:
                return MapDirection.NW;
            case E:
                return MapDirection.SE;
            case NE:
                return MapDirection.E;
            case SE:
                return MapDirection.S;
            case SW:
                return MapDirection.W;
            case NW:
                return MapDirection.N;
        }
        return null;
    }
    MapDirection previous(){
        switch(this) {
            case N:
                return MapDirection.NW;
            case S:
                return MapDirection.SE;
            case W:
                return MapDirection.SW;
            case E:
                return MapDirection.NE;
            case NE:
                return MapDirection.N;
            case SE:
                return MapDirection.E;
            case SW:
                return MapDirection.S;
            case NW:
                return MapDirection.W;
        }
        return null;
    }
    Vector2d toUnitVector(){
        int x=0;
        int y=0;
        switch(this) {
            case N:
                x = 0;
                y = 1;
                break;
            case S:
                x = 0;
                y = -1;
                break;
            case W:
                x = -1;
                y = 0;
                break;
            case E:
                x = 1;
                y = 0;
                break;
            case NE:
                x = 1;
                y = 1;
                break;
            case SE:
                x = 1;
                y = -1;
                break;
            case SW:
                x = -1;
                y = -1;
                break;
            case NW:
                x = -1;
                y = 1;
                break;
        }
        return new Vector2d(x,y);
    }
}
