package com.elon.jsc.kdbush;

import java.util.List;

/**
 * KD树排序
 */
public class JKDSort {

    /**
     * 节点数量
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

    /**
     * 构造方法
     */
    public JKDSort(int nodeSize, List<Integer> ids, List<Double> coords) {
        this.nodeSize = nodeSize;
        this.ids = ids;
        this.coords = coords;
    }

    /**
     * 数据排序
     *
     * @param left
     * @param right
     * @param depth
     */
    public void sort(int left, int right, int depth) {
        if (right - left <= nodeSize) {
            return;
        }

        // 计算中间节点的位置
        int m = (left + right) / 2;

        // 以中间节点排序
        select(m, left, right, depth % 2);

        // 递归处理左右子树
        sort(left, m - 1, depth + 1);
        sort(m + 1, right, depth + 1);
    }

    /**
     * 排序使左子树的点小于右子树的点。
     *
     * @param k
     * @param left
     * @param right
     * @param depth
     */
    private void select(int k, int left, int right, int depth) {

        while (right > left) {
            if (right - left > 600) {
                int n = right - left + 1;
                int m = k - left + 1;
                double z = Math.log(n);
                double s = 0.5 * Math.exp(2 * z / 3);
                double sd = 0.5 * Math.sqrt(z * s * (n - s) / n) * (m - n / 2 < 0 ? -1 : 1);
                int newLeft = (int) Math.max(left, Math.floor(k - m * s / n + sd));
                int newRight = (int) Math.min(right, Math.floor(k + (n - m) * s / n + sd));
                select(k, newLeft, newRight, depth);
            }

            double t = coords.get(2 * k + depth);
            int i = left;
            int j = right;

            swapItem(left, k);
            if (coords.get(2 * right + depth) > t){
                swapItem(left, right);
            }

            while (i < j) {
                swapItem(i, j);
                i++;
                j--;

                while (coords.get(2 * i + depth) < t) {
                    i++;
                }
                while (coords.get(2 * j + depth) > t) {
                    j--;
                }
            }

            if (Double.compare(coords.get(2 * left + depth), t) == 0) {
                swapItem(left, j);
            } else {
                j++;
                swapItem(j, right);
            }

            if (j <= k) {
                left = j + 1;
            }
            if (k <= j) {
                right = j - 1;
            }

        }
    }

    private void swapItem(int i, int j){
        swapInt(ids, i, j);
        swapDouble(coords, 2 * i, 2 * j);
        swapDouble(coords, 2 * i + 1, 2 * j +1);
    }

    private void swapInt(List<Integer> arr, int i, int j) {
        int tmp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, tmp);
    }

    private void swapDouble(List<Double> arr, int i, int j) {
        double tmp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, tmp);
    }

}