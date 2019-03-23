package com.elon.jsc.kdbush;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * KD-Tree空间范围查询
 */
public class JKDRange {

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

    public JKDRange(int nodeSize, List<Integer> ids, List<Double> coords) {
        this.nodeSize = nodeSize;
        this.ids = ids;
        this.coords = coords;
    }

    /**
     * 查询矩形范围内的点，过滤不在屏幕范围内的对象。
     *
     * @param minX
     * @param minY
     * @param maxX
     * @param maxY
     * @return
     */
    public List<Integer> range(double minX, double minY, double maxX, double maxY) {
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        stack.push(ids.size() - 1);
        stack.push(0);

        List<Integer> result = new ArrayList<>();

        // 递归搜索KD-Tree上指定范围内的元素。
        while (!stack.isEmpty()) {
            int axis = stack.pop();
            int right = stack.pop();
            int left = stack.pop();

            if (right - left <= nodeSize) {
                for (int i = left; i <= right; i++) {
                    double x = coords.get(2 * i);
                    double y = coords.get(2 * i + 1);
                    if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
                        result.add(ids.get(i));
                    }
                }

                continue;
            }

            int m = (left + right) >> 1;

            // 如果中间节点在范围内，加入结果集。
            double x = coords.get(2 * m);
            double y = coords.get(2 * m + 1);
            if (x >= minX && x <= maxX && y >= minY && y <= maxY){
                result.add(ids.get(m));
            }

            int nextAxis = (axis + 1) % 2;

            if (axis == 0? minX <= x : minY <= y) {
                stack.push(left);
                stack.push(m - 1);
                stack.push(nextAxis);
            }

            if (axis == 0 ? maxX >= x : maxY >= y) {
                stack.push(m + 1);
                stack.push(right);
                stack.push(nextAxis);
            }
        }

        return result;
    }
}
