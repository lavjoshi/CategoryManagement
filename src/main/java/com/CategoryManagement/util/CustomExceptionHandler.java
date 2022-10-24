package com.CategoryManagement.util;

import com.CategoryManagement.controller.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ResponseDTO> handleException(IllegalArgumentException exception) {
        log.error("Constraint violation ", exception);
        ResponseDTO responseDTO = ResponseDTO.builder().isError(Boolean.TRUE).message(exception.getMessage()).build();
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ResponseDTO> handleDBException(ConstraintViolationException exception) {
        log.error("Constraint violation ", exception);
        ResponseDTO responseDTO = ResponseDTO.builder().isError(Boolean.TRUE).message(exception.getSQLException().getMessage()).build();
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ResponseDTO> handleNotFoundException(NoSuchElementException exception) {
        log.error("Not found ", exception);
        ResponseDTO responseDTO = ResponseDTO.builder().isError(Boolean.TRUE).message(exception.getMessage()).build();
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({ProductMappedException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public ResponseEntity<ResponseDTO> handleProductMappedException(ProductMappedException exception) {
        log.error("ProductMappedException", exception);
        ResponseDTO responseDTO = ResponseDTO.builder().isError(Boolean.TRUE).message(exception.getMessage()).productIDList(exception.getIds()).build();
        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_ACCEPTABLE);
    }


}
