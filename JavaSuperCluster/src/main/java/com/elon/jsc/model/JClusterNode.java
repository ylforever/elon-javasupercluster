package com.elon.jsc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 聚合节点模型定义。
 *
 * @author elon
 * @version 1.0 2019-03-15
 */
@Data
public class JClusterNode <T extends AggregationModelBase> implements Serializable {

    private static final long serialVersionUID = -5358622773451333438L;
    /**
     * 是否聚合对象
     */
    private boolean isCluster = false;

    /**
     * 聚合点的ID
     */
    private int clusterId = -1;

    /**
     * 聚合点数量
     */
    private int pointCount = 0;

    /**
     * 聚合点的X坐标
     */
    private double x = 0;

    /**
     * Y坐标
     */
    private double y = 0;

    /**
     * 聚合点为单点时存储应用层的对象模型。
     */
    private T data = null;
}
