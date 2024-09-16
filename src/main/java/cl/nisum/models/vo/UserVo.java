package cl.nisum.models.vo;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import cl.nisum.models.projection.UserProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@ToString
public class UserVo implements UserProjection {

    private UUID id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modified;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;
    private String token;
    private boolean active;

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public LocalDateTime getCreated() {
        return this.created;
    }

    @Override
    public LocalDateTime getModified() {
        return this.modified;
    }

    @Override
    public LocalDateTime getLastLogin() {
        return this.lastLogin;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

}