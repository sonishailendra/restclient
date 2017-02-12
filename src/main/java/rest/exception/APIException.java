package rest.exception;
/**
 * 
 * This Exception throws when status is not 2X.
 * @author Shailendra Soni
 *
 */
public class APIException extends Exception{

    private static final long serialVersionUID = 2772534795749113997L;
    
    public APIException(String message){
        super(message);
    }

}
