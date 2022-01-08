package donnager.RedditClone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class AuthenticationResponse {

    private String authenticationToken;
    private String username;
}
