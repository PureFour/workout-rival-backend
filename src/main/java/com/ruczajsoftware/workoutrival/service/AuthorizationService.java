package com.ruczajsoftware.workoutrival.service;

import com.ruczajsoftware.workoutrival.model.authentication.AuthenticationRequest;
import com.ruczajsoftware.workoutrival.model.authentication.AuthenticationResponse;
import com.ruczajsoftware.workoutrival.model.authentication.AuthenticationResponseBuilder;
import com.ruczajsoftware.workoutrival.model.database.User;
import com.ruczajsoftware.workoutrival.model.exceptions.EntityNotFoundException;
import com.ruczajsoftware.workoutrival.model.exceptions.ExceptionMessages;
import com.ruczajsoftware.workoutrival.model.exceptions.UnauthorizedException;
import com.ruczajsoftware.workoutrival.service.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthorizationService {

	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final UserService userService;

	public AuthenticationResponse authenticateUser(AuthenticationRequest authRequest) throws UnauthorizedException, EntityNotFoundException {

		final User user;
		try {
			//TODO rename field email in AuthenticationRequest class
			user = userService.getUserByUsernameOrEmail(authRequest.getEmail());
			final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					user.getUsername(), authRequest.getPassword());
			authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new UnauthorizedException(ExceptionMessages.INCORRECT_CREDENTIALS);
		} catch (Exception exc) {
			throw new EntityNotFoundException(ExceptionMessages.USER_NOT_FOUND);
		}

		final String jwtToken = "Bearer " + jwtUtil.generateToken(user);
		return new AuthenticationResponseBuilder().token(jwtToken).build();
	}
}