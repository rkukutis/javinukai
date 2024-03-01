package lt.javinukai.javinukai.config;

import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.enums.PhotoSubmissionType;
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
                .name("gamta")
                .description("ne mūsų kurta aplinka")
                .totalSubmissions(100)
                .type(PhotoSubmissionType.SINGLE)
                .build();
        categoryRepository.save(category01);
    }

    private void addCategoryToExistingContest(Contest incompleteContest) {
        Category category01 = Category.builder()
                .name("asmenys")
                .description("gyvenimiška patirtis")
                .totalSubmissions(100)
                .type(PhotoSubmissionType.SINGLE)
                .build();
        incompleteContest.addCategory(category01);
        Category category02 = Category.builder()
                .name("asmenys")
                .description("gyvenimiška patirtis")
                .totalSubmissions(555)
                .type(PhotoSubmissionType.SINGLE)
                .build();
        incompleteContest.addCategory(category02);
        contestRepository.save(incompleteContest);
    }

    private Contest createIncompleteContestAndCategories() {

        Category category01 = Category.builder()
                .name("Curabitur in condimentum nisl")
                .description("Nunc venenatis interdum velit, vel commodo sem. Phasellus iaculis ex nec dapibus varius. Praesent pulvinar velit sit amet lectus vehicula, vel consequat lectus consectetur. Duis vulputate sagittis sapien eu blandit. Vestibulum sapien odio, interdum ac ante sit amet, fringilla interdum purus. In hac habitasse platea dictumst. Morbi ac metus vel massa ultricies vehicula sit amet in nisl. Morbi bibendum dolor ut massa commodo, et pulvinar enim condimentum. Nullam feugiat placerat arcu, sed placerat ex pulvinar et. Aenean at laoreet nunc. Nam sit amet dui sit amet nulla faucibus rutrum. ")
                .totalSubmissions(100)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        Category category02 = Category.builder()
                .name("Donec lacus est, fermentum vel rutrum id, tincidunt ut sapien")
                .description("Suspendisse euismod sollicitudin tellus in porta. Praesent faucibus elit eu arcu mattis, ac vehicula massa tincidunt. In auctor mi rhoncus nisl ultrices, a posuere elit pharetra. Aenean tempor arcu at orci aliquam, et pharetra sapien dapibus. Pellentesque volutpat dolor lectus. Proin venenatis, nulla in rutrum egestas, arcu tellus viverra ipsum, eget condimentum risus diam ut ante. Aliquam volutpat viverra risus, eget sodales augue dignissim in. Nulla nulla elit, gravida at placerat eu, tristique vel lacus. Duis a nisi id orci scelerisque bibendum posuere ut leo. Integer fringilla enim at rhoncus congue. Integer viverra quis ipsum non consectetur. Aenean egestas id lorem in dictum. ")
                .totalSubmissions(50)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        Contest contest01 = Contest.builder()
                .name("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ")
                .description("Integer ullamcorper, velit in consequat sollicitudin, enim metus venenatis neque, vel iaculis nisi lectus et est. In hac habitasse platea dictumst. Nulla turpis nunc, rutrum ut cursus id, rutrum vitae ante. Praesent viverra tristique elementum. Vestibulum tempus ante nisl, ut porta tortor auctor in. Nunc viverra sapien mauris, at blandit neque pretium in. Maecenas turpis urna, posuere sit amet leo id, luctus cursus ante. Sed hendrerit urna ut enim mattis, ut volutpat risus gravida. Nam ut metus rutrum, consequat neque vel, aliquet leo. ")
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
                .name("medicina")
                .description("slaugos mokslas")
                .totalSubmissions(100)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        Category category02 = Category.builder()
                .name("sportas")
                .description("citius, altius, fortius")
                .totalSubmissions(20)
                .type(PhotoSubmissionType.COLLECTION)
                .build();

        Category category03 = Category.builder()
                .name("istorija")
                .description("daugiau, nei kaulai ir griuvėsiai")
                .totalSubmissions(500)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        Contest contest01 = Contest.builder()
                .name("Mauris at pretium est, et rutrum libero")
                .description("Curabitur eget risus fermentum, molestie dolor imperdiet, rutrum nibh. Ut ac ante dolor. Integer malesuada, ante tempus auctor bibendum, sem orci venenatis dui, vel mattis odio lacus vel felis. Quisque sodales, tellus ac porta condimentum, tortor nibh cursus felis, id sollicitudin augue nisl eget arcu. Nam iaculis libero mollis lectus malesuada aliquet. Cras pharetra orci diam, sit amet luctus diam dictum at. Quisque in nisi ut est pellentesque ultrices. Aliquam eget enim non quam cursus gravida vel in risus. ")
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
