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
public class BasicCounter {
    public static final int UNKNOWN = -1;

    private int total = UNKNOWN;
    private int count = UNKNOWN;

    public void count() {
        if (count < 0) {
            count = 0;
        }
        count++;
    }

    public void count(int addValue) {
        if (count < 0) {
            count = 0;
        }
        count += addValue;
    }
}
