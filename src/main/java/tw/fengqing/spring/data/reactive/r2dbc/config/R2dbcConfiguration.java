package tw.fengqing.spring.data.reactive.r2dbc.config;

import tw.fengqing.spring.data.reactive.r2dbc.converter.MoneyReadConverter;
import tw.fengqing.spring.data.reactive.r2dbc.converter.MoneyWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import java.util.Arrays;

@Configuration
public class R2dbcConfiguration {

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        return new R2dbcCustomConversions(
                CustomConversions.StoreConversions.NONE,
                Arrays.asList(new MoneyReadConverter(), new MoneyWriteConverter()));
    }
}
