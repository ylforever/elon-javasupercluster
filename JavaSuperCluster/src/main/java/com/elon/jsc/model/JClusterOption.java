package com.elon.jsc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 汇聚算法参数模型定义
 */
@Data
public class JClusterOption implements Serializable {

    private static final long serialVersionUID = -7916591464862026234L;

    /**
     * 生成聚合数据的最小地图放大层级
     */
    private int minZoom = 0;

    /**
     * 生成数据的最大地图放大层级
     */
    private int maxZoom = 16;

    /**
     * 聚合半径(单位：像素)
     */
    private int radius = 40;

    /**
     * 瓦片大小
     */
    private int extent = 512;

    /**
     * KD-Tree的叶子节点数量
     */
    private int nodeSize = 64;

    private Object reduce = null;

    private Object initial = null;

    private Object map = null;
}
