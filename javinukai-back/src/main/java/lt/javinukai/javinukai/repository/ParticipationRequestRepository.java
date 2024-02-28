package lt.javinukai.javinukai.repository;


import lt.javinukai.javinukai.entity.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, UUID> {
}
