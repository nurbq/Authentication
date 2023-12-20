package org.example.userregistr.dao.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Role {

    private Long id;
    private String roleName;
    private Long userId;
}
