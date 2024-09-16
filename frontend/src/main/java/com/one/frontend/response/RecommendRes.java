package com.one.frontend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecommendRes {

    private List<String> imageUrls;

    private String productName;
}
