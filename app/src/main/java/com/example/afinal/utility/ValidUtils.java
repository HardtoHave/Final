package com.example.afinal.utility;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUtils {
    //account cannot be null and must be australia number
    public static boolean isPhoneValid(String account) {
        if (account == null) {
            return false;
        }
        // aus number
        String pattern = "^(?:\\+?(61))? ?(?:\\((?=.*\\)))?(0?[2-57-8])\\)? ?(\\d\\d(?:[- ](?=\\d{3})|(?!\\d\\d[- ]?\\d[- ]))\\d\\d[- ]?\\d[- ]?\\d{3})$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(account);
        return m.matches();
    }

    // password cannot less than 6 digit
    public static boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


    public static boolean isEmailValid(String email) {
        try {
            String check = "^[A-Za-z0-9-._]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,6})$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * MD5 encrypt and BASE64 decrypt
     * @return encrypt string
     */
    public static String encodeByMd5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return new String(Base64.encode(md5.digest(str.getBytes(StandardCharsets.UTF_8)), Base64.NO_WRAP));

    }

}
