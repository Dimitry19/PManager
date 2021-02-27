package cm.packagemanager.pmanager.api;


import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
	private String[] codes;
	private String message;

	//protected final Log logger = LogFactory.getLog(ApiError.class);


	public ApiError(String code, String message) {

		this.codes= new String[1];
		this.codes[0]=code;
		this.message=message;
		//logger.error(ApiError.class + message);
		System.out.println(message);
	}



	public ApiError(Exception ex) {
		//logger.error(ApiError.class +" {}" ,ex);
		StringBuilder stringBuilder = new StringBuilder();
		HttpMessageNotReadableException is;

		if( ex instanceof MethodArgumentNotValidException ){
			MethodArgumentNotValidException ob=(MethodArgumentNotValidException)ex;
			List<ObjectError> errors=ob.getBindingResult().getAllErrors();
			if (CollectionsUtils.isNotEmpty(errors)){
				this.codes= new String[errors.size()];
				for (int i=0;i<errors.size();i++)
				{
					ObjectError objectError=errors.get(i);
					this.codes[i]=objectError.getCode();
					stringBuilder.append(objectError.getDefaultMessage() +"\n");
				}
			}
		}

		if( ex instanceof HttpMessageNotReadableException ){
			HttpMessageNotReadableException ob=(HttpMessageNotReadableException)ex;
			stringBuilder.append(ob.getCause().getMessage());
		}
		this.message=stringBuilder.toString();
	}
}