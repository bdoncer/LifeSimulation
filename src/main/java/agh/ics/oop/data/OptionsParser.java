package agh.ics.oop.data;

public class OptionsParser {
    //zamienia liczby na ruchy
    public MoveDirection parse(int num) {
        MoveDirection res=MoveDirection.R;
        if (num == 0) {
            res=MoveDirection.F;
        }
        if (num == 1) {
            res=MoveDirection.FR;
        }
        if (num == 2) {
            res=MoveDirection.R;
        }
        if (num == 3) {
            res=MoveDirection.BR;
        }
        if (num == 4) {
            res=MoveDirection.B;
        }
        if (num == 5) {
            res=MoveDirection.BL;
        }
        if (num == 6) {
            res=MoveDirection.L;
        }
        if (num == 7) {
            res=MoveDirection.FL;
        }
        return res;

    }

}
