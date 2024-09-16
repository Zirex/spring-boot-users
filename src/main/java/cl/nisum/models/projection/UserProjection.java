package cl.nisum.models.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserProjection {
    UUID getId();
    LocalDateTime getCreated();
    LocalDateTime getModified();
    LocalDateTime getLastLogin();
    String getToken();
    boolean isActive();
}
