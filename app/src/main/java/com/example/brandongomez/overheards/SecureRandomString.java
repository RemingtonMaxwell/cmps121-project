package com.example.brandongomez.overheards;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Courtesy of Professor Luca De Alfaro **/
public final class SecureRandomString {
    private SecureRandom random = new SecureRandom();

    public String nextString() {
        return new BigInteger(130, random).toString(32);
    }

}
