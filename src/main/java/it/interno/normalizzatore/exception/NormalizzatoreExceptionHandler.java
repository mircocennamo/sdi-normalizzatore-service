package it.interno.normalizzatore.exception;

import it.interno.normalizzatore.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class NormalizzatoreExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ResponseDto> exceptionGenericHandler() {
        String error = messageSource.getMessage("normalizzatore.errore.generico", null, LocaleContextHolder.getLocale());
        ResponseDto genericErr = ResponseDto.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).error(error).build();

        return ResponseEntity.internalServerError().body(genericErr);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ResponseDto> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException mame){

        String message = messageSource.getMessage("errore.valore.campo", new Object[]{mame.getName(), "".equals(mame.getValue()) ? "''" : "'" + mame.getValue() + "'"}, LocaleContextHolder.getLocale());
        ResponseDto responseDto = ResponseDto.builder().code(HttpStatus.BAD_REQUEST.value()).error(message).build();

        return ResponseEntity.badRequest().body(responseDto);
    }
}
