package com.quailss.demo.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException() {super();}

    public EntityNotFoundException(String message) {
        super(message);
    }

    public static class MemberNotFoundException extends EntityNotFoundException{
        public MemberNotFoundException(String message){super(message);}
    }

    public static class BookmarkNotFoundException extends EntityNotFoundException{
        public BookmarkNotFoundException(String message){super(message);}
    }

    public static class RecipeNotFoundException extends EntityNotFoundException{
        public RecipeNotFoundException(String message){super(message);}
    }

    public static class ReviewNotFoundException extends EntityNotFoundException{
        public ReviewNotFoundException(String message){super(message);}
    }
}
