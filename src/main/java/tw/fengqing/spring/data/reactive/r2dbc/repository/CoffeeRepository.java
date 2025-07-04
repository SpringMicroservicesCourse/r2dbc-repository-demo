package tw.fengqing.spring.data.reactive.r2dbc.repository;

import tw.fengqing.spring.data.reactive.r2dbc.model.Coffee;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CoffeeRepository extends ReactiveCrudRepository<Coffee, Long> {
    @Query("select * from t_coffee where name = $1")
    Flux<Coffee> findByName(String name);
}
