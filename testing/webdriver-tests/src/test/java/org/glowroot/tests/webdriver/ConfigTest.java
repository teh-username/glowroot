/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glowroot.tests.webdriver;

import org.junit.Test;

import org.glowroot.tests.webdriver.config.AdvancedConfigPage;
import org.glowroot.tests.webdriver.config.ConfigSidebar;
import org.glowroot.tests.webdriver.config.GeneralConfigPage;
import org.glowroot.tests.webdriver.config.SmtpConfigPage;
import org.glowroot.tests.webdriver.config.StorageConfigPage;
import org.glowroot.tests.webdriver.config.UserRecordingConfigPage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

public class ConfigTest extends WebDriverTest {

    @Test
    public void shouldUpdateGeneralConfig() throws Exception {
        // given
        App app = new App(driver, "http://localhost:" + container.getUiPort());
        GlobalNavbar globalNavbar = new GlobalNavbar(driver);
        GeneralConfigPage page = new GeneralConfigPage(driver);

        app.open();
        globalNavbar.getConfigurationLink().click();

        // when
        page.getEnabledSwitchOff().click();
        page.getStoreThresholdTextField().clear();
        page.getStoreThresholdTextField().sendKeys("2345");
        page.getProfilingIntervalTextField().clear();
        page.getProfilingIntervalTextField().sendKeys("3456");
        page.clickSaveButton();

        // then
        app.open();
        globalNavbar.getConfigurationLink().click();
        assertThat(page.getEnabledSwitchOn().getAttribute("class").split(" "))
                .doesNotContain("active");
        assertThat(page.getEnabledSwitchOff().getAttribute("class").split(" ")).contains("active");
        assertThat(page.getStoreThresholdTextField().getAttribute("value")).isEqualTo("2345");
        assertThat(page.getProfilingIntervalTextField().getAttribute("value")).isEqualTo("3456");
    }

    @Test
    public void shouldUpdateSmtpConfig() throws Exception {
        // given
        App app = new App(driver, "http://localhost:" + container.getUiPort());
        GlobalNavbar globalNavbar = new GlobalNavbar(driver);
        ConfigSidebar configSidebar = new ConfigSidebar(driver);
        SmtpConfigPage page = new SmtpConfigPage(driver);

        app.open();
        globalNavbar.getConfigurationLink().click();
        configSidebar.getAlertsLink().click();
        Utils.withWait(driver, xpath("//a[@href='config/smtp']")).click();

        // when
        page.getFromEmailAddressTextField().clear();
        page.getFromEmailAddressTextField().sendKeys("user1234@example.org");
        page.getFromDisplayNameTextField().clear();
        page.getFromDisplayNameTextField().sendKeys("User 1234");
        page.getSmtpHostTextField().clear();
        page.getSmtpHostTextField().sendKeys("example.org");
        page.getSmtpPortTextField().clear();
        page.getSmtpPortTextField().sendKeys("5678");
        page.getUseSslCheckbox().click();
        page.getUsernameTextField().clear();
        page.getUsernameTextField().sendKeys("user1234");
        page.getPasswordTextField().clear();
        page.getPasswordTextField().sendKeys("p");
        page.clickSaveButton();

        // then
        app.open();
        globalNavbar.getConfigurationLink().click();
        configSidebar.getAlertsLink().click();
        Utils.withWait(driver, xpath("//a[@href='config/smtp']")).click();
        assertThat(page.getFromEmailAddressTextField().getAttribute("value"))
                .isEqualTo("user1234@example.org");
        assertThat(page.getFromDisplayNameTextField().getAttribute("value")).isEqualTo("User 1234");
        assertThat(page.getSmtpHostTextField().getAttribute("value")).isEqualTo("example.org");
        assertThat(page.getSmtpPortTextField().getAttribute("value")).isEqualTo("5678");
        assertThat(page.getUseSslCheckbox().isSelected()).isTrue();
        assertThat(page.getUsernameTextField().getAttribute("value")).isEqualTo("user1234");
        assertThat(page.getPasswordTextField().getAttribute("value")).isEqualTo("********");
    }

    @Test
    public void shouldUpdateUserRecordingConfig() throws Exception {
        // given
        App app = new App(driver, "http://localhost:" + container.getUiPort());
        GlobalNavbar globalNavbar = new GlobalNavbar(driver);
        UserRecordingConfigPage page = new UserRecordingConfigPage(driver);

        app.open();
        globalNavbar.getConfigurationLink().click();
        // user recording config is not accessible via config sidebar currently
        String userRecordingUrl = driver.getCurrentUrl().replace("/config/general",
                "/config/user-recording");
        driver.navigate().to(userRecordingUrl);

        // when
        page.getEnabledSwitchOff().click();
        page.getUserTextField().clear();
        page.getUserTextField().sendKeys("abc");
        page.getProfileIntervalTextField().clear();
        page.getProfileIntervalTextField().sendKeys("2345");
        page.clickSaveButton();

        // then
        app.open();
        globalNavbar.getConfigurationLink().click();
        // user recording config is not accessible via config sidebar currently
        driver.navigate().to(userRecordingUrl);
        assertThat(page.getEnabledSwitchOn().getAttribute("class").split(" "))
                .doesNotContain("active");
        assertThat(page.getEnabledSwitchOff().getAttribute("class").split(" ")).contains("active");
        assertThat(page.getUserTextField().getAttribute("value")).isEqualTo("abc");
        assertThat(page.getProfileIntervalTextField().getAttribute("value")).isEqualTo("2345");
    }

    @Test
    public void shouldUpdateStorageConfig() throws Exception {
        // given
        App app = new App(driver, "http://localhost:" + container.getUiPort());
        GlobalNavbar globalNavbar = new GlobalNavbar(driver);
        ConfigSidebar configSidebar = new ConfigSidebar(driver);
        StorageConfigPage page = new StorageConfigPage(driver);

        app.open();
        globalNavbar.getConfigurationLink().click();
        configSidebar.getStorageLink().click();

        // when
        page.getAggregateExpirationTextField().clear();
        page.getAggregateExpirationTextField().sendKeys("44");
        page.getTraceExpirationTextField().clear();
        page.getTraceExpirationTextField().sendKeys("55");
        page.getGaugeExpirationTextField().clear();
        page.getGaugeExpirationTextField().sendKeys("66");
        page.getCappedDatabaseSizeTextField().clear();
        page.getCappedDatabaseSizeTextField().sendKeys("678");
        page.clickSaveButton();

        // then
        app.open();
        globalNavbar.getConfigurationLink().click();
        configSidebar.getStorageLink().click();
        assertThat(page.getAggregateExpirationTextField().getAttribute("value")).isEqualTo("44");
        assertThat(page.getTraceExpirationTextField().getAttribute("value")).isEqualTo("55");
        assertThat(page.getGaugeExpirationTextField().getAttribute("value")).isEqualTo("66");
        assertThat(page.getCappedDatabaseSizeTextField().getAttribute("value")).isEqualTo("678");
    }

    @Test
    public void shouldUpdateAdvancedConfig() throws Exception {
        // given
        App app = new App(driver, "http://localhost:" + container.getUiPort());
        GlobalNavbar globalNavbar = new GlobalNavbar(driver);
        ConfigSidebar configSidebar = new ConfigSidebar(driver);
        AdvancedConfigPage page = new AdvancedConfigPage(driver);

        app.open();
        globalNavbar.getConfigurationLink().click();
        configSidebar.getAdvancedLink().click();

        // when
        page.getImmediatePartialStoreThresholdTextField().clear();
        page.getImmediatePartialStoreThresholdTextField().sendKeys("1234");
        page.getMaxTraceEntriesPerTransactionTextField().clear();
        page.getMaxTraceEntriesPerTransactionTextField().sendKeys("2345");
        page.getMaxStackTraceSamplesPerTransactionTextField().clear();
        page.getMaxStackTraceSamplesPerTransactionTextField().sendKeys("3456");
        page.getThreadInfoCheckBox().click();
        page.getGcInfoCheckBox().click();
        page.clickSaveButton();

        // then
        app.open();
        globalNavbar.getConfigurationLink().click();
        configSidebar.getAdvancedLink().click();
        assertThat(page.getImmediatePartialStoreThresholdTextField().getAttribute("value"))
                .isEqualTo("1234");
        assertThat(page.getMaxTraceEntriesPerTransactionTextField().getAttribute("value"))
                .isEqualTo("2345");
        assertThat(page.getMaxStackTraceSamplesPerTransactionTextField().getAttribute("value"))
                .isEqualTo("3456");
        assertThat(page.getThreadInfoCheckBox().isSelected()).isFalse();
        assertThat(page.getGcInfoCheckBox().isSelected()).isFalse();
    }

    // TODO test servlet, jdbc and logger plugin config pages
}
