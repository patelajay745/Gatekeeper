package ca.ajaypatel.gatekeeper.management.dto;

public record LoginResponse(

		String token,
		String tokenType,
		long expiresInSeconds,
		String role,
		Long organizationId
		
) {

}
