package com.elon.jsc;
import com.alibaba.fastjson.JSONObject;
import com.elon.jsc.model.AppModelTest;
import com.elon.jsc.model.JClusterNode;
import com.elon.jsc.model.JClusterOption;
import com.elon.jsc.supercluster.JSuperCluster;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用启动类
 */
@SpringBootApplication
public class StartupSuperCluster {

    private static Logger logger = LogManager.getLogger(StartupSuperCluster.class);

    public static void main(String[] args){
        SpringApplication.run(StartupSuperCluster.class, args);
        logger.info("Start up Java Super Cluster Success!");

        // 测试点的汇聚
        List<AppModelTest> testDataList = new ArrayList<>();
        AppModelTest test1 = new AppModelTest();
        test1.setId(1);
        test1.setLongitude(112.97362);
        test1.setLatitude(27.83088);
        testDataList.add(test1);

        AppModelTest test2 = new AppModelTest();
        test2.setId(2);
        test2.setLongitude(112.98363);
        test2.setLatitude(27.84087);
        testDataList.add(test2);

        JClusterOption option = new JClusterOption();

        JSuperCluster jsc = new JSuperCluster<AppModelTest>(option);
        jsc.load(testDataList);

        List<Double> bbox = new ArrayList<>();
        bbox.add(112.6675);
        bbox.add(27.76451);
        bbox.add(113.13648);
        bbox.add(27.95462);
        List<JClusterNode<AppModelTest>> resultList = jsc.getClusters(bbox, 3);

        System.out.println("汇聚结果:" + JSONObject.toJSONString(resultList));

        System.out.println("显示聚簇点下的叶子节点：");
        for (JClusterNode<AppModelTest> c : resultList) {
            if (c.isCluster()) {
                System.out.println("汇聚节点ID：" + c.getClusterId());
                System.out.println("叶子节点：" + JSONObject.toJSONString(jsc.getLeaves(c.getClusterId())));
            }
        }
    }
}
