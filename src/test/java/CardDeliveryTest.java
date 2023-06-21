import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.javafaker.Faker;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;


import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {


    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeAll
    static void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    @DisplayName("Should successful plan meeting")
    void shouldSuccessfulPlanMeeting() {
        Configuration.holdBrowserOpen = true;
        DataGenerate.UserInfo info = DataGenerate.Registration.generateUser("ru");
        int addDaysToFirstMeeting = 3;
        String firstMeetingDate = DataGenerate.generateDate(addDaysToFirstMeeting);
        int addDaysToSecondMeeting = 8;
        String secondMeetingDate = DataGenerate.generateDate(addDaysToSecondMeeting);
        $x("//input[contains(@placeholder,'Город')]").setValue(info.getCity());
        $x("//input[contains(@placeholder,'Дата встречи')]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $x("//input[contains(@placeholder,'Дата встречи')]").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(info.getName());
        $("[data-test-id=phone] input").setValue(info.getPhone());
        $("[data-test-id=agreement]").click();
        $x("//span[contains(text(),'Запланировать')]").click();
        $x("//div[contains(text(),'Встреча успешно запланирована на ')]").shouldBe(Condition.visible, Duration.ofSeconds(12));
        $x("//div[contains(text(),'Встреча успешно запланирована на ')]").shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $x("//input[contains(@placeholder,'Дата встречи')]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $x("//input[contains(@placeholder,'Дата встречи')]").setValue(secondMeetingDate);
        $x("//span[contains(text(),'Запланировать')]").click();
        $("[data-test-id=replan-notification]").shouldBe(Condition.visible, Duration.ofSeconds(12));
        $("[data-test-id=replan-notification] button").click();
        $("[data-test-id=success-notification]").shouldBe(Condition.visible, Duration.ofSeconds(12));
        $("[data-test-id=success-notification] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate));
    }

}
