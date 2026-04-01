package com.friendbook.service;

import com.friendbook.entity.User;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    public static final String SESSION_USER_ID = "friendbookUserId";

    public void login(HttpSession session, User user) {
        session.setAttribute(SESSION_USER_ID, user.getId());
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public Optional<Long> getCurrentUserId(HttpSession session) {
        Object value = session.getAttribute(SESSION_USER_ID);
        if (value instanceof Long id) {
            return Optional.of(id);
        }
        return Optional.empty();
    }
}
