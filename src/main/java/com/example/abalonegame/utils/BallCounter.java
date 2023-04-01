/*
 * Author Artjoms Bogatirjovs 1.4.2023
 */

package com.example.abalonegame.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BallCounter {
    public static final int UNKNOWN = -1;

    private int ballTotal = UNKNOWN;
    private int ballCount = UNKNOWN;

    public void count() {
        if (ballCount < 0) {
            ballCount = 0;
        }
        ballCount++;
    }

    public void count(int aBalls) {
        if (ballCount < 0) {
            ballCount = 0;
        }
        ballCount++;
    }
}
