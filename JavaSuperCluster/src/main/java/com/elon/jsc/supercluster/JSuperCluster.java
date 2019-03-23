package com.elon.jsc.supercluster;

import com.elon.jsc.kdbush.JKDBush;
import com.elon.jsc.model.AggregationModelBase;
import com.elon.jsc.model.JClusterNode;
import com.elon.jsc.model.JClusterOption;
import com.elon.jsc.model.JKDNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SuperCluster汇聚类。
 */
public class JSuperCluster<T extends AggregationModelBase> {
    /**
     * 汇聚算法参数
     */
    private JClusterOption option = null;

    /**
     * K-D树
     */
    private List<JKDBush> trees = null;

    /**
     * 参数汇聚的点数据列表
     */
    private List<T> points = null;

    public JSuperCluster(JClusterOption option) {
        this.option = option;
        this.trees = Arrays.asList(new JKDBush[option.getMaxZoom() + 2]);
    }

    /**
     * 加载数据，生成KD树。
     * @param points
     */
    public void load(List<T> points) {
        this.points = points;

        // 构建聚合点数据
        List<JKDNode> clusters = new ArrayList<>();
        for (int i = 0; i < this.points.size(); i++) {
            if (points.get(i) == null) {
                continue;
            }

            clusters.add(createPointCluster(points.get(i), i));
        }
        trees.set(option.getMaxZoom() + 1, new JKDBush(clusters));

        // 逐层构建每一级的汇聚节点
        for (int z = option.getMaxZoom(); z >= option.getMinZoom(); z--) {
            List<JKDNode> clustersList = buildCluster(clusters, z);

            trees.set(z, new JKDBush(clustersList));
            clusters = clustersList;
        }
    }

    private JKDNode createPointCluster(T p, int id) {
        JKDNode node = new JKDNode();
        node.setId(id);

        node.setX(lngX(p.getLongitude()));
        node.setY(latY(p.getLatitude()));
        node.setParentId(-1);
        node.setZoom(Integer.MAX_VALUE);
        node.setOrignalId(p.getId());
        node.setIndex(id);

        return node;
    }

    /**
     * 获取聚簇对象。
     * @param bbox
     * @param zoom
     * @return
     */
    public List<JClusterNode<T>> getClusters(List<Double> bbox, int zoom) {
        double minLng = ((bbox.get(0) + 180) % 360 + 360) % 360 - 180;
        double minLat = Math.max(-90, Math.min(90, bbox.get(1)));
        double maxLng = bbox.get(2) == 180 ? 180 : ((bbox.get(2) + 180) % 360 + 360) % 360 -180;
        double maxLat = Math.max(-90, Math.min(90, bbox.get(3)));

        if (bbox.get(2) - bbox.get(0) >= 360) {
            minLng = -180;
            maxLng = 180;
        } else if (minLng > maxLng) {
            List<Double> easternBBox = new ArrayList<>();
            easternBBox.add(minLng);
            easternBBox.add(minLat);
            easternBBox.add(180.0);
            easternBBox.add(maxLat);
            List<JClusterNode<T>> easternHem = getClusters(easternBBox, zoom);

            List<Double> westernBBox = new ArrayList<>();
            westernBBox.add(-180.0);
            westernBBox.add(minLat);
            westernBBox.add(maxLng);
            westernBBox.add(maxLat);
            List<JClusterNode<T>> westernHem = getClusters(westernBBox, zoom);

            easternHem.addAll(westernHem);

            return easternHem;
        }

        JKDBush tree = trees.get(limitZoom(zoom));
        List<Integer> ids = tree.range(lngX(minLng), latY(maxLat), lngX(maxLng), latY(minLat));
        List<JClusterNode<T>> clusters = new ArrayList<>();

        for (int id : ids) {
            JKDNode c = tree.getPoints().get(id);
            if (c.getNumPoints() > 0) {
                JClusterNode<T> cn = new JClusterNode<>();
                cn.setCluster(true);
                cn.setClusterId(c.getId());
                cn.setPointCount(c.getNumPoints());
                cn.setX(xLng(c.getX()));
                cn.setY(yLat(c.getY()));
                clusters.add(cn);
            } else {
                T vo = points.get(c.getIndex());
                JClusterNode<T> cn = new JClusterNode<>();
                cn.setClusterId(vo.getId());
                cn.setX(xLng(c.getX()));
                cn.setY(yLat(c.getY()));
                cn.setData(vo);
                clusters.add(cn);
            }
        }

        return clusters;
    }

    /**
     * 获取聚簇节点下所有叶子节点。
     *
     * @param clusterId
     * @return
     */
    public List<T> getLeaves(int clusterId) {
        int limit = Integer.MAX_VALUE;
        int offset = 0;

        List<T> leaves = new ArrayList<>();
        appendLeaves(leaves, clusterId, limit, offset, 0);

        return leaves;
    }

    /**
     * 构建聚簇对象。
     * @param points
     * @param zoom
     * @return
     */
    private List<JKDNode> buildCluster(List<JKDNode> points, int zoom) {
        List<JKDNode> clusters = new ArrayList<>();
        double r = option.getRadius() / (option.getExtent() * Math.pow(2, zoom));

        for (int i = 0; i < points.size(); i++) {
            JKDNode p = points.get(i);
            if (p.getZoom() <= zoom) {
                continue;
            }

            p.setZoom(zoom);

            // 找到所有临近的节点做汇聚
            JKDBush tree = trees.get(zoom + 1);
            List<Integer> neighborIds = tree.within(p.getX(), p.getY(), r);

            int numPoints = (p.getNumPoints() != 0) ? p.getNumPoints() : 1;
            double wx = p.getX() * numPoints;
            double wy = p.getY() * numPoints;

            // cluster id中包含的zoom和原始对象ID的信息
            int id = (i << 5) + (zoom + 1);

            for (int neighborId : neighborIds) {
                JKDNode b = tree.getPoints().get(neighborId);

                // 过滤掉已处理过的邻居节点
                if (b.getZoom() <= zoom) {
                    continue;
                }

                b.setZoom(zoom);
                int numPoints2 = (b.getNumPoints() != 0) ? b.getNumPoints() : 1;
                wx += b.getX() * numPoints2;
                wy += b.getY() * numPoints2;

                numPoints += numPoints2;
                b.setParentId(id);
            }

            if (numPoints == 1) {
                clusters.add(p);
            } else {
                p.setParentId(id);
                clusters.add(createCluster(wx / numPoints, wy / numPoints, id, numPoints, null));
            }
        }

        return clusters;
    }

    /**
     * 获取聚簇节点下的子节点。
     *
     * @param clusterId
     * @return
     */
    private List<JClusterNode<T>> getChildren(int clusterId) {
        int originId = clusterId >> 5;
        int originZoom = clusterId % 32;

        List<JClusterNode<T>> children = new ArrayList<>();

        JKDBush index = this.trees.get(originZoom);
        if (index == null) {
            return children;
        }

        JKDNode origin = index.getPoints().get(originId);
        if (origin == null) {
            return children;
        }

        double r = option.getRadius() / (option.getExtent() * Math.pow(2, originZoom - 1));
        List<Integer> ids = index.within(origin.getX(), origin.getY(), r);

        for (int id : ids) {
            JKDNode c = index.getPoints().get(id);
            if (c.getParentId() == clusterId) {
                if (c.getNumPoints() > 0) {
                    JClusterNode<T> cn = new JClusterNode<>();
                    cn.setCluster(true);
                    cn.setClusterId(c.getId());
                    cn.setPointCount(c.getNumPoints());
                    cn.setX(c.getX());
                    cn.setY(c.getY());
                    children.add(cn);
                } else {
                    T vo = points.get(c.getIndex());
                    JClusterNode<T> cn = new JClusterNode<>();
                    cn.setClusterId(vo.getId());
                    cn.setX(vo.getLongitude());
                    cn.setY(vo.getLatitude());
                    cn.setData(vo);
                    children.add(cn);
                }
            }
        }

        return children;
    }

    /**
     * 添加叶子节点。
     *
     * @param result
     * @param clusterId
     * @param limit
     * @param offset
     * @param skipped
     * @return
     */
    private int appendLeaves(List<T> result, int clusterId, int limit, int offset, int skipped) {
       List<JClusterNode<T>> children = getChildren(clusterId);

       for (JClusterNode<T> child : children) {
           if (child.isCluster()) {
               if (skipped + child.getPointCount() <= offset) {
                   // 跳过整个聚簇节点
                   skipped += child.getPointCount();
               } else {
                   skipped = appendLeaves(result, child.getClusterId(), limit, offset, skipped);
               }
           } else if (skipped < offset) {
               skipped++;
           } else {
               result.add(child.getData());
           }

           if (result.size() == limit) {
               break;
           }
       }

       return skipped;
    }

    private  int limitZoom(int z) {
        return  Math.max(option.getMinZoom(), Math.min(z, option.getMaxZoom() + 1));
    }

    /**
     * 将经度转成墨卡托坐标
     * @param lng
     * @return
     */
    private double lngX(double lng) {
        return lng / 360 + 0.5;
    }

    private double latY(double lat) {
        double sin = Math.sin(lat * Math.PI / 180);
        double y = (0.5 - 0.25 * Math.log((1 + sin) / (1 - sin)) / Math.PI);
        return y < 0 ? 0 : y > 1 ? 1 : y;
    }

    /**
     * 墨卡托坐标转经度。
     * @return
     */
    private double xLng(double x) {
        return (x - 0.5) * 360;
    }

    private double yLat(double y) {
        double y2 = (180 - y * 360) * Math.PI / 180;
        return 360 * Math.atan(Math.exp(y2)) / Math.PI - 90;
    }

    /**
     * 构建聚合节点。
     *
     * @return
     */
    private JKDNode createCluster(double x, double y, int id, int numPoints, Object properties) {
       JKDNode cp = new JKDNode();
       cp.setId(id);
       cp.setX(x);
       cp.setY(y);
       cp.setParentId(-1);
       cp.setNumPoints(numPoints);
       cp.setProperties(properties);

       return cp;
    }
}
