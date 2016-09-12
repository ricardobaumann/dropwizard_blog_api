package com.github.ricardobaumann.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private String title;
    
    private String content;
    
    private Long id;
    
}
