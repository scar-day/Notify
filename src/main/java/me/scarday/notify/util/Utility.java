package me.scarday.notify.util;

import lombok.experimental.UtilityClass;

import static java.lang.Math.abs;

@UtilityClass
public class Utility {
    public String declineAttempts(long number, String caseOne, String caseTwo, String caseFive) {

        String str;
        number = abs(number);

        if (number % 10 == 1 && number % 100 != 11) {
            str = number + " " + caseOne;
        } else if (number % 10 >= 2 && number % 10 <= 4 && (number % 100 < 10 || number % 100 >= 20)) {
            str = number + " " + caseTwo;
        } else {
            str = number + " " + caseFive;
        }

        return str;
    }
}
