package edu.upc.eetac.dsa.talk.dao;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by tono on 07/10/2015.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserAlreadyExistsException extends Exception {
}
