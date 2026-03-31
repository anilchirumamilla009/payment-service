package com.techwave.paymentservice.cucumber;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class PaymentServiceStepDefs {

    @Autowired
    private MockMvc mockMvc;

    private boolean authenticated;
    private MvcResult lastResult;

    @Given("I am an authenticated user")
    public void iAmAnAuthenticatedUser() {
        authenticated = true;
    }

    @Given("I am not authenticated")
    public void iAmNotAuthenticated() {
        authenticated = false;
    }

    @When("I send a GET request to {string}")
    public void iSendGetRequest(String path) throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.get(path);
        if (authenticated) {
            req = req.with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER"));
        }
        lastResult = mockMvc.perform(req).andReturn();
    }

    @When("I send a PUT request to {string} with body:")
    public void iSendPutRequestWithBody(String path, String body) throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.put(path)
                .contentType(MediaType.APPLICATION_JSON).content(body);
        if (authenticated) {
            req = req.with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER"));
        }
        lastResult = mockMvc.perform(req).andReturn();
    }

    @When("I send a POST request to {string} with body:")
    public void iSendPostRequestWithBody(String path, String body) throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON).content(body);
        if (authenticated) {
            req = req.with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER"));
        }
        lastResult = mockMvc.perform(req).andReturn();
    }

    @When("I send a PATCH request to {string} with body:")
    public void iSendPatchRequestWithBody(String path, String body) throws Exception {
        MockHttpServletRequestBuilder req = MockMvcRequestBuilders.patch(path)
                .contentType(MediaType.APPLICATION_JSON).content(body);
        if (authenticated) {
            req = req.with(SecurityMockMvcRequestPostProcessors.user("admin").roles("USER"));
        }
        lastResult = mockMvc.perform(req).andReturn();
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        assertEquals(status, lastResult.getResponse().getStatus());
    }

    @And("the response body should be a non-empty JSON array")
    public void theResponseBodyShouldBeNonEmptyArray() throws Exception {
        String body = lastResult.getResponse().getContentAsString();
        assertTrue(body.startsWith("["));
        assertNotEquals("[]", body.trim());
    }

    @And("each item should have a {string} of {string}")
    public void eachItemShouldHaveFieldOf(String field, String value) throws Exception {
        String body = lastResult.getResponse().getContentAsString();
        assertTrue(body.contains("\"" + field + "\":\"" + value + "\""));
    }

    @And("the response body {string} should be {string}")
    public void theResponseBodyFieldShouldBe(String field, String value) throws Exception {
        String body = lastResult.getResponse().getContentAsString();
        assertTrue(body.contains("\"" + field + "\":\"" + value + "\""),
                "Expected field " + field + "=" + value + " in: " + body);
    }

    @And("the response body should contain an {string}")
    public void theResponseBodyShouldContainField(String field) throws Exception {
        String body = lastResult.getResponse().getContentAsString();
        assertTrue(body.contains("\"" + field + "\""));
    }

    @And("the response header {string} should be {string}")
    public void theResponseHeaderShouldBe(String header, String value) {
        assertEquals(value, lastResult.getResponse().getHeader(header));
    }

    @And("the response header {string} should exist")
    public void theResponseHeaderShouldExist(String header) {
        assertNotNull(lastResult.getResponse().getHeader(header),
                "Expected header " + header + " to be present");
    }

    @And("the response body should not contain {string}")
    public void theResponseBodyShouldNotContain(String text) throws Exception {
        String body = lastResult.getResponse().getContentAsString();
        assertFalse(body.contains(text), "Response body should not contain: " + text);
    }
}

