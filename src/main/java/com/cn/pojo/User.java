package com.cn.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: helisen
 * @create: 2021-01-23 11:09
 **/

/**
 * 生成无参构造方法/getter/setter/hashCode/equals/toString
 */
@Data
/**
 *生成所有参数构造方法
 */
@AllArgsConstructor
/**
 *@AllArgsConstructor 会导致@Data不生成无参数构造方法，需要手动添加@NoArgsConstructor，
 * 如果没有无参构造方法，可能会导致比如com.fasterxml.jackson在序列化处理时报错
 */
@NoArgsConstructor
public class User {
    private String name;
    private Integer age;
}
