package com.doramonz.aligonggoo.config.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateStateStep {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

//    @JobScope
//    public Step updateStateStep() {
//        return new StepBuilder("updateStateStep", jobRepository)
//                .<ProductGongGooBatchDao, Integer>chunk(10, transactionManager)
//                .reader(reader())
//                .processor(new ProductGongGooUpdateStateProcessor(aliProductUtil))
//                .writer(writer())
//                .build();
//    }

    @StepScope
    public ItemReader<ProductGongGooBatchDao> reader() {
        JdbcCursorItemReader<ProductGongGooBatchDao> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT * FROM product_gongGoo WHERE gongGoo_status = 1");
        reader.setRowMapper(new ProductGongGooBatchRowMapper());
        return reader;
    }

    @StepScope
    public ItemWriter<Integer> writer() {
        return items -> {
            for (Integer item : items) {
                if (item != null) {
                    log.debug("Close Expired ProductGongGooBatchDao. id: {}", item);
                    dataSource.getConnection().prepareStatement("UPDATE product_gongGoo SET gongGoo_status = 0 WHERE product_gongGoo_id = " + item).execute();
                }
            }
        };
    }
}
