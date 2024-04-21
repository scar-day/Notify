package me.scarday.notify.util;

import lombok.experimental.UtilityClass;

import java.util.List;

import static java.lang.Math.abs;

@UtilityClass
public class MessageUtility {
    public String declineAttempts(long number, List<String> attemptsCases) {

        number = abs(number);
        int mod10 = (int) (number % 10);
        int mod100 = (int) (number % 100);

        String str;
        if (mod10 == 1 && mod100 != 11) {
            str = number + " " + attemptsCases.get(0) + attemptsCases.get(1);
        } else if (mod10 >= 2 && mod10 <= 4 && (mod100 < 10 || mod100 >= 20)) {
            str = number + " " + attemptsCases.get(0) + attemptsCases.get(2);
        } else {
            str = number + " " + attemptsCases.get(0) + attemptsCases.get(3);
        }

        return str;
    }
}
