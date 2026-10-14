package trade_plumber.error

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(AlpacaApiFailedException::class)
    fun alpacaFailed(ex: AlpacaApiFailedException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, ex.message)

    @ExceptionHandler(UpdateInProgressException::class)
    fun updateInProgress(ex: UpdateInProgressException): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.message)

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun unreadableBody(): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, UNREADABLE_BODY_DETAIL)

    @ExceptionHandler(Exception::class)
    fun unexpected(): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, UNEXPECTED_ERROR_DETAIL)

    private companion object {
        const val UNREADABLE_BODY_DETAIL = "Request body is missing or malformed"
        const val UNEXPECTED_ERROR_DETAIL = "Unexpected error"
    }
}
