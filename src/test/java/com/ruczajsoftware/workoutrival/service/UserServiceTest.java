package com.ruczajsoftware.workoutrival.service;

import com.ruczajsoftware.workoutrival.model.database.PersonalData;
import com.ruczajsoftware.workoutrival.model.database.PersonalDataBuilder;
import com.ruczajsoftware.workoutrival.model.database.Sex;
import com.ruczajsoftware.workoutrival.model.exceptions.BadRequestException;
import com.ruczajsoftware.workoutrival.repositories.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private EmailService emailService;
    @MockBean
    private PinService pinService;

    @Autowired
    private UserService userService;

    @Test
    void calculateBMIByPersonalDataShouldReturnProperBMITest() throws BadRequestException {
        PersonalData personalData = getPersonalDataForBMI(181, 72.5f);
        Assert.assertEquals(22.1f, userService.calculateBMIByPersonalData(personalData), 0.1);
    }

    @Test
    void calculateBMIByPersonalDataShouldThrowIllegalArgumentException() {
        PersonalData personalDataEmptyHeight = getPersonalDataForBMI(0, 72.5f);
        PersonalData personalDataEmptyWeight = getPersonalDataForBMI(185, 0f);
        assertThrows(IllegalArgumentException.class, () -> userService.calculateBMIByPersonalData(personalDataEmptyHeight));
        assertThrows(IllegalArgumentException.class, () -> userService.calculateBMIByPersonalData(personalDataEmptyWeight));
    }

    private PersonalData getPersonalDataForBMI(int height, float weight) {
        return new PersonalDataBuilder()
                .height(height)
                .weight(weight)
                .birthday(new Date())
                .sex(Sex.MALE)
                .build();
    }


}