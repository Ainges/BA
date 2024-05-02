package routes.preonboarding;

import Singeltons.OkHttpClientSingelton;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@ApplicationScoped
public class PreOnboardingBirthdayCalculationRouteTest {

    @Inject
    OkHttpClientSingelton okHttpClientSingelton;

    //TODO: Implement dynamic Dates because the test can fail because it is using the current date

    @Test
    public void testbeforeBirthday() {

        given()
            .accept("application/json")
            .header("Content-Type", "application/json")
            .body("{\n" +
                  "    \"first_working_day\":\"29-04-2024\",\n" +
                  "    \"birth_date\": \"25-04-2000\"\n" +
                  "}")
        .when()
                .post("/onboarding/preonboarding/BirthdayCalculation")
        .then()
            .statusCode(200)
            .body("birthday", equalTo("before"));

        // TODO: AssertFalse
        given()
            .accept("application/json")
            .header("Content-Type", "application/json")
            .body("{\n" +
                  "    \"first_working_day\":\"29-04-2024\",\n" +
                  "    \"birth_date\": \"29-04-2000\"\n" +
                  "}")
                .when()
                .post("/onboarding/preonboarding/BirthdayCalculation")
                .then()
                .statusCode(200)
                .body("birthday", equalTo("before"));
    }

    @Test
    public void testonBirthday() {

        given()
            .accept("application/json")
            .header("Content-Type", "application/json")
            .body("{\n" +
                  "    \"first_working_day\":\"29-04-2024\",\n" +
                  "    \"birth_date\": \"29-04-2000\"\n" +
                  "}")
        .when()
                .post("/onboarding/preonboarding/BirthdayCalculation")
        .then()
            .statusCode(200)
            .body("birthday", equalTo("on"));
    }
}
