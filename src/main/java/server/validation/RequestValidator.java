package server.validation;

import model.dto.AccountRequest;
import model.dto.TransferRequest;

import java.math.BigDecimal;

public class RequestValidator {

    public static void validateTransferRequest(TransferRequest trReq) {
        if (null == trReq) {
            throw new IllegalStateException("Transfer request object can not be null");
        }
        if (trReq.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Transfer amount: " + trReq.amount + " must be positive");
        }
        if (trReq.fromAcc == trReq.toAcc) {
            throw new ValidationException("Sender's account id: " + trReq.fromAcc + " can not be equal to " +
                    "recipient's account id: " + trReq.toAcc);
        }
    }

    public static void validateAccountRequest(AccountRequest acc) {
        if (null == acc) {
            throw new IllegalStateException("Account request object can not be null");
        }
        if (acc.number == null || acc.number.length() <= 0) {
            throw new ValidationException("Account number can not be empty");
        }
        if (acc.balance == null || acc.balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Account balance can not be negative");
        }
    }

    public static void validateId(long id) {
        if (id <= 0) {
            throw new ValidationException("Reference id: " + id + " must be positive");
        }
    }

    public static void validateNumber(String number) {
        try {
            Long.parseLong(number);
        } catch (NumberFormatException e) {
            throw new ValidationException("String: " + number + " is not a number");
        }
    }
}
