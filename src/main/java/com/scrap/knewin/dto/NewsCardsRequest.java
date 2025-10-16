package com.scrap.knewin.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsCardsRequest {

    private long post_id;
    private List<Integer> categories;
    private List<Integer> tags;
}