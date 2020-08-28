package com.tingxuelou.www.provider.bean.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Group {
    private Integer id;

    private String groupId;

    private String groupName;

    private String groupDesc;

    private String isDel;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}