package org.example.sw_300335322_rafaelmartins_03.web;

import jakarta.servlet.http.HttpSession;
import org.example.sw_300335322_rafaelmartins_03.entities.Account;
import org.example.sw_300335322_rafaelmartins_03.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    private Account account;

    @BeforeEach
    void setUp() throws ParseException {

        account = new Account();
        account.setId(1L);
        account.setCustomerNumber("111");
        account.setCustomerName("Jack");
        account.setCustomerDeposit(10000);
        account.setNumberOfYears(5);
        account.setSavingsType("Savings-Deluxe");

    }

    @Test
    void findAll() throws Exception {
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);
        accountList.add(account);

        when(accountRepository.findAll()).thenReturn(accountList);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("accountList", accountList))
                .andExpect(view().name("index"))
                .andExpect(model().attribute("accountList", hasSize(2)));

        // Verify that findAll() was called exactly once
        verify(accountRepository, times(1)).findAll();
        verifyNoMoreInteractions(accountRepository);
    }




//    @Mock
//    private AccountRepository accountRepository;
//
//    @Mock
//    private Model model;
//
//    @Mock
//    private BindingResult bindingResult;
//
//    @Mock
//    private HttpSession session;
//
//    @Mock
//    private RedirectAttributes redirectAttributes;
//
//    @InjectMocks
//    private Controller controller;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

//    @Test
//    public void testSave() {
//        Account account = new Account();
//        account.setCustomerNumber("1001");
//        account.setCustomerName("John Doe");
//        account.setCustomerDeposit(1000.0);
//        account.setNumberOfYears(5);
//        account.setSavingsType("Savings-Regular");
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(accountRepository.existsByCustomerNumber("1001")).thenReturn(false);
//
//        String viewName = controller.save(account, bindingResult,session);
//
//        assertEquals("redirect:/", viewName);
//        verify(accountRepository, times(1)).save(account);
//        verify(session, times(1)).removeAttribute("errorMessage");
//    }

}