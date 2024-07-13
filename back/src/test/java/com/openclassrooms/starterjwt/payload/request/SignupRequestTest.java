package com.openclassrooms.starterjwt.payload.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Sign up request tests")
public class SignupRequestTest {
    private static Validator validator;
    private SignupRequest signupRequest;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeEach
    public void setUp() {
        signupRequest = new SignupRequest();

    }

    public static String repeatLetter(String letter, int times) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times; i++) {
            result.append(letter);
        }
        return result.toString();
    }

    @DisplayName("Valid sign up request test")
    @Test
    public void testValidSignupRequest() {
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("test!1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertTrue(violations.isEmpty());

    }

    @DisplayName("Sign up request test with blank email")
    @Test
    public void testBlankEmail() {
        signupRequest.setEmail("");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("test!1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("ne doit pas être vide", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request test with email too long")
    @Test
    public void testLongEmail() {
        String longEmail = repeatLetter("a", 42) + "@test.com";
        signupRequest.setEmail(longEmail);
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("test!1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 0 et 50", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request test with invalid email")
    @Test
    public void testInvalidEmail() {
        signupRequest.setEmail("invalid-email");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("test!1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("doit être une adresse électronique syntaxiquement correcte", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request with blank firstname")
    @Test
    public void testBlankFirstname() {
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("test!1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("ne doit pas être vide", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request with firstname too short")
    @Test
    public void testShortFirstname() {
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("Jo");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("test!1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 3 et 20", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request with firstname too long")
    @Test
    public void testLongFirstname() {
        String longFirstname = repeatLetter("a", 21);
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName(longFirstname);
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("test!1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 3 et 20", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request with blank lastname")
    @Test
    public void testBlankLastname() {
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("");
        signupRequest.setPassword("test!1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("ne doit pas être vide", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request with lastname too short")
    @Test
    public void testShortLastname() {
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Wi");
        signupRequest.setPassword("test!1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 3 et 20", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request with lastname too long")
    @Test
    public void testLongLastname() {
        String longLastname = repeatLetter("a", 21);
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName(longLastname);
        signupRequest.setPassword("test!1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 3 et 20", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request with blank password")
    @Test
    public void testBlankPassword() {
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("ne doit pas être vide", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request with password too short")
    @Test
    public void testShortPassword() {
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("test");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 6 et 40", violations.iterator().next().getMessage());
    }

    @DisplayName("Sign up request with password too long")
    @Test
    public void testLongPassword() {
        String longPassword = repeatLetter("a", 41);
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword(longPassword);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertFalse(violations.isEmpty());
        assertEquals("la taille doit être comprise entre 6 et 40", violations.iterator().next().getMessage());
    }

    @DisplayName("Getters and setters tests")
    @Test
    public void testGettersAndSetters() {
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("test!1234");

        assertEquals("john.wick@test.com", signupRequest.getEmail());
        assertEquals("John", signupRequest.getFirstName());
        assertEquals("Wick", signupRequest.getLastName());
        assertEquals("test!1234", signupRequest.getPassword());
    }

    @DisplayName("To string test")
    @Test
    public void testToString() {
        signupRequest.setEmail("john.wick@test.com");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Wick");
        signupRequest.setPassword("test!1234");

        String expectedToString = "SignupRequest(email=john.wick@test.com, firstName=John, lastName=Wick, password=test!1234)";
        assertEquals(expectedToString, signupRequest.toString());
    }

    @DisplayName("Equals and hashcode tests")
    @Test
    public void testEqualsAndHashCode() {
        SignupRequest signupRequest1 = new SignupRequest();
        signupRequest1.setEmail("john.wick@test.com");
        signupRequest1.setFirstName("John");
        signupRequest1.setLastName("Wick");
        signupRequest1.setPassword("test!1234");

        SignupRequest signupRequest2 = new SignupRequest();
        signupRequest2.setEmail("john.wick@test.com");
        signupRequest2.setFirstName("John");
        signupRequest2.setLastName("Wick");
        signupRequest2.setPassword("test!1234");

        SignupRequest signupRequest3 = new SignupRequest();
        signupRequest3.setEmail("not.john.wick@test.com");
        signupRequest3.setFirstName("NotJohn");
        signupRequest3.setLastName("Wick");
        signupRequest3.setPassword("test!1234");

        assertEquals(signupRequest1, signupRequest2);
        assertEquals(signupRequest1.hashCode(), signupRequest2.hashCode());

        assertNotEquals(signupRequest1, signupRequest3);
        assertNotEquals(signupRequest1.hashCode(), signupRequest3.hashCode());
    }

}
