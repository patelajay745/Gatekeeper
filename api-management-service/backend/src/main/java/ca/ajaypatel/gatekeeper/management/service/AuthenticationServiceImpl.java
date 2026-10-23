package ca.ajaypatel.gatekeeper.management.service;

import ca.ajaypatel.gatekeeper.management.dto.LoginRequest;
import ca.ajaypatel.gatekeeper.management.dto.LoginResponse;
import ca.ajaypatel.gatekeeper.management.entity.ManagementUser;
import ca.ajaypatel.gatekeeper.management.entity.OrganizationStatus;
import ca.ajaypatel.gatekeeper.management.exception.ApiException;
import ca.ajaypatel.gatekeeper.management.repository.ManagementUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
	
	private final ManagementUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtEncoder jwtencoder;
	
	@Value("${security.jwt.expiration-seconds}")
	private long expirationSeconds;
	
	@Override
	@Transactional(readOnly = true)
	public LoginResponse login(LoginRequest request) {
		
		ManagementUser user= userRepository.findByUsername(request.username()).orElseThrow(() -> invalidCredentials());
		
		
		if(!user.isEnabled() || !passwordEncoder.matches(request.password(),user.getPasswordHash())){
			throw invalidCredentials();
		}
		
		if(user.getOrganisation()!= null && user.getOrganisation().getStatus()!= OrganizationStatus.ACTIVE){
			throw new ApiException(HttpStatus.FORBIDDEN,"The organization is not active");
		}
		
		
		Instant issuedAt= Instant.now();
		
		String role=user.getRole().getName();
		Long organizationId=user.getOrganisation() == null ?  null : user.getOrganisation().getId();
		
		JwtClaimsSet.Builder claims=JwtClaimsSet.builder()
				                            .issuer("gatekeeper-api-management")
				                            .issuedAt(issuedAt)
				                            .expiresAt(issuedAt.plusSeconds(expirationSeconds))
				                            .subject(user.getUsername())
				                            .claim("role",role);
		
		if(organizationId!=null){
			claims.claim("organizationId",organizationId);
		}
		
		JwsHeader header= JwsHeader.with(MacAlgorithm.HS256).build();
		
		String token=jwtencoder.encode(
				JwtEncoderParameters.from(header,claims.build())
		).getTokenValue();
		
		
		
		return new LoginResponse(token,"Bearer",expirationSeconds,role,organizationId);
	}
	
	private RuntimeException invalidCredentials(){
		return new ApiException(HttpStatus.UNAUTHORIZED,"Invalid username or password");
	}
}
