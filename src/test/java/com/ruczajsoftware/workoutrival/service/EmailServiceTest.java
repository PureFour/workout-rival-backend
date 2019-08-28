package com.ruczajsoftware.workoutrival.service;

import com.ruczajsoftware.workoutrival.service.util.Bundle;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;

@RunWith(SpringRunner.class)
public class EmailServiceTest {

    @MockBean
    EmailService emailService;

    @Test
    void resetTemplateTest() {
        final String message = Bundle.getEmailTemplate(Locale.ENGLISH, "reset_password", "1234");
        Assert.assertFalse(message.isEmpty());
    }
}