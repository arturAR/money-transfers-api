package server.validation;

import model.dto.AccountRequest;
import model.dto.TransferRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {

    @Test
    void testValidateTransferRequest_WhenCorrectTransferRequest_NothingIsThrown() {
        TransferRequest trReq = new TransferRequest();
        trReq.amount = BigDecimal.valueOf(400);
        trReq.fromAcc = 2;
        trReq.toAcc = 3;

        RequestValidator.validateTransferRequest(trReq);
    }

    @Test
    void testValidateTransferRequest_WhenNullTransferRequest_ThrowIllegalStateEx() {
        assertThrows(IllegalStateException.class, () -> RequestValidator.validateTransferRequest(null));
    }

    @Test
    void testValidateTransferRequest_WhenNegativeAmount_ThrowValidationEx() {
        TransferRequest trReq = new TransferRequest();
        trReq.amount = BigDecimal.valueOf(-400);
        trReq.fromAcc = 2;
        trReq.toAcc = 3;

        assertThrows(ValidationException.class, () -> RequestValidator.validateTransferRequest(trReq));
    }

    @Test
    void testValidateTransferRequest_WhenEqualAccIds_ThrowValidationEx() {
        TransferRequest trReq = new TransferRequest();
        trReq.amount = BigDecimal.valueOf(400);
        trReq.fromAcc = 2;
        trReq.toAcc = 2;

        assertThrows(ValidationException.class, () -> RequestValidator.validateTransferRequest(trReq));
    }

    @Test
    void testValidateTransferRequest_WhenCorrectAccountRequest_NothingIsThrown() {
        AccountRequest ac = new AccountRequest();
        ac.number = "acc";
        ac.balance = BigDecimal.valueOf(100);

        RequestValidator.validateAccountRequest(ac);
    }

    @Test
    void testValidateTransferRequest_WhenNullAccountRequest_ThrowIllegalStateEx() {
        assertThrows(IllegalStateException.class, () -> RequestValidator.validateAccountRequest(null));
    }

    @Test
    void validateAccountRequest_WhenNullAccNumber_ThrowValidationEx() {
        AccountRequest ac = new AccountRequest();
        ac.balance = BigDecimal.valueOf(100);

        assertThrows(ValidationException.class, () -> RequestValidator.validateAccountRequest(ac));
    }

    @Test
    void validateAccountRequest_WhenEmptyAccNumber_ThrowValidationEx() {
        AccountRequest ac = new AccountRequest();
        ac.number = "";
        ac.balance = BigDecimal.valueOf(100);

        assertThrows(ValidationException.class, () -> RequestValidator.validateAccountRequest(ac));
    }

    @Test
    void validateAccountRequest_WhenNegativeBalance_ThrowValidationEx() {
        AccountRequest ac = new AccountRequest();
        ac.number = "1115";
        ac.balance = BigDecimal.valueOf(-90);

        assertThrows(ValidationException.class, () -> RequestValidator.validateAccountRequest(ac));
    }

    @Test
    void validateId_WhenPositiveId_NothingIsThrown() {
        RequestValidator.validateId(100);
    }

    @Test
    void validateId_WhenNegativeId_ThrowValidationEx() {
        assertThrows(ValidationException.class, () -> RequestValidator.validateId(-100));
    }

    @Test
    void validateId_WhenZeroId_ThrowValidationEx() {
        assertThrows(ValidationException.class, () -> RequestValidator.validateId(0));
    }

    @Test
    void validateId_WhenNumberAsString_NothingIsThrown() {
        RequestValidator.validateNumber("890");
    }

    @Test
    void validateNumber_WhenUnparsableNumber_ThrowValidationEx() {
        assertThrows(ValidationException.class, () -> RequestValidator.validateNumber("123xyz"));
    }
}