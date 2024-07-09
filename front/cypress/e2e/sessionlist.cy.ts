/// <reference types="cypress" />

export default function SessionListSpec() {

    describe('Session List Component', () => {
    
        const mockSession1 = {
            id: 1,
            name: 'Yoga session 1',
            description: 'Yoga session 1 desc',
            date: '2024-07-10T20:00:00Z',
            teacher_id: 1,
            users: [1, 2],
            createdAt: '2024-07-03T00:00:00Z',
            updatedAt: '2024-07-04T00:00:00Z'
        };
    
        const mockSession2 = {
            id: 2,
            name: 'Yoga session 2',
            description: 'Yoga session 2 desc',
            date: '2024-07-11T20:00:00Z',
            teacher_id: 2,
            users: [3, 4],
            createdAt: '2024-07-03T00:00:00Z',
            updatedAt: '2024-07-04T00:00:00Z'
        };
    
        const mockSessions = [mockSession1, mockSession2];
    
        describe('Admin User', () => {
    
            const mockAdminLogin = {
                token: 'jwt',
                type: 'Bearer',
                id: 1,
                username: 'john.wick@test.com',
                firstName: 'John',
                lastName: 'Wick',
                admin: true 
            };
    
            const mockAdminUser = {
                id: 1,
                email: 'john.wick@test.com',
                lastName: 'Wick',
                firstName: 'John',
                admin: true,
                createdAt: '2024-07-02T00:00:00Z',
                updatedAt: '2024-07-03T00:00:00Z'  
            };
    
            beforeEach(() => {
                cy.visit('/login')
    
                cy.intercept('POST', '/api/auth/login', {
                    statusCode: 200,
                    body: mockAdminLogin
                });
    
                cy.intercept('GET', '/api/user/1', {
                    statusCode: 200,
                    body: mockAdminUser
                });
    
                cy.intercept('GET', '/api/session', {
                    statusCode: 200,
                    body: mockSessions
                });
    
                cy.get('input[formControlName=email]').type('john.wick@test.com');
                cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
    
                cy.url().should('include', '/sessions');
    
            });
    
            it('should show Create button', () => {
                cy.get('button[routerLink="create"]').should('be.visible');
            });
    
            it('should navigate to create form page when Create button is clicked', () => {
                cy.get('button[routerLink="create"]').click();
                cy.url().should('contain', '/sessions/create');
            });
    
            it('should list all sessions', () => {
                cy.get('mat-card.item').eq(0).should('be.visible');
                cy.get('mat-card.item').eq(1).should('be.visible');
            });
    
            it('should show session1 info', () => {
                cy.get('mat-card.item').eq(0).should('contain', mockSession1.name);
                cy.get('mat-card.item').eq(0).should('contain', 'Session on July 10, 2024');
                cy.get('mat-card.item').eq(0).should('contain', mockSession1.description);
            });
    
            it('should show session2 info', () => {
                cy.get('mat-card.item').eq(1).should('contain', mockSession2.name);
                cy.get('mat-card.item').eq(1).should('contain', 'Session on July 11, 2024');
                cy.get('mat-card.item').eq(1).should('contain', mockSession2.description);     
            });
    
            it('should show Detail and Edit button for each session', () => {
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').should('be.visible');
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Edit').should('be.visible');
                cy.get('mat-card.item').eq(1).get('span.ml1').contains('Detail').should('be.visible');
                cy.get('mat-card.item').eq(1).get('span.ml1').contains('Edit').should('be.visible');
            });
    
            it('it should navigate to detail form page when Detail button is clicked', () => {
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').click();
                cy.url().should('contain', '/sessions/detail/1');
            });
    
            it('it should navigate to update form page when Update button is clicked', () => {
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Edit').click();
                cy.url().should('contain', '/sessions/update/1');
            });
    
        });
        describe('Non-Admin User', () => {
            const mockLogin = {
                token: 'jwt',
                type: 'Bearer',
                id: 1,
                username: 'john.wick@test.com',
                firstName: 'John',
                lastName: 'Wick',
                admin: false 
            };
    
            const mockUser = {
                id: 1,
                email: 'john.wick@test.com',
                lastName: 'Wick',
                firstName: 'John',
                admin: false,
                createdAt: '2024-07-02T00:00:00Z',
                updatedAt: '2024-07-03T00:00:00Z'  
            };
    
            beforeEach(() => {
                cy.visit('/login')
    
                cy.intercept('POST', '/api/auth/login', {
                    statusCode: 200,
                    body: mockLogin
                });
    
                cy.intercept('GET', '/api/user/1', {
                    statusCode: 200,
                    body: mockUser
                });
    
                cy.intercept('GET', '/api/session', {
                    statusCode: 200,
                    body: mockSessions
                });
    
                cy.get('input[formControlName=email]').type('john.wick@test.com');
                cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
    
                cy.url().should('include', '/sessions');
    
            });
    
            it('should not show Create button', () => {
                cy.get('button[routerLink="create"]').should('not.exist');
            });
    
            it('should list all sessions', () => {
                cy.get('mat-card.item').eq(0).should('be.visible');
                cy.get('mat-card.item').eq(1).should('be.visible');
            });
    
            it('should show session1 info', () => {
                cy.get('mat-card.item').eq(0).should('contain', mockSession1.name);
                cy.get('mat-card.item').eq(0).should('contain', 'Session on July 10, 2024');
                cy.get('mat-card.item').eq(0).should('contain', mockSession1.description);
            });
    
            it('should show session2 info', () => {
                cy.get('mat-card.item').eq(1).should('contain', mockSession2.name);
                cy.get('mat-card.item').eq(1).should('contain', 'Session on July 11, 2024');
                cy.get('mat-card.item').eq(1).should('contain', mockSession2.description);     
            });
    
            it('should show Detail for each session', () => {
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').should('be.visible');
                cy.get('mat-card.item').eq(1).get('span.ml1').contains('Detail').should('be.visible');
            });
    
            it('should not show Edit button for any session', () => {
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Edit').should('not.exist');
                cy.get('mat-card.item').eq(1).get('span.ml1').contains('Edit').should('not.exist');
            });
    
            it('it should navigate to detail form page when Detail button is clicked', () => {
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').click();
                cy.url().should('contain', '/sessions/detail/1');
            });
        });
    });
}