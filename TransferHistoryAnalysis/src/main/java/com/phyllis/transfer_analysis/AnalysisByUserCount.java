package com.phyllis.transfer_analysis;

import com.phyllis.transferhistory_analysis.beans.UserBehavior;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.kafka.common.protocol.types.Field;

import java.util.ArrayList;

public class AnalysisByUserCount {
    public static void main(String[] args) throws Exception{
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //读取数据 创建 DataStream
        DataSet<String> inputDataSet = env.readTextFile("C:\\Edrive\\ELTE\\streamProject\\TransferHistoryAnalysis\\src\\main\\resources\\transfer_history.csv");

//        DataStream<UserBehavior> dataStream = inputStream.map(new MapFunction<String, UserBehavior>() {
//            @Override
//            public UserBehavior map(String s) throws Exception {
//                String[] fields = s.split(",");
//                return new UserBehavior(new Long(fields[0]),new Long(fields[1]),new Long(fields[2]));
//            }
//        });
//           // group
//        KeyedStream<UserBehavior, Tuple> keyedStream = dataStream.keyBy("valueDate");
//        //滚动聚合
//        DataStream<UserBehavior> resultStream = keyedStream.sum("partyId");
//        resultStream.print();
        DataSet<Tuple2<String,Integer>> resultSet = inputDataSet.flatMap(new AnalysisByUserCount.MyFlatMapper())
                .groupBy(0)
                .sum(1);

        resultSet.print();
//        resultSet.writeAsCsv("C:\\Edrive\\ELTE\\streamProject\\TransferHistoryAnalysis\\src\\main\\resources\\flatMapResult.csv");
        resultSet.writeAsCsv("C:\\Edrive\\ELTE\\streamProject\\TransferHistoryAnalysis\\src\\main\\resources\\flatMapResult_date.csv");
        env.execute();
    }

//    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String,Integer>> {
//        @Override
//        public void flatMap(String s, Collector<Tuple2<String,Integer>> collector) throws Exception {
//            String[] Fileds = s.split(",");
//            String time = Fileds[5].substring(0,7);
//            //get each month's transfer counts
//            collector.collect(new Tuple2<>(time,1));
//        }
//    }

    public static class MyFlatMapper implements FlatMapFunction<String, Tuple2<String,Integer>> {
        @Override
        public void flatMap(String s, Collector<Tuple2<String,Integer>> collector) throws Exception {
            String[] Fileds = s.split(",");
            String time = Fileds[5].substring(0,7);
            //get each month's transfer counts
            collector.collect(new Tuple2<>(time,1));
        }
    }


}
