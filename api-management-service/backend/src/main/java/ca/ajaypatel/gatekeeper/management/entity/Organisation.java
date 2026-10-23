package ca.ajaypatel.gatekeeper.management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(	name="organizations")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Organisation extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String name;
	String slug;
	
	@Enumerated(EnumType.STRING)
	OrganizationStatus status;
	
	@Column(name = "deleted_at")
	Instant deletedAt;
	
	
}
