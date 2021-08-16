package com.cmms.demo.endpoint;

import com.cmms.demo.config.JwtTokenUtil;
import com.cmms.demo.domain.MyMessage;
import com.cmms.demo.domain.UserPOJO;
import com.cmms.demo.dto.UserDTO;
import com.cmms.demo.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@EnableAutoConfiguration
@RestController
@CrossOrigin("*")
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserServiceImpl userDetailsService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDTO authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getAccount(), authenticationRequest.getPassword());

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getAccount());

		final String token = jwtTokenUtil.generateToken(userDetails);
		UserPOJO u = userDetailsService.saveToken(token,userDetailsService.getUserByName(authenticationRequest.getAccount()));

		return ResponseEntity.ok(new UserDTO().convertToUserDTO(u,userDetailsService.getAllRByUserName(u.getAccount())));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
	@RequestMapping(value = "/logout",method = RequestMethod.POST)
	public ResponseEntity<MyMessage> logOut(@RequestBody UserDTO user) throws Exception {
		userDetailsService.logOut();
		return ResponseEntity.ok(new MyMessage("logOut"));
	}

	@GetMapping(
			value = "/get-image",
			produces = MediaType.IMAGE_JPEG_VALUE
	)
	public ResponseEntity<byte[]> getImageWithMediaType(@RequestParam String imgName) throws IOException {
		File file = new File("./images/"+ imgName);
		byte[] fileContent = Files.readAllBytes(file.toPath());
		return new ResponseEntity<>(fileContent, HttpStatus.OK);
	}
}
