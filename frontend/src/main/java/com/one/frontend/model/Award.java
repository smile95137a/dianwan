package com.one.frontend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Award {

    private BigDecimal cumulative; // 累计消费金额
    private List<BigDecimal> thresholdList; // 每个达标金额
    private List<Integer> tokenList; // 对应的银币数量
    private List<Integer> rewardsAchieved; // 当前可以领取的所有银币奖励
}
