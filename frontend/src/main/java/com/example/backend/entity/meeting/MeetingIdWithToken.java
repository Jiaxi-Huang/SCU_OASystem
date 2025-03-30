package com.example.backend.entity.meeting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MeetingIdWithToken {
    private int mtin_id;
    private String accessToken;

    public MeetingIdWithToken(int mtin_id, String accessToken) {
        this.mtin_id = mtin_id;
        this.accessToken = accessToken;
    }
}
