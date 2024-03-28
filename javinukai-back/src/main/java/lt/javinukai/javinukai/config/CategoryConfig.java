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
                .name("Vivamus tempus suscipit neque")
                .description("Nullam in justo vel elit hendrerit cursus. Vivamus tempus suscipit neque, at commodo nisl vulputate in. Proin lobortis quis tellus vel euismod. Cras tincidunt elementum ante in consequat. Etiam lobortis sagittis risus, sed tempor erat dictum eget. Proin scelerisque sed. ")
                .maxTotalSubmissions(100)
                .maxUserSubmissions(30)
                .type(PhotoSubmissionType.SINGLE)
                .build();
        categoryRepository.save(category01);
    }

    private void addCategoryToExistingContest(Contest incompleteContest) {
        Category category01 = Category.builder()
                .name("Nulla id risus sed purus dapibu")
                .description("Cras ipsum eros, mollis id elementum in, facilisis eu quam. Nulla id risus sed purus dapibus fringilla vel eu nunc. Nam tristique quam non erat laoreet, eu gravida ligula egestas. Morbi aliquam nec turpis eu sodales. Nam dapibus suscipit dui. ")
                .maxTotalSubmissions(100)
                .maxUserSubmissions(30)
                .type(PhotoSubmissionType.SINGLE)
                .build();
        incompleteContest.addCategory(category01);
        Category category02 = Category.builder()
                .name("Phasellus lacinia massa")
                .description("Quisque at orci vitae nisi efficitur condimentum vel vel mauris. Morbi bibendum turpis dolor, quis volutpat mi pellentesque vel. Phasellus lacinia massa vitae volutpat convallis. Praesent id felis euismod, fringilla est eu, eleifend orci. Ut nec nunc ac tellus laoreet. ")
                .maxTotalSubmissions(555)
                .maxUserSubmissions(30)
                .type(PhotoSubmissionType.SINGLE)
                .build();
        incompleteContest.addCategory(category02);
        contestRepository.save(incompleteContest);
    }

    private Contest createIncompleteContestAndCategories() {

        Category category01 = Category.builder()
                .name("Curabitur in condimentum nisl")
                .description("Nunc venenatis interdum velit, vel commodo sem. Phasellus iaculis ex nec dapibus varius. Praesent pulvinar velit sit amet lectus vehicula, vel consequat lectus consectetur. Duis vulputate sagittis sapien eu blandit. Vestibulum sapien odio, interdum ac ante sit amet, fringilla interdum purus. In hac habitasse platea dictumst. Morbi ac metus vel massa ultricies vehicula sit amet in nisl. Morbi bibendum dolor ut massa commodo, et pulvinar enim condimentum. Nullam feugiat placerat arcu, sed placerat ex pulvinar et. Aenean at laoreet nunc. Nam sit amet dui sit amet nulla faucibus rutrum. ")
                .maxTotalSubmissions(100)
                .maxUserSubmissions(30)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        Category category02 = Category.builder()
                .name("Donec lacus est, fermentum vel rutrum id, tincidunt ut sapien")
                .description("Suspendisse euismod sollicitudin tellus in porta. Praesent faucibus elit eu arcu mattis, ac vehicula massa tincidunt. In auctor mi rhoncus nisl ultrices, a posuere elit pharetra. Aenean tempor arcu at orci aliquam, et pharetra sapien dapibus. Pellentesque volutpat dolor lectus. Proin venenatis, nulla in rutrum egestas, arcu tellus viverra ipsum, eget condimentum risus diam ut ante. Aliquam volutpat viverra risus, eget sodales augue dignissim in. Nulla nulla elit, gravida at placerat eu, tristique vel lacus. Duis a nisi id orci scelerisque bibendum posuere ut leo. Integer fringilla enim at rhoncus congue. Integer viverra quis ipsum non consectetur. Aenean egestas id lorem in dictum. ")
                .maxTotalSubmissions(50)
                .maxUserSubmissions(30)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        Category category03 = Category.builder()
                .name("Duis vestibulum tincidunt quam nec scelerisque")
                .description("Duis tincidunt vehicula ornare. Duis ac pretium arcu. Nunc vitae venenatis est. Morbi eu elit et libero feugiat elementum. In hac habitasse platea dictumst")
                .maxTotalSubmissions(50)
                .maxUserSubmissions(30)
                .type(PhotoSubmissionType.COLLECTION)
                .build();

        Contest contest01 = Contest.builder()
                .name("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ")
                .description("Integer ullamcorper, velit in consequat sollicitudin, enim metus venenatis neque, vel iaculis nisi lectus et est. In hac habitasse platea dictumst. Nulla turpis nunc, rutrum ut cursus id, rutrum vitae ante. Praesent viverra tristique elementum. Vestibulum tempus ante nisl, ut porta tortor auctor in. Nunc viverra sapien mauris, at blandit neque pretium in. Maecenas turpis urna, posuere sit amet leo id, luctus cursus ante. Sed hendrerit urna ut enim mattis, ut volutpat risus gravida. Nam ut metus rutrum, consequat neque vel, aliquet leo. ")
                .maxTotalSubmissions(888)
                .maxUserSubmissions(50)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now().plusDays(7))
                .build();

        contest01.addCategory(category01);
        contest01.addCategory(category02);
        contest01.addCategory(category03);
        contestRepository.save(contest01);
        return contest01;
    }

    private void createContestAndCategories() {

        Category category01 = Category.builder()
                .name("condimentum vel vel mauris")
                .description("Quisque at orci vitae nisi efficitur condimentum vel vel mauris. Morbi bibendum turpis dolor, quis volutpat mi pellentesque vel. Phasellus lacinia massa vitae volutpat convallis. Praesent id felis euismod, fringilla est eu, eleifend orci. Ut nec nunc ac tellus laoreet")
                .maxTotalSubmissions(100)
                .maxUserSubmissions(50)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        Category category02 = Category.builder()
                .name("Sed maximus nisi condimentum")
                .description("Nunc consequat dapibus enim, eu tempus erat pretium sed. Integer condimentum sed risus eget vehicula. Maecenas eu sem libero. Suspendisse rhoncus sodales sagittis. Sed maximus nisi condimentum, molestie quam vel, iaculis libero. Sed eros nulla, consequat ut sem id, eleifend")
                .maxTotalSubmissions(20)
                .maxUserSubmissions(6)
                .type(PhotoSubmissionType.COLLECTION)
                .build();

        Category category03 = Category.builder()
                .name("Pellentesque egestas egestas suscipit")
                .description("Pellentesque nec nunc mattis ex mollis varius. Aliquam non odio ultricies, eleifend sapien ac, scelerisque lectus. Pellentesque egestas egestas suscipit. Cras nec commodo quam. Morbi porttitor nunc tincidunt augue egestas, sagittis ullamcorper tortor tempus. Duis condimentum turpis felis, sed malesuada")
                .maxTotalSubmissions(500)
                .maxUserSubmissions(30)
                .type(PhotoSubmissionType.SINGLE)
                .build();

        Contest contest01 = Contest.builder()
                .name("Mauris at pretium est, et rutrum libero")
                .description("Curabitur eget risus fermentum, molestie dolor imperdiet, rutrum nibh. Ut ac ante dolor. Integer malesuada, ante tempus auctor bibendum, sem orci venenatis dui, vel mattis odio lacus vel felis. Quisque sodales, tellus ac porta condimentum, tortor nibh cursus felis, id sollicitudin augue nisl eget arcu. Nam iaculis libero mollis lectus malesuada aliquet. Cras pharetra orci diam, sit amet luctus diam dictum at. Quisque in nisi ut est pellentesque ultrices. Aliquam eget enim non quam cursus gravida vel in risus. ")
                .maxTotalSubmissions(777)
                .maxUserSubmissions(50)
                .startDate(ZonedDateTime.now())
                .endDate(ZonedDateTime.now().plusDays(14))
                .build();

        contest01.addCategory(category01);
        contest01.addCategory(category02);
        contest01.addCategory(category03);

        contestRepository.save(contest01);
    }

}
