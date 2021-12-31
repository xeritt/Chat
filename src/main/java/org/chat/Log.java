package org.chat;

public interface Log {
    default void log(String str){
        System.out.println(str);
    }
}
