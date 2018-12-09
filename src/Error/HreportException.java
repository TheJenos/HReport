/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Error;

/**
 *
 * @author TheJenos
 */
public class HreportException extends Exception{

    public HreportException() {
    }

    public HreportException(String message) {
        super(message);
    }

    public HreportException(String message, Throwable cause) {
        super(message, cause);
    }

    public HreportException(Throwable cause) {
        super(cause);
    }
    
}
