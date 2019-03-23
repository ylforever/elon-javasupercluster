package com.elon.jsc.kdbush;

import com.elon.jsc.model.JKDNode;

import java.util.Arrays;
import java.util.List;

/**
 * K-D树模型定义，用于GIS点汇聚时空间搜索。根据开源软件KDBush的JS代码翻译而来。
 *
 * @author elon
 * @version 1.0 2019-03-15
 */
public class JKDBush {

    /**
     * KD树节点数量
     */
    private int nodeSize = 64;

    /**
     * 节点列表
     */
    private List<JKDNode> points = null;

    /**
     * 节点ID列表(从0开始新分配的ID)
     */
    private List<Integer> ids = null;

    /**
     * 节点坐标列表(同一个点的X和Y存储在相邻的位置)
     */
    private List<Double> coords = null;

    /**
     * 构造函数。根据传入的KDNode模型初始化数据。
     *
     * @param points KDNode对象模型
     */
    public JKDBush(List<JKDNode> points){
        this.points = points;

        // 分配ID和坐标的存储空间(坐标存储X和Y, 需要两倍的空间)
        ids = Arrays.asList(new Integer[points.size()]);
        coords = Arrays.asList(new Double[points.size() * 2]);

        // 初始化数据
        for (int i = 0; i < points.size(); ++i){
            ids.set(i, i);

            // 偶数位存储X坐标, 奇数位存储Y坐标
            coords.set(2 * i, points.get(i).getX());
            coords.set(2 * i + 1, points.get(i).getY());
        }

        // 排序以支持快速搜索
        new JKDSort(nodeSize, ids, coords).sort(0, ids.size() - 1, 0);
    }

    public List<Integer> range(double minX, double minY, double maxX, double maxY) {
        return new JKDRange(nodeSize, ids, coords).range(minX, minY, maxX, maxY);
    }

    public List<Integer> within(double x, double y, double r) {
        return new JKDWithin(nodeSize, ids, coords).within(x, y, r);
    }

    public int getNodeSize() {
        return nodeSize;
    }

    public void setNodeSize(int nodeSize) {
        this.nodeSize = nodeSize;
    }

    public List<JKDNode> getPoints() {
        return points;
    }

    public void setPoints(List<JKDNode> points) {
        this.points = points;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Double> getCoords() {
        return coords;
    }

    public void setCoords(List<Double> coords) {
        this.coords = coords;
    }
}
