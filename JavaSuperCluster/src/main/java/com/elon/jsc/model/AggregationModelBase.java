package com.elon.jsc.model;

import java.io.Serializable;

/**
 * 汇聚模型基础类定义。所有需要调用汇聚算法的模型从该类派生。
 *
 * @author elon
 * @version 1.0 2019-03-15
 */
public class AggregationModelBase implements Serializable {

    private static final long serialVersionUID = 4951392595928922035L;

    /**
     * 对象唯一ID(应用层定义，可以是数据库中对象的自增ID或者其它唯一标识)。
     */
    private int id = -1;

    /**
     * 经度
     */
    private double longitude = 0;

    /**
     * 维度
     */
    private double latitude = 0;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
