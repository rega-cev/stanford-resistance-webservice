/*##########################################################################*
 * Package
 *##########################################################################*/
package edu.stanford.hiv.webservices;

/*##########################################################################*
 * Begin code
 *##########################################################################*/

/**
 * Generic exception class for Sierra.
 * 
 * <p>History:
 * <table border="1">
 * <tr> <th>User</th> <th>Date</th> <th>Change Comment</th> </tr>
 * 
 * <tr> <td>bbetts</td> <td>27 November 2005</td> 
 * <td>Creation.
 * </td></tr>
 * 
 * </table> 
 */
public class SierraException extends Exception {
    private static final long serialVersionUID = 1;
    
    public SierraException(String msg) {
        super(msg);
    }
}
