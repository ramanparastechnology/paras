package com.paras.choo.utils;

import java.util.Random;

/**
 * Created by paras on 23-07-2015.
 */
public class MyStringRandomGen {

    private static final String CHAR_LIST =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STRING_LENGTH = 12;

    /**
     * This method generates random string
     * @return
     */
    public MyStringRandomGen(int RANDOM_STRING_LENGTH){
        RANDOM_STRING_LENGTH = this.RANDOM_STRING_LENGTH;
    }
    public String generateRandomString(){

        StringBuffer randStr = new StringBuffer();
        for(int i=0; i<RANDOM_STRING_LENGTH; i++){
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    /**
     * This method generates random numbers
     * @return int
     */
    private int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    public static void main(String a[]){
        MyStringRandomGen msr = new MyStringRandomGen(10);
        System.out.println(msr.generateRandomString());
        System.out.println(msr.generateRandomString());
        System.out.println(msr.generateRandomString());
        System.out.println(msr.generateRandomString());
        System.out.println(msr.generateRandomString());
        System.out.println(msr.generateRandomString());
        System.out.println(msr.generateRandomString());
    }
//    - See more at: http://www.java2novice.com/java-collections-and-util/random/string/#sthash.rfvWoKMW.dpuf
}
