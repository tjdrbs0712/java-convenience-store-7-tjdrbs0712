package store.error;

import store.message.ErrorMessage;

public class InputException extends IllegalArgumentException{
    public InputException(ErrorMessage errorMessage){
        super(errorMessage.getMessage());
    }
}
