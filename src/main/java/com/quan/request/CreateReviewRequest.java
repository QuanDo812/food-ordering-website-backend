package com.quan.request;

import com.quan.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequest {

    private String comment;

    private Long foodId;
    private Long rating;

}
