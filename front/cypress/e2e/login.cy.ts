/// <reference types="cypress" />

describe('Login spec', () => {

  beforeEach(() => {
    cy.visit('/login');
  });

  it('should display login form', () => {
    cy.get('input[formControlName=email]').should('be.visible');
    cy.get('input[formControlName=password]').should('be.visible');
  });

  it('should display an error message on failed login', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'Unauthorized' }
    })

    cy.get('input[formControlName=email]').type('invaliduser@test.com');
    cy.get('input[formControlName=password]').type('invalidpassword{enter}{enter}');

    cy.get('.error').should('be.visible');

  });

  it('should navigate to sessions page on successful login', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('should disable submit button if the email has an invalid format', () => {
    cy.get('input[formControlName=email]').type('invalid-email');
    cy.get('input[formControlName=password').type('test!1234');

    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should disable submit button if the password is empty', () => {
    cy.get('input[formControlName=email]').type('yoga@studio.com');

    cy.get('button[type=submit]').should('be.disabled');
  });

});