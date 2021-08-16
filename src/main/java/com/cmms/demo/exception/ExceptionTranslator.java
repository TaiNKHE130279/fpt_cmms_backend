package com.cmms.demo.exception;


import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionTranslator {
/*
    @Autowired
    @Qualifier("messconfig")
    MessageSource messageSource;

    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> onBindException(BindException e, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(
                new MyLanguage(messageSource.getMessage("errors_messager", null, locale)));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> onMethodArgumentNotValidException(MethodArgumentNotValidException e, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), messageSource.getMessage(error.getDefaultMessage(), null, locale));
        }


        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> onConstraintViolationException(ConstraintViolationException e, Locale locale) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return ResponseEntity.badRequest().body(
                new MyLanguage(messageSource.getMessage("errors_mess1", null, locale)));
    }*/
}
