package com.phyllis.transfer_analysis;

import com.phyllis.transferhistory_analysis.beans.UserChurnAcc;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.util.ArrayList;
import java.util.HashMap;

public class flink_elasticSearch {
    public static void main(String[] args) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        //读取数据建 DataStream
        DataStream<String> inputStream = env.readTextFile("C:\\Edrive\\ELTE\\streamProject\\TransferHistoryAnalysis\\src\\main\\resources\\flatMapResult_date.csv");
        DataStream<UserChurnAcc> dataStream = inputStream.map(line->{
           String[] fields = line.split(",");
           return new UserChurnAcc(fields[0],new Long(fields[1]));
        });
        ArrayList<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost("localhost",9200));
        dataStream.addSink(new ElasticsearchSink.Builder<UserChurnAcc>(httpHosts, new MyEsSinkFunction()).build());
        env.execute();
    }

    //implement my defined ES read opertor
    public static class MyEsSinkFunction implements ElasticsearchSinkFunction<UserChurnAcc>{

        @Override
        public void process(UserChurnAcc userChurnAcc, RuntimeContext runtimeContext, RequestIndexer requestIndexer) {
            //define write datasource
            HashMap<String,String> dataSource = new HashMap<>();
            dataSource.put("churnDate",userChurnAcc.getChurnDates().toString());
            dataSource.put("currentNumber",userChurnAcc.getChurnNumber().toString());
        //create requests,as a commend to write into ES
            IndexRequest indexRequest = Requests.indexRequest()
                    .index("churn_date")
                    .type("readingdata")
                    .source(dataSource);

            //use index to send request
            requestIndexer.add(indexRequest);
        }
    }

}
