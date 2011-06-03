package com.alok.util;


import java.lang.reflect.Constructor;

public final class Errors {

    public static final RuntimeException bombUnless(boolean condition, String message){
        return bombIf(!condition, message, RuntimeException.class);
    }

    public static final RuntimeException bombIf(boolean condition, String message){
        return bombIf(condition, message, RuntimeException.class);
    }

    public static final RuntimeException bombIf(boolean condition, String message, Class<? extends RuntimeException> e){
        if(condition){
            try {
                Constructor<? extends RuntimeException> constructor = e.getConstructor(String.class);
                throw constructor.newInstance(message);
            } catch (Exception e1) {
                e1.printStackTrace();
        }
        }
        return null;
    }
}

