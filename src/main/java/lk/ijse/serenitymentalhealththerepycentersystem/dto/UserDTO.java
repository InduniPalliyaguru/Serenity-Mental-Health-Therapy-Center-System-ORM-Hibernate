package lk.ijse.serenitymentalhealththerepycentersystem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    private String user_id;
    private String username;
    private String password;
    private String email;
    private String role;

}
