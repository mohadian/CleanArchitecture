package com.elvotra.clean.data.remote.model.mapper;

import com.elvotra.clean.data.remote.model.UserData;
import com.elvotra.clean.domain.model.User;

import java.util.ArrayList;
import java.util.List;

public class UsersResponseMapper {

    private static UsersResponseMapper INSTANCE;

    public static UsersResponseMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersResponseMapper();
        }
        return INSTANCE;
    }

    private UsersResponseMapper() {

    }

    public List<User> transform(List<UserData> response) {
        List<User> result = new ArrayList<>(response.size());
        for (int i = 0; i < response.size(); i++) {
            UserData userData = response.get(i);
            User user = new User(userData.getId(), userData.getName(), userData.getUsername(), userData.getEmail());
            result.add(user);
        }
        return result;
    }
}
