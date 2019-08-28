package com.ruczajsoftware.workoutrival.service;

import com.ruczajsoftware.workoutrival.model.exceptions.BadRequestException;
import com.ruczajsoftware.workoutrival.model.exceptions.ExceptionMessages;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PinService {
    private static final Map<String, String> USER_PINS = new HashMap<>();

    public String createAndSavePin(String userEmail) {
        removeOldPins(userEmail);
        final String pin = generatePasswordResetPin();
        USER_PINS.put(userEmail, pin);
        return pin;
    }

    public void checkPasswordResetPin(String userEmail, String pin) throws BadRequestException {
        if (!USER_PINS.containsKey(userEmail)) {
            throw new BadRequestException(ExceptionMessages.PIN_NOT_FOUND);
        }
        final String userPasswordResetPin = USER_PINS.get(userEmail);
        if (!StringUtils.equals(pin, userPasswordResetPin)) {
            throw new BadRequestException(ExceptionMessages.PIN_INCORRECT);
        }
        USER_PINS.remove(userEmail);
    }

    public String generatePasswordResetPin() {
        return RandomStringUtils.randomNumeric(4);
    }

    private void removeOldPins(String userEmail) {
        if (USER_PINS.containsKey(userEmail)) {
            USER_PINS.remove(userEmail);
        }
    }
}
