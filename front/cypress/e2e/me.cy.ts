/// <reference types="cypress" />

export default function MeSpec() {
    describe('Me Component', () => {
        describe('Admin User', () => {
            beforeEach(() => {
                cy.visit('/login');
                cy.intercept('POST', 'api/auth/login', {
                    statusCode: 200,
                    body: {
                        token: 'jwt',
                        type: 'Bearer',
                        id: 1,
                        username: 'john.wick@test.com',
                        firstName: 'John',
                        lastName: 'Wick',
                        admin: true
                    }
                }).as('LoginSuccess');

                cy.get('input[formControlName=email]').type('john.wick@test.com');
                cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

                cy.url().should('include', '/sessions');

                cy.intercept('GET', '/api/user/1', {
                    body: {
                        id: 1,
                        email: 'john.wick@test.com',
                        lastName: 'Wick',
                        firstName: 'John',
                        admin: true,
                        createdAt: '2024-07-02T00:00:00Z',
                        updatedAt: '2024-07-03T00:00:00Z'
                    }
                }).as("GetAdminUser");

                cy.get('span.link').contains('Account').click();
            });

            it('should show User information title', () => {
                cy.get('mat-card-title').should('contain', 'User information');
            });
            
            it('should show user information on init', () => {
                cy.get('p').eq(0).should('contain', 'Name: John WICK');
                cy.get('p').eq(1).should('contain', 'Email: john.wick@test.com');
                cy.get('p').eq(3).should('contain', 'Create at:  July 2, 2024');
                cy.get('p').eq(4).should('contain', 'Last update:  July 3, 2024');
            });

            it('should show You are admin', () => {
                cy.get('p').eq(2).should('contain', 'You are admin');
            });

            it('should not show delete button', () => {
                cy.get('button[mat-raised-button]').should('not.exist');
            });

            it('should go back if back button is clicked', () => {
                cy.get('button[mat-icon-button]').click();
                cy.url().should('not.contain', '/me');
            });
        }); 

        describe('Non-Admin User', () => {
            beforeEach(() => {
                cy.visit('/login');
                cy.intercept('POST', 'api/auth/login', {
                    statusCode: 200,
                    body: {
                        token: 'jwt',
                        type: 'Bearer',
                        id: 1,
                        username: 'john.wick@test.com',
                        firstName: 'John',
                        lastName: 'Wick',
                        admin: false
                    }
                }).as('LoginSuccess');

                cy.get('input[formControlName=email]').type('john.wick@test.com');
                cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

                cy.url().should('include', '/sessions');

                cy.intercept('GET', '/api/user/1', {
                    body: {
                        id: 1,
                        email: 'john.wick@test.com',
                        lastName: 'Wick',
                        firstName: 'John',
                        admin: false,
                        createdAt: '2024-07-02T00:00:00Z',
                        updatedAt: '2024-07-03T00:00:00Z'
                    }
                }).as('GetNonAdminUser');

                cy.get('span.link').contains('Account').click();
            });

            it('it should show User information title', () => {
                cy.get('mat-card-title').should('contain', 'User information');
            });

            it('should show user information on init', () => {
                cy.get('p').eq(0).should('contain', 'Name: John WICK');
                cy.get('p').eq(1).should('contain', 'Email: john.wick@test.com');
                cy.get('p').eq(3).should('contain', 'Create at:  July 2, 2024');
                cy.get('p').eq(4).should('contain', 'Last update:  July 3, 2024');
            });

            it('should not show You are admin', () => {
                cy.get('mat-card-content').should('not.contain', 'You are admin');
            });

            it('should show Delete button', () => {
                cy.get('button[mat-raised-button').should('be.visible');
            });

            it('it should delete user account and log out when Delete button is clicked', () => {
                cy.intercept('DELETE', '/api/user/1', {
                    statusCode: 200,
                    body: {}
                }).as('Delete');
                
                cy.get('button[mat-raised-button]').click();
                cy.get('simple-snack-bar').should('contain', 'Your account has been deleted !');
                cy.url().should('eq', 'http://localhost:4200/'); 

            });

            it('should go back if Back button is clicked', () => {
                cy.get('button[mat-icon-button]').click();
                cy.url().should('not.contain', '/me');
            });
        });    
    });
}