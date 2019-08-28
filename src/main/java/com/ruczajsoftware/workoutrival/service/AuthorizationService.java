package com.ruczajsoftware.workoutrival.service;
import com.ruczajsoftware.workoutrival.model.authentication.AuthenticationRequest;
import com.ruczajsoftware.workoutrival.model.authentication.AuthenticationResponse;
import com.ruczajsoftware.workoutrival.model.authentication.AuthenticationResponseBuilder;
import com.ruczajsoftware.workoutrival.model.exceptions.EntityNotFoundException;
import com.ruczajsoftware.workoutrival.model.exceptions.ExceptionMessages;
import com.ruczajsoftware.workoutrival.model.exceptions.UnauthorizedException;
import com.ruczajsoftware.workoutrival.model.database.User;
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

		final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				authRequest.getEmail(), authRequest.getPassword());

		try {
			authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			throw new UnauthorizedException(ExceptionMessages.INCORRECT_CREDENTIALS);
		}

		final User user = userService.getUserByEmail(authRequest.getEmail());

		final String jwtToken = "Bearer " + jwtUtil.generateToken(user);

		return new AuthenticationResponseBuilder().token(jwtToken).build();
	}
}