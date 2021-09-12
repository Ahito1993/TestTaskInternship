package com.game.utils;

import com.game.exception.BadRequestException;

import java.util.Calendar;
import java.util.Date;

public class Validator {
    public boolean isIdValid (Long id){
        if (id == null || (id instanceof Long) == false || id <=0) {
            return false;
        }
        return true;
    }

    public void checkName (String name) {
        if (name.length() > 12 || name == null || name.isEmpty()) {
            throw new BadRequestException();
        }
    }

    public void checkTitle (String title) {
        if (title.length() > 30) {
            throw new BadRequestException();
        }
    }

    public void checkExperience (Integer experience) {
        if (experience < 0 || experience > 10000000) {
            throw new BadRequestException();
        }
    }

    public void checkBirthday (Date birthday) {
        if (birthday.getTime() < 0) {
            throw new BadRequestException();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthday);

        if (calendar.get(Calendar.YEAR) < 2000 || calendar.get(Calendar.YEAR) > 3000) {
            throw new BadRequestException();
        }
    }

    public Integer level (Integer experience) {
        return (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;
    }

    public Integer untilNextLevel (Integer level, Integer experienxe) {
        return 50 * (level + 1) * (level + 2) - experienxe;
    }
}
