package com.github.ricardobaumann.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@Entity
@Table(name="access_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Wither
public class AccessToken {

    @Id
    @Column(name="id")
    @JsonProperty("access_token_id")
    @NotNull
    private String accessTokenId;

    @Column(name="user_id")
    @JsonProperty("user_id")
    @NotNull
    private Long userId;

    @JsonProperty("last_access_utc")
    @NotNull
    @Column(name="last_access")
    private DateTime lastAccessUTC;

}
