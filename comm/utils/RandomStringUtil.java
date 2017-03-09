package com.siweidg.comm.utils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class RandomStringUtil {  
	/** 
     * 每位允许的字符 
     */  
    private static final String POSSIBLE_CHARS="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";  
      
    /** 
     * 生产一个指定长度的随机字符串 
     * @param length 字符串长度 
     * @return 
     */  
    @SuppressWarnings("unused")
	public static String generateRandomString(int length) {  
        StringBuilder sb = new StringBuilder(length);  
        SecureRandom random = new SecureRandom();  
        for (int i = 0; i < length; i++) {  
            sb.append(POSSIBLE_CHARS.charAt(random.nextInt(POSSIBLE_CHARS.length())));  
        }  
        return sb.toString();  
    }  
      
    /** 
     * @param args 
     */  
//    public static void main(String[] args) {  
//        Set<String> check = new HashSet<String>();  
//        RandomString obj = new RandomString();  
//          
//        //生成2000000个随机字符串，检查是否出现重复  
//        for (int i = 0; i < 2; i++) {  
//            String s = obj.generateRandomString(16); 
//            System.out.println(s);
//            if (check.contains(s)) {                  throw new IllegalStateException("Repeated string found : " + s);  
//            } else {  
//                if (i % 1000 == 0) System.out.println("generated " + i / 1000 + "000 strings.");  
//                check.add(s);  
//            }  
//        }  
//    }

}