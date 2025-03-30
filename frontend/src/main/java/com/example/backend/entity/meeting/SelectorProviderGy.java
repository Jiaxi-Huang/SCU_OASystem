package com.example.backend.entity.meeting;

//Is a injection possible here? Let me know if there is a better solution! --GY
public class SelectorProviderGy {
    public String buildMeetingSearchQuery(String field, String key, int user_id) {
        return "SELECT mtin_id, adder_id, mtin_title, mtin_ctnt, " +
                "mtin_fin, mtin_st, mtin_len, mtin_host, mtin_loc, mtin_crt " +
                "FROM meetings natural join usermeetings " +
                "WHERE user_id = " +  user_id + " AND " +
                field + " LIKE CONCAT('%', '" + key + "' ,'%')";
    }

    public String buildTodoSearchQuery(String field, String key, int user_id) {
        return "SELECT * FROM usertodo WHERE user_id = "
                + user_id + " AND " + field + " LIKE CONCAT('%', '" + key + "' ,'%')";
    }

}
