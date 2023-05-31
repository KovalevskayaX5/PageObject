package ru.netology.web.test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TransactionTests {
    @BeforeEach
    public void openPage() {
        open("http://localhost:9999");

        var loginPage = new LoginPageV2();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }


    @Test
    void shouldTransferMoneyBetweenCardsFirst() {

        var dashboardPage = new DashboardPage();

        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        var moneyTransfer = dashboardPage.firstCardButton();
        var infoCard = DataHelper.getSecondCardNumber();
        String sum = "300";
        moneyTransfer.transactionForm(sum, infoCard);

        assertEquals(balanceFirstCard + Integer.parseInt(sum), dashboardPage.getFirstCardBalance());
        assertEquals(balanceSecondCard - Integer.parseInt(sum), dashboardPage.getSecondCardBalance());
    }

    @Test
    void shouldTransferMoneyBetweenCardsSecond() {

        var dashboardPage = new DashboardPage();

        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        var moneyTransfer = dashboardPage.secondCardButton();
        var infoCard = DataHelper.getFirstCardNumber();
        String sum = "1000";
        moneyTransfer.transactionForm(sum, infoCard);

        assertEquals(balanceSecondCard + Integer.parseInt(sum), dashboardPage.getSecondCardBalance());
        assertEquals(balanceFirstCard - Integer.parseInt(sum), dashboardPage.getFirstCardBalance());
    }

    @Test
    void shouldCancelMoneyTransferFirst() {

        var dashboardPage = new DashboardPage();

        var moneyTransfer = dashboardPage.firstCardButton();
        moneyTransfer.cancelButton();
    }

    @Test
    void shouldCancelMoneyTransferSecond() {

        var dashboardPage = new DashboardPage();

        var moneyTransfer = dashboardPage.secondCardButton();
        moneyTransfer.cancelButton();
    }

    @Test
    void shouldTransferMoneyBetweenCardsError() {

        var dashboardPage = new DashboardPage();

        var moneyTransfer = dashboardPage.secondCardButton();
        var infoCard = DataHelper.getFirstCardNumber();
        String sum = "30000";
        moneyTransfer.transactionForm(sum, infoCard);
       moneyTransfer.getError();
    }
}