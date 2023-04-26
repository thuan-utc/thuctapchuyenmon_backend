//package cntt2.k61.backend.controller;
//
//import cntt2.k61.backend.exception.BusinessException;
//import cntt2.k61.backend.exception.ErrorJson;
//import cntt2.k61.backend.exception.SomethingNotFoundException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//
//import javax.servlet.http.HttpServletRequest;
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//@ControllerAdvice
//@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
//public class ErrorController {
//    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorJson> handleConflict(Exception ex, HttpServletRequest request) {
//
//        HttpStatus httpStatus;
//
//        if (ex instanceof BusinessException) {
//
//            // logging exception message
//            log.warn("Problem processing request: " + request.getRequestURI());
//            log.warn("Problem description: " + ex.getMessage());
//
//            // 4xx
//            if (ex instanceof SomethingNotFoundException) {
//                httpStatus = HttpStatus.NOT_FOUND; // 404
//            } else {
//                httpStatus = HttpStatus.BAD_REQUEST; // 400
//            }
//            ErrorJson error = new ErrorJson(((BusinessException) ex).getErrorCode(), ex.getMessage(), request.getRequestURI());
//            return new ResponseEntity<>(error, httpStatus);
//
//        } else if (ex instanceof MethodArgumentNotValidException) {
//            List<String> errors = new ArrayList<>();
//            ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors().forEach((error) -> {
//                String fieldName = ((FieldError) error).getField();
//                String errorMessage = error.getDefaultMessage();
//                errors.add(String.format("[field: %s - erorMsg: %s]", fieldName, errorMessage));
//            });
//            ErrorJson error = new ErrorJson("N/A", String.join(",", errors), request.getRequestURI());
//            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//        } else if (ex instanceof BindException) {
//            List<String> errors = new ArrayList<>();
//            ((BindException) ex).getBindingResult().getAllErrors().forEach((error) -> {
//                String fieldName = ((FieldError) error).getField();
//                String errorMessage = error.getDefaultMessage();
//                errors.add(String.format("[field: %s - erorMsg: %s]", fieldName, errorMessage));
//            });
//            ErrorJson error = new ErrorJson("N/A", String.join(",", errors), request.getRequestURI());
//            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//        } else if (ex.getCause() != null && ex.getCause().getCause() instanceof ConstraintViolationException) {
//            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) ex.getCause().getCause()).getConstraintViolations();
//            List<String> errors = new ArrayList<>();
//            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
//                errors.add(String.format("[field: %s - erorMsg: %s]", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));
//            }
//            ErrorJson error = new ErrorJson("N/A", String.join(",", errors), request.getRequestURI());
//            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//        } else { // 500 response code
//
//            // logging exception message and a stacktrace
//            log.warn("Problem processing request: " + request.getRequestURI(), ex);
//
//            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//
//            ErrorJson error = new ErrorJson("N/A", ex.getClass().getName() + ":" + ex.getMessage(), request.getRequestURI());
//            return new ResponseEntity<>(error, httpStatus);
//        }
//    }
//}
