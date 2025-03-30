package com.example.backend.entity.authedRoutes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permission {
    private int user_id;
    private String permissions;
}
