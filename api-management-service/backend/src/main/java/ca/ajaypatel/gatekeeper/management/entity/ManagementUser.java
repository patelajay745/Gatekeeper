package ca.ajaypatel.gatekeeper.management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(	name="management_users")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagementUser extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id")
	Organisation organisation;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id",nullable = false)
	Role role;
	
	String username;
	
	String passwordHash;
	
	boolean enabled;
	
	
}
