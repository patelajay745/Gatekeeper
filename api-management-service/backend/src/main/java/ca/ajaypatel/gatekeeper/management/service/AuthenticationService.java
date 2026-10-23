package ca.ajaypatel.gatekeeper.management.service;

import ca.ajaypatel.gatekeeper.management.dto.LoginRequest;
import ca.ajaypatel.gatekeeper.management.dto.LoginResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;


public interface AuthenticationService {
	
	LoginResponse login(LoginRequest request);
}
