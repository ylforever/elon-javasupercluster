package com.elon.jsc.model;

import java.io.Serializable;

/**
 * KD树单点对象模型定义。应用层的单个GIS点在计算前转换为该模型。
 *
 * @author elon
 */
public class JKDNode implements Serializable {

    private static final long serialVersionUID = -7134737233652288121L;

    /**
     * 对象索引ID(算法对数量重新编号后的id)
     */
    private int id = -1;

    /**
     * 父节点ID
     */
    private int parentId = 1;

    /**
     * 地图放大级别
     */
    private int zoom = Integer.MAX_VALUE;

    /**
     * 聚合点的数量
     */
    private int numPoints = 0;

    /**
     * 对象属性
     */
    private Object properties = null;

    /**
     * 对象原始ID，记录应用层汇聚模型的ID值
     */
    private int orignalId = -1;

    /**
     * X坐标
     */
    private double x = 0;

    /**
     * Y坐标
     */
    private double y = 0;

    private int index = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numPoints) {
        this.numPoints = numPoints;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public int getOrignalId() {
        return orignalId;
    }

    public void setOrignalId(int orignalId) {
        this.orignalId = orignalId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
