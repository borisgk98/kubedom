package space.borisgk98.kubedom.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

@Configuration
public class DataAccessConfiguration {

    @Bean
    public CriteriaBuilder getCriteriaBuilder(@Autowired EntityManager em) {
        return em.getCriteriaBuilder();
    }
}
