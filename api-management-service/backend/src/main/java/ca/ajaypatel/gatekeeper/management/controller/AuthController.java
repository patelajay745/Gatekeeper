package ca.ajaypatel.gatekeeper.management.controller;

import ca.ajaypatel.gatekeeper.management.dto.LoginRequest;
import ca.ajaypatel.gatekeeper.management.dto.LoginResponse;
import ca.ajaypatel.gatekeeper.management.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthenticationService authenticationService;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
		return ResponseEntity.ok(authenticationService.login(request));
	}

}
