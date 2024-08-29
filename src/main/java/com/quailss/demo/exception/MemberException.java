package com.quailss.demo.exception;

//잘못된 인자 전달
public class MemberException extends IllegalArgumentException {
    public MemberException() {super();}

    public MemberException(String message){
        super(message);
    }
}
