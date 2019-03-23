package com.elon.jsc.kdbush;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 按搜索半径查询KD-Tree的数据
 */
public class JKDWithin {
    /**
     * KD树节点数量
     */
    private int nodeSize;

    /**
     * 节点ID列表
     */
    private List<Integer> ids = null;

    /**
     * 节点坐标列表
     */
    private List<Double> coords = null;

    public JKDWithin(int nodeSize, List<Integer> ids, List<Double> coords) {
        this.nodeSize = nodeSize;
        this.ids = ids;
        this.coords = coords;
    }

    /**
     * 搜索半径范围内的点。
     *
     * @param qx
     * @param qy
     * @param r
     * @return
     */
    public List<Integer> within(double qx, double qy, double r) {
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        stack.push(ids.size() - 1);
        stack.push(0);

        List<Integer> result = new ArrayList<>();
        double r2 = r * r;

        // 在KD-Tree上搜索半径范围内的数据
        while (!stack.isEmpty()) {
            int axis = stack.pop();
            int right = stack.pop();
            int left = stack.pop();

            if (right - left <= nodeSize) {
                for (int i = left; i <= right; i++) {
                    if (sqDist(coords.get(2 * i), coords.get(2 * i + 1), qx, qy) <= r2) {
                        result.add(ids.get(i));
                    }
                }

                continue;
            }

            int m = (left + right) >> 1;

            double x = coords.get(2 * m);
            double y = coords.get(2 * m + 1);
            if (sqDist(x, y, qx, qy) <= r2) {
                result.add(ids.get(m));
            }

            int nextAxis = (axis + 1) % 2;

            if (axis == 0 ? qx - r <= x : qy - r <= y) {
                stack.push(left);
                stack.push(m - 1);
                stack.push(nextAxis);
            }

            if (axis == 0 ? qx + r >= x : qy + r >= y) {
                stack.push(m + 1);
                stack.push(right);
                stack.push(nextAxis);
            }
        }

        return result;
    }

    private double sqDist(double ax, double ay, double bx, double by) {
        double dx = ax -bx;
        double dy = ay -by;

        return dx * dx + dy * dy;
    }
}
