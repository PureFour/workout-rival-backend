package com.ruczajsoftware.workoutrival.endpoints;

import com.ruczajsoftware.workoutrival.model.database.PersonalData;
import com.ruczajsoftware.workoutrival.model.database.User;
import com.ruczajsoftware.workoutrival.model.database.UserBuilder;
import com.ruczajsoftware.workoutrival.service.AuthorizationService;
import com.ruczajsoftware.workoutrival.service.UserService;
import com.ruczajsoftware.workoutrival.service.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false) // disabling security
public class UserControllerTest {
    private static final String BASE_URL = "/workoutRival/users";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorizationService authorizationService;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    public void getUserShouldReturnUser()
            throws Exception {

        final User user = new UserBuilder()
                .email("test@test.com")
                .username("test")
                .password("test")
                .bodyMeasurements(List.of())
                .personalData(new PersonalData())
                .trainingPlans(List.of())
                .definedTrainings(List.of())
                .build();

        given(userService.loadUserByUsername("test")).willReturn(any());
        given(userService.getUserByUsername("test")).willReturn(user);

        mvc.perform(get(BASE_URL)
                .param("username", user.getUsername())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(user.getEmail()));

    }
}