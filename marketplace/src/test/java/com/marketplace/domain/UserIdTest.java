package com.marketplace.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.context.ObjectMapperModule;
import com.marketplace.domain.shared.UserId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserIdTest {
    ObjectMapper objectMapper = ObjectMapperModule.provideObjectMapper();

    // https://gist.github.com/youribonnaffe/03176be516c0ed06828ccc7d6c1724ce
    //    static record User(UserId userId, String username) {
//    }
    static class User {
        UserId userId;
        String username;

        public User() {
        }

        public User(UserId userId, String username) {
            this.username = username;
            this.userId = userId;
        }

        public UserId getUserId() {
            return userId;
        }

        public void setUserId(UserId userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

    @Test
    void testJsonUnwrap() throws JsonProcessingException {
        var userId = new UserId(UUID.randomUUID());
        User user = new User(userId, "richard");
        String userIdJson = objectMapper.writeValueAsString(user);
        System.out.println(userIdJson);
        assertThat(userIdJson).isNotNull();

        var res = objectMapper.readValue(userIdJson, User.class);
        assertThat(res).isNotNull();
        assertThat(res.username).isEqualTo(user.username);
        assertThat(res.userId).isEqualTo(user.userId);
    }


    @Test
    void userIdFromUUIDIsValid() {
        var userId = UserId.newUserId();
        assertThat(userId.isValid()).isTrue();
    }

    @Test
    void emptyUserIdIsNotValid() {
        var userId = UserId.EMPTY_VALUE;
        assertThat(userId.isValid()).isFalse();
    }
}
