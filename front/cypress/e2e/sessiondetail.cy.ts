/// <reference types="cypress" />

export default function SessionDetailSpec() {
    describe('Session Detail Component', () => {
    
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
    
        const mockTeacher1 = {
            id: 1,
            lastName: 'Yoga1',
            firstName: 'Teacher1',
            createdAt: '2024-07-01T00:00:00Z',
            updatedAt: '2024-07-02T00:00:00Z'
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
    
                cy.intercept('GET', '/api/teacher/1', {
                    statusCode: 200,
                    body: mockTeacher1
                });
    
                cy.intercept('GET', '/api/session', {
                    statusCode: 200,
                    body: mockSessions
                });
    
                cy.intercept('GET', '/api/session/1', {
                    statusCode: 200,
                    body: mockSession1
                });
    
                cy.intercept('DELETE', '/api/session/1', {
                    statusCode: 200,
                    body: {}
                })
    
                cy.get('input[formControlName=email]').type('john.wick@test.com');
                cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
    
                cy.url().should('include', '/sessions');
    
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').click();
                cy.url().should('contain', '/sessions/detail/1');
    
            });
    
            it('should go back when Back button is clicked', () => {
                cy.get('button[mat-icon-button]').contains('arrow_back').click();
                cy.url().should('not.contain', '/sessions/detail/1');
            });
    
            it('should show the name of the session as title', () => {
                cy.get('mat-card-title').get('h1').should('contain', 'Yoga Session 1');
            });
    
            it('should show teacher name', () => {
                cy.get('mat-card-subtitle').get('span.ml1').should('contain', mockTeacher1.firstName + ' ' + mockTeacher1.lastName.toUpperCase());
            });
    
            it('should show delete button', () => {
                cy.get('button[mat-raised-button]').get('span.ml1').contains('Delete').should('be.visible');
            });
    
            it('should delete the session and navigate to sessions list', () => {
                cy.get('button[mat-raised-button]').get('span.ml1').contains('Delete').click();
                cy.get('simple-snack-bar').should('contain', 'Session deleted !');
                cy.url().should('contain', '/sessions');
            });
            
            it('should not show Participate button', () => {
                cy.get('button').contains('Participate').should('not.exist');
            });
            
            it('should not show Do not participate button', () => {
                cy.get('button').contains('Do not participate').should('not.exist');
            });    
    
            it('should show session info', () => {
                cy.get('mat-card-content').get('span.ml1').contains('attendees').should('contain', mockSession1.users.length + ' attendees');
                cy.get('mat-card-content').get('span.ml1').eq(3).should('contain', 'July 10, 2024');
                cy.get('mat-card-content').get('div.description').should('contain', mockSession1.description);
                cy.get('mat-card-content').get('div.created').should('contain', 'Create at:  July 3, 2024');
                cy.get('mat-card-content').get('div.updated').should('contain', 'Last update:  July 4, 2024');
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
    
                cy.intercept('GET', '/api/teacher/1', {
                    statusCode: 200,
                    body: mockTeacher1
                });
    
                cy.intercept('GET', '/api/session', {
                    statusCode: 200,
                    body: mockSessions
                });
    
                cy.intercept('GET', '/api/session/1', {
                    statusCode: 200,
                    body: mockSession1
                });
    
                cy.get('input[formControlName=email]').type('john.wick@test.com');
                cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
    
                cy.url().should('include', '/sessions');
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').click();
                cy.url().should('contain', '/sessions/detail/1');
    
            });
    
            it('should go back when Back button is clicked', () => {
                cy.get('button[mat-icon-button]').contains('arrow_back').click();
                cy.url().should('not.contain', '/sessions/detail/1');
            });
    
            it('should show the name of the session as title', () => {
                cy.get('mat-card-title').get('h1').should('contain', 'Yoga Session 1');
            });
    
            it('should show teacher name', () => {
                cy.get('mat-card-subtitle').get('span.ml1').should('contain', mockTeacher1.firstName + ' ' + mockTeacher1.lastName.toUpperCase());
            });
    
            it('should not show delete button', () => {
                cy.get('button[mat-raised-button]').get('span.ml1').contains('Delete').should('not.exist');
            });
            
            it('should show Do not participate Button', () => {
                cy.get('button').contains('Do not participate').should('be.visible');
            });
    
            it('should show session info', () => {
                cy.get('mat-card-content').get('span.ml1').contains('attendees').should('contain', mockSession1.users.length + ' attendees');
                cy.get('mat-card-content').get('span.ml1').eq(3).should('contain', 'July 10, 2024');
                cy.get('mat-card-content').get('div.description').should('contain', mockSession1.description);
                cy.get('mat-card-content').get('div.created').should('contain', 'Create at:  July 3, 2024');
                cy.get('mat-card-content').get('div.updated').should('contain', 'Last update:  July 4, 2024');
    
            });
        });
    
        describe('User participation/unparticipation', () => {
    
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
    
            const mockSession1WithoutUser = {
                id: 1,
                name: 'Yoga session 1',
                description: 'Yoga session 1 desc',
                date: '2024-07-10T20:00:00Z',
                teacher_id: 1,
                users: [2],
                createdAt: '2024-07-03T00:00:00Z',
                updatedAt: '2024-07-04T00:00:00Z'
            }
    
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
    
                cy.intercept('GET', '/api/teacher/1', {
                    statusCode: 200,
                    body: mockTeacher1
                });
    
                cy.intercept('GET', '/api/session', {
                    statusCode: 200,
                    body: mockSessions
                });
    
                cy.intercept('POST', '/api/session/1/participate/1', {
                    statusCode: 200,
                    body: {}
                });
    
                cy.intercept('DELETE', '/api/session/1/participate/1', {
                    statusCode: 200,
                    body: {}
                });
    
                cy.get('input[formControlName=email]').type('john.wick@test.com');
                cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');
    
                cy.url().should('include', '/sessions');
            });
    
            it('it should cancel participation and update info', () => {
                cy.intercept('GET', '/api/session/1', {
                    statusCode: 200,
                    body: mockSession1
                });
    
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').click();
                cy.url().should('contain', '/sessions/detail/1');
    
                cy.intercept('GET', '/api/session/1', {
                    statusCode: 200,
                    body: mockSession1WithoutUser
                });
    
                cy.get('button').contains('Do not participate').click();
    
                cy.get('button').contains('Participate').should('be.visible');
                cy.get('mat-card-content').get('span.ml1').contains('attendees').should('contain', mockSession1WithoutUser.users.length + ' attendees');
            });
    
            it('it should add participation and update info', () => {
                cy.intercept('GET', '/api/session/1', {
                    statusCode: 200,
                    body: mockSession1WithoutUser
                });
    
                cy.get('mat-card.item').eq(0).get('span.ml1').contains('Detail').click();
                cy.url().should('contain', '/sessions/detail/1');
    
                cy.intercept('GET', '/api/session/1', {
                    statusCode: 200,
                    body: mockSession1
                });
    
                cy.get('button').contains('Participate').click();
    
                cy.get('button').contains('Do not participate').should('be.visible');
                cy.get('mat-card-content').get('span.ml1').contains('attendees').should('contain', mockSession1.users.length + ' attendees');
            });
        });
    });
}