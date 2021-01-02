package com.phyllis.transferhistory_analysis;

import com.phyllis.transferhistory_analysis.beans.UserBehavior;
import com.phyllis.transferhistory_analysis.beans.UserChurnAcc;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class UserChurn {
    public static void main(String[] args) throws Exception {
        //创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //读取数据 创建 DataStream
        DataStream<String> inputStream = env.readTextFile("C:\\Edrive\\ELTE\\streamProject\\TransferHistoryAnalysis\\src\\main\\resources\\transfer_history_v2.csv");

        //转化为POJO 分配时间戳和watermark
        DataStream<UserBehavior> dataStream = inputStream
                .map(line -> {
                    String[] fileds = line.split(",");
                    return new UserBehavior(new Long(fileds[0]), new Long(fileds[1]), new Long(fileds[2]));
                })
                .assignTimestampsAndWatermarks(new AscendingTimestampExtractor<UserBehavior>() {
                    @Override
                    public long extractAscendingTimestamp(UserBehavior element) {
                        return element.getValueDate()*1000L;
                    }
                });

        //分组开窗聚合，得到每个窗口内所有的partyid的唯一总数，也就是说，统计出每天转账的账号有多少
        DataStream<UserChurnAcc> windowAggStream = dataStream
                .keyBy("valueDate")
                .timeWindow(Time.days(1),Time.days(1))
                .aggregate( new UserChurnAgg(), new WindowUserChurnResult());
        env.execute("user churn analysis");
    }

    //实现自定义聚合增量函数
    public static class UserChurnAgg implements AggregateFunction<UserBehavior,Long,Long>{
        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(UserBehavior userBehavior, Long aLong) {
            return aLong + 1;
        }

        @Override
        public Long getResult(Long aLong) {
            return aLong ;
        }

        @Override
        public Long merge(Long aLong, Long acc1) {
            return aLong+acc1;
        }
    }

    //自定义全窗口函数
    public static class WindowUserChurnResult implements WindowFunction<Long, UserChurnAcc, Tuple, TimeWindow>{
        @Override
        public void apply(Tuple tuple, TimeWindow timeWindow, Iterable<Long> iterable, Collector<UserChurnAcc> collector) throws Exception {
            Long valueDate = tuple.getField(0);
            Long  currentNumber = iterable.iterator().next();
            Long churnNumber = iterable.iterator().next();
            collector.collect(new UserChurnAcc(valueDate, currentNumber, churnNumber));

//            //将统计结果输出
//            StringBuilder resultBuilder = new StringBuilder();
//            resultBuilder.append("_______________________");
//            resultBuilder.append(collector);
//            Thread.sleep(1000L);
//            collector.collect(resultBuilder);

        }
    }



}
