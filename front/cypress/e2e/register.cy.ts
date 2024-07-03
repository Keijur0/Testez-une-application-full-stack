/// <reference types="cypress" />

describe('Register spec', () => {

  beforeEach(() => {
    cy.visit('/register');
  });

  it('should display register form', () => {
    cy.get('input[formControlName=firstName]').should('be.visible');
    cy.get('input[formControlName=lastName]').should('be.visible');
    cy.get('input[formControlName=email]').should('be.visible');
    cy.get('input[formControlName=password]').should('be.visible');
  });

  it('should disable submit button if firstname is empty', () => {
    cy.get('input[formControlName=lastName]').type('test');
    cy.get('input[formControlName=email').type('test@test');
    cy.get('input[formControlName=password]').type('test!1234');

    cy.get('button[type=submit').should('be.disabled');
  });

  it('should disable submit button if lastname is empty', () => {
    cy.get('input[formControlName=firstName]').type('test');
    cy.get('input[formControlName=email').type('test@test');
    cy.get('input[formControlName=password]').type('test!1234');

    cy.get('button[type=submit').should('be.disabled');
  });

  it('should disable submit button if email is empty', () => {
    cy.get('input[formControlName=firstName').type('test');
    cy.get('input[formControlName=lastName]').type('test');
    cy.get('input[formControlName=password]').type('test!1234');

    cy.get('button[type=submit').should('be.disabled');
  });

  it('should disable submit button if password is empty', () => {
    cy.get('input[formControlName=firstName]').type('test');
    cy.get('input[formControlName=lastName]').type('test');
    cy.get('input[formControlName=email').type('test@test');

    cy.get('button[type=submit').should('be.disabled');
  });

  it('should disable submit button if email has an invalid format', () => {
    cy.get('input[formControlName=firstName]').type('test');
    cy.get('input[formControlName=lastName]').type('test');
    cy.get('input[formControlName=email').type('invalid-email');
    cy.get('input[formControlName=password]').type('test!1234');

    cy.get('button[type=submit').should('be.disabled');
  });

  it('should navigate to login page on successful registration', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {}
    }).as('register');

    cy.get('input[formControlName=firstName]').type('test');
    cy.get('input[formControlName=lastName]').type('test');
    cy.get('input[formControlName=email]').type('testuser@test.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    cy.wait('@register');

    cy.url().should('include', '/login');
  });

  it('should display an error message on failed registration', () => {
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 400,
      body: { message: 'Bad Request' }
    }).as('register');

    cy.get('input[formControlName=firstName]').type('test');
    cy.get('input[formControlName=lastName]').type('test');
    cy.get('input[formControlName=email]').type('testuser@test.com');
    cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

    cy.wait('@register');

    cy.get('.error').should('be.visible');
  });

});