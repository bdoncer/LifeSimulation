package agh.ics.oop;

import java.util.ArrayList;
import java.util.Objects;

public class OptionsParser {
    public MoveDirection[] parse(String[] tab) {
        int lenn = tab.length;
        MoveDirection[] res = new MoveDirection[lenn];
        for(int i=0;i < lenn;i++) {
            if (Objects.equals(tab[i], "0")) {
                res[i]=MoveDirection.F;
            }
            if (Objects.equals(tab[i], "1")) {
                res[i]=MoveDirection.FR;
            }
            if (Objects.equals(tab[i], "2")) {
                res[i]=MoveDirection.R;
            }
            if (Objects.equals(tab[i], "3")) {
                res[i]=MoveDirection.BR;
            }
            if (Objects.equals(tab[i], "4")) {
                res[i]=MoveDirection.B;
            }
            if (Objects.equals(tab[i], "5")) {
                res[i]=MoveDirection.BL;
            }
            if (Objects.equals(tab[i], "6")) {
                res[i]=MoveDirection.L;
            }
            if (Objects.equals(tab[i], "7")) {
                res[i]=MoveDirection.FL;
            }

        }
        return res;
    }
}
