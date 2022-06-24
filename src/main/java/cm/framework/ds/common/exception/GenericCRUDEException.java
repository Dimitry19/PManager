package cm.framework.ds.common.exception;

import cm.framework.ds.common.enums.GenericCRUDEEnum;

public class GenericCRUDEException extends RuntimeException{

	public GenericCRUDEException(String message){
		super(message);
	}

	public GenericCRUDEException(String message, GenericCRUDEEnum operation){

		StringBuilder error =new StringBuilder(operation.toValue());
		error.append("Error:").append(message);
		throw new RuntimeException(message);

	}
}
