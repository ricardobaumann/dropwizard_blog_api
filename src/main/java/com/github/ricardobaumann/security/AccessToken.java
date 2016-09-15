package com.github.ricardobaumann.security;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Wither
public class AccessToken {

    @JsonProperty("access_token_id")
    @NotNull
    private UUID accessTokenId;

    @JsonProperty("user_id")
    @NotNull
    private Long userId;

    @JsonProperty("last_access_utc")
    @NotNull
    private DateTime lastAccessUTC;

}
