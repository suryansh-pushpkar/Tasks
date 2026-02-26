package com.user.utility;
import java.security.SecureRandom;

public class OtpGenerator {
   
    private static String currentOtp; 

    public static String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 4; i++) {
            sb.append(secureRandom.nextInt(10)); 
        }
        
        currentOtp = sb.toString();
        System.out.println("Generated OTP: " + currentOtp);
        return currentOtp;
    }
    
    public static boolean verifyOtp(String userOtp) {
        if (userOtp == null || currentOtp == null) {
            return false;
        }
        return userOtp.equals(currentOtp);
    }
}