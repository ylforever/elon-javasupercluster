package com.elon.jsc.model;

import java.io.Serializable;

/**
 * 汇聚算法参数模型定义
 */
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

    public int getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getExtent() {
        return extent;
    }

    public void setExtent(int extent) {
        this.extent = extent;
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
    }

    public Object getReduce() {
        return reduce;
    }

    public void setReduce(Object reduce) {
        this.reduce = reduce;
    }

    public Object getInitial() {
        return initial;
    }

    public void setInitial(Object initial) {
        this.initial = initial;
    }

    public Object getMap() {
        return map;
    }

    public void setMap(Object map) {
        this.map = map;
    }
}
