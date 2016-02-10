package com.example.brandongomez.cs121asg3;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by brandongomez on 2/8/16.
 */
public class SecureRandomString {
    private SecureRandom random = new SecureRandom();

    public String nextString() {
        return new BigInteger(130, random).toString(32);
    }

}
