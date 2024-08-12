package org.example.sw_300335322_rafaelmartins_03.web;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.sw_300335322_rafaelmartins_03.dto.InvestmentProjection;
import org.example.sw_300335322_rafaelmartins_03.entities.Account;
import org.example.sw_300335322_rafaelmartins_03.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class Controller {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping(path = "/")
    public String displayIndexPage(Model model, HttpSession session) {

        List<Account> accountList = accountRepository.findAll();
        model.addAttribute("accountList", accountList);

        // Check for error message in session
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "index";
    }

    @GetMapping("/addEntry")
    public String addEntry(Model model) {

        model.addAttribute("account", new Account());

        return "addEntry";

    }

    @PostMapping(path="/save")
    public String save(Account account, BindingResult
            bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "addEntry";
        }

        // Add error message if user tries to add an existing customerNumber
        if(accountRepository.existsByCustomerNumber(account.getCustomerNumber())) {
            session.setAttribute("errorMessage", "The record you are trying to add is already existing. Choose a different customer number.");
            return "redirect:/";
        }

        accountRepository.save(account);

        // Remove the error message from the session after a successful save
        session.removeAttribute("errorMessage");

        return "redirect:/";
    }

    @GetMapping("/editEntry")
    public String editEntry(Model model, HttpSession session, Long id) {

        Account account =accountRepository.findById(id).orElse(null);

        if (account == null) throw new RuntimeException("Account not found");

        model.addAttribute("account", account);

        return "editEntry";

    }

    @PostMapping("/saveEdit")
    public String saveEdit(Account account, BindingResult bindingResult, @RequestParam("accountId")
                           Long accountId, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "editEntry";
        }

        // Ensure we update the existing account rather than creating a new one
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        existingAccount.setCustomerNumber(account.getCustomerNumber());
        existingAccount.setCustomerName(account.getCustomerName());
        existingAccount.setCustomerDeposit(account.getCustomerDeposit());
        existingAccount.setNumberOfYears(account.getNumberOfYears());
        existingAccount.setSavingsType(account.getSavingsType());

        accountRepository.save(existingAccount);

        // Remove the error message from the session after a successful edit
        session.removeAttribute("errorMessage");

        return "redirect:/";
    }

    @GetMapping("/delete")
    public String delete(Long id) {

        accountRepository.deleteById(id);

        return "redirect:/";

    }

    @GetMapping("/projectedInvestment")
    public String projectedInvestment(Model model, @RequestParam Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<InvestmentProjection> projections = calculateProjections(account);

        model.addAttribute("account", account);
        model.addAttribute("projections", projections);

        return "projectedInvestment";
    }

    private List<InvestmentProjection> calculateProjections(Account account) {

        double rate = account.getSavingsType().equals("Savings-Deluxe") ? 0.15 : 0.10;
        double amount = account.getCustomerDeposit();
        int years = account.getNumberOfYears();

        List<InvestmentProjection> projections = new ArrayList<>();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

        for (int i = 1; i <= years; i++) {
            double interest = amount * rate;
            double endingBalance = amount + interest;
            projections.add(new InvestmentProjection(i, currencyFormatter.format(amount), currencyFormatter.format(interest), currencyFormatter.format(endingBalance)));
            amount = endingBalance;
        }

        return projections;
    }

}