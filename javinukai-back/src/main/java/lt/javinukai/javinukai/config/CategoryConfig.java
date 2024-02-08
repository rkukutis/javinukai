package lt.javinukai.javinukai.config;

import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.repository.CategoryRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class CategoryConfig {

    private final ContestRepository contestRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryConfig(ContestRepository contestRepository, CategoryRepository categoryRepository) {
        this.contestRepository = contestRepository;
        this.categoryRepository = categoryRepository;
    }

    @Bean
    public CommandLineRunner contestCreator() {
        return runner -> {
            createContestAndCategories();
            createCategory();
            addCategoryToExistingContest(createIncompleteContestAndCategories());
        };
    }

    private void createCategory() {
        Category category01 = Category.builder()
                .categoryName("gamta")
                .description("ir taip aišku ")
                .totalSubmissions(100)
                .build();
        categoryRepository.save(category01);
    }

    private void addCategoryToExistingContest(Contest incompleteContest) {
        Category category01 = Category.builder()
                .categoryName("asmenys")
                .description("patirties nešėjai")
                .totalSubmissions(100)
                .build();
        incompleteContest.addCategory(category01);
        Category category02 = Category.builder()
                .categoryName("asmenys")
                .description("patirties nešėjai")
                .totalSubmissions(555)
                .build();
        incompleteContest.addCategory(category02);
        contestRepository.save(incompleteContest);
    }

    private Contest createIncompleteContestAndCategories() {

        Category category01 = Category.builder()
                .categoryName("knyga")
                .description("spausdintas žodis")
                .totalSubmissions(100)
                .build();

        Category category02 = Category.builder()
                .categoryName("mokyklos klasė")
                .description("dažnam pažįstama")
                .totalSubmissions(50)
                .build();

        Contest contest01 = Contest.builder()
                .contestName("išmintis")
                .description("nes žinai")
                .totalSubmissions(888)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        contest01.addCategory(category01);
        contest01.addCategory(category02);

        contestRepository.save(contest01);
        return contest01;
    }

    private void createContestAndCategories() {

        Category category01 = Category.builder()
                .categoryName("medicina")
                .description("spaudintas žodis")
                .totalSubmissions(100)
                .build();

        Category category02 = Category.builder()
                .categoryName("sportas")
                .description("citius, altius, fortius")
                .totalSubmissions(20)
                .build();

        Category category03 = Category.builder()
                .categoryName("istorija")
                .description("daugiau, nei kaulai ir griuvėsiai")
                .totalSubmissions(500)
                .build();

        Contest contest01 = Contest.builder()
                .contestName("viltis")
                .description("paskutinė, nepabėgusi")
                .totalSubmissions(777)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now())
                .build();

        contest01.addCategory(category01);
        contest01.addCategory(category02);
        contest01.addCategory(category03);

        contestRepository.save(contest01);
    }

}
