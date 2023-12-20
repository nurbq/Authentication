package org.example.userregistr.dao.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserEntity {

    private Long id;
    private String email;
    private String password;
    private LocalDateTime createdDate;
}
