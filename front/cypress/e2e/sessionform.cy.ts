/// <reference types="cypress" />

describe('Session Form Component', () => {
    const mockAdminLogin = {
        token: 'jwt',
        type: 'Bearer',
        id: 1,
        username: 'john.wick@test.com',
        firstName: 'John',
        lastName: 'Wick',
        admin: true 
    };

    const mockNonAdminLogin = {
        token: 'jwt',
        type: 'Bearer',
        id: 1,
        username: 'john.wick@test.com',
        firstName: 'John',
        lastName: 'Wick',
        admin: false 
    };

    const mockTeacher1 = {
        id: 1,
        lastName: 'Yoga1',
        firstName: 'Teacher1',
        createdAt: '2024-07-01T00:00:00Z',
        updatedAt: '2024-07-02T00:00:00Z'
    };

    const mockTeacher2 = {
        id: 2,
        lastName: 'Yoga2',
        firstName: 'Teacher2',
        createdAt: '2024-07-01T00:00:00Z',
        updatedAt: '2024-07-02T00:00:00Z'
    };

    const mockSession = {
        id: 1,
        name: 'Yoga session 1',
        description: 'Yoga session 1 desc',
        date: '2024-07-05T20:00:00Z',
        teacher_id: 1,
        users: [],
        createdAt: '2024-07-05T00:00:00Z',
        updatedAt: '2024-07-05T00:00:00Z'
    };

    const mockSessions = [mockSession];

    const mockTeachers = [mockTeacher1, mockTeacher2];

    beforeEach(() => {
        cy.intercept('POST', '/api/auth/login', {
            statusCode: 200,
            body: mockAdminLogin
        });

        cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: mockTeachers
        });
    });

    describe('Create form', () => {
        beforeEach(() => {
            cy.intercept('GET', '/api/session', {
                statusCode: 200,
                body: {}
            });

            cy.visit('/login');

            cy.get('input[formControlName=email]').type('john.wick@test.com');
            cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

            cy.url().should('contain', '/sessions');

            cy.get('button').contains('Create').click();
            cy.url().should('contain', '/sessions/create');
        });

        it('should show back button', () => {
            cy.get('button[routerLink="/sessions"]').should('be.visible');
        });

        it('should go to sessions page on Back button click', () => {
            cy.get('button[routerLink="/sessions"').click();
            cy.url().should('contain', '/sessions');
        });

        it('should show Create session as title', () => {
            cy.get('mat-card-title').should('contain', 'Create session');
        });

        it('should show 4 empty fields: Name, Date, Teacher and Description', () => {
            cy.get('input[formControlName="name"]').should('be.empty');
            cy.get('input[formControlName="date"]').should('be.empty');
            cy.get('mat-select[formControlName="teacher_id"]').should('contain', '');
            cy.get('textarea[formControlName="description"]').should('be.empty');
        });

        it('should show Save button as disabled', () => {
            cy.get('button[type="submit"]').should('be.disabled');
        });

        it('should show options when Teacher field is clicked', () => {
            cy.get('mat-form-field').contains('Teacher').click({ force: true });
            cy.get('mat-option').should('be.visible');
            cy.get('mat-option').should('contain', 'Teacher1 Yoga1');
            cy.get('mat-option').should('contain', 'Teacher2 Yoga2');
            cy.get('mat-option').eq(0).click();
            cy.get('mat-option').should('not.be.visible');
        });

        it('should disable Save button when Name field is empty', () => {
            cy.get('input[formControlName="date"]').type('2024-07-05');
            cy.get('mat-form-field').contains('Teacher').click({ force: true });
            cy.get('mat-option').should('be.visible');
            cy.get('mat-option').eq(0).click();

            cy.get('textarea[formControlName="description"]').type('Yoga Session 1 description');

            cy.get('button[type="submit"]').should('be.disabled');
        });

        it('should disable Save button when Date field is empty', () => {
            cy.get('input[formControlName="name"]').type('Yoga Session 1');
            cy.get('mat-form-field').contains('Teacher').click({ force: true });
            cy.get('mat-option').should('be.visible');
            cy.get('mat-option').eq(0).click();

            cy.get('textarea[formControlName="description"]').type('Yoga Session 1 description');

            cy.get('button[type="submit"]').should('be.disabled');
        });

        it('should disable Save button when Teacher field is empty', () => {
            cy.get('input[formControlName="name"]').type('Yoga Session 1');
            cy.get('input[formControlName="date"]').type('2024-07-05');
            cy.get('textarea[formControlName="description"]').type('Yoga Session 1 description');

            cy.get('button[type="submit"]').should('be.disabled');
        });

        it('should disable Save button when Description field is empty', () => {
            cy.get('input[formControlName="name"]').type('Yoga Session 1');
            cy.get('input[formControlName="date"]').type('2024-07-05');
            cy.get('mat-form-field').contains('Teacher').click({ force: true });
            cy.get('mat-option').should('be.visible');
            cy.get('mat-option').eq(0).click();

            cy.get('button[type="submit"]').should('be.disabled');
        });

        it('it should create a new session and navigate to sessions page when we submit a filled form', () => {
            cy.get('input[formControlName="name"]').type('Yoga Session 1');
            cy.get('input[formControlName="date"]').type('2024-07-05');
            cy.get('mat-form-field').contains('Teacher').click({ force: true });
            cy.get('mat-option').should('be.visible');
            cy.get('mat-option').eq(0).click();
            cy.get('textarea[formControlName="description"]').type('Yoga session 1 description');

            cy.intercept('POST', '/api/session', {
                statusCode: 200,
                body: mockSession
            });

            cy.intercept('GET', 'api/session', {
                statusCode: 200,
                body: mockSessions
            });

            cy.get('button[type="submit"').click();
            cy.url().should('contain', '/sessions');

            cy.get('simple-snack-bar').should('contain', 'Session created !')

            cy.get('mat-card.item').contains('Yoga session 1').should('exist');
        });
        
        it('should cancel the session creation if Back button is clicked after filling fields', () => {
            cy.get('input[formControlName="name"]').type('Yoga Session 1');
            cy.get('input[formControlName="date"]').type('2024-07-05');
            cy.get('mat-form-field').contains('Teacher').click({ force: true });
            cy.get('mat-option').should('be.visible');
            cy.get('mat-option').eq(0).click();
            cy.get('textarea[formControlName="description"]').type('Yoga session 1 description');

            cy.get('button[routerLink="/sessions"]').click();
            cy.url().should('contain', '/sessions');
            cy.get('mat-card.item').should('not.exist');
        })
    });

    describe('Update form', () => {
        beforeEach(() => {
            cy.intercept('GET', '/api/session', {
                statusCode: 200,
                body: mockSessions
            }).as('getSessions');

            cy.intercept('GET', '/api/session/1', {
                statusCode: 200,
                body: mockSession
            }).as('getSession');

            cy.visit('/login');

            cy.get('input[formControlName=email]').type('john.wick@test.com');
            cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

            cy.url().should('contain', '/sessions');

            cy.get('button').contains('Edit').click();
            cy.url().should('contain', '/sessions/update/1');
        });

        it('should show back button', () => {
            cy.get('button[routerLink="/sessions"]').should('be.visible');
        });

        it('should go to sessions page on Back button click', () => {
            cy.get('button[routerLink="/sessions"').click();
            cy.url().should('contain', '/sessions');
        });

        it('should show Update session as title', () => {
            cy.get('mat-card-title').should('contain', 'Update session');
        });

        it('should show 4 fields with current info: Name, Date, Teacher and Description', () => {
            cy.get('input[formControlName="name"]').should('have.value', mockSession.name);
            cy.get('input[formControlName="date"]').should('have.value', '2024-07-05');
            cy.get('mat-select[formControlName="teacher_id"]').should('contain', mockTeacher1.firstName + ' ' + mockTeacher1.lastName);
            cy.get('textarea[formControlName="description"]').should('have.value', mockSession.description);
        });

        it('should show Save button as enabled', () => {
            cy.get('button[type="submit"]').should('not.be.disabled');
        });
        
        it ('should go to sessions page and show a success message if we click Save with no change', () => {
            cy.intercept('PUT', '/api/session/1', {
                statusCode: 200,
                body: mockSession
            });
            cy.get('button[type="submit"]').click();
            cy.url().should('contain', '/sessions');
            cy.get('simple-snack-bar').should('contain', 'Session updated !');
        });

        it ('should not save any change if Back button is clicked', () => {
            cy.get('input[formControlName="name"]').type('Edit Yoga Session 1');

            cy.get('button[routerLink="/sessions"]').click();
            cy.url().should('contain', '/sessions');
            cy.get('mat-card.item').should('contain', mockSession.name);
        });
        it ('should update session info', () => {
            const mockSessionEdit = {
                id: 1,
                name: 'Edit Yoga Session 1',
                description: 'Edit Yoga session 1 desc',
                date: '2024-07-06T20:00:00Z',
                teacher_id: 2,
                users: [],
                createdAt: '2024-07-05T00:00:00Z',
                updatedAt: '2024-07-05T00:00:00Z'
            };

            const mockSessionsEdit = [mockSessionEdit];

            cy.intercept('PUT', '/api/session/1', {
                statusCode: 200,
                body: mockSessionEdit
            }).as('putSessionEdit');

            cy.intercept('GET', '/api/session/1', {
                statusCode: 200,
                body: mockSessionEdit
            }).as('getSessionEdit');

            cy.intercept('GET', '/api/sessions', {
                statusCode: 200,
                body: mockSessionsEdit
            }).as('getSessionsEdit');

            cy.intercept('GET', '/api/teacher/2', {
                statusCode: 200,
                body: mockTeacher2
            })
            
            cy.get('input[formControlName="name"]').clear().type('Edit Yoga Session 1');
            cy.get('input[formControlName="date"]').type('2024-07-06');
            cy.get('mat-form-field').contains('Teacher').click({ force: true });
            cy.get('mat-option').should('be.visible');
            cy.get('mat-option').eq(1).click();
            cy.get('textarea[formControlName="description"]').clear().type('Edit Yoga session 1 description');

            cy.get('button[type="submit"]').click();

            cy.url().should('contain', '/sessions');

            cy.get('simple-snack-bar').should('contain', 'Session updated !');

            cy.get('mat-card.item').get('button').contains('Detail').click();
            cy.url().should('contain', '/sessions/detail/1');

            cy.get('mat-card-title').should('contain', mockSessionEdit.name);
            cy.get('mat-card-subtitle').should('contain', mockTeacher2.firstName + ' ' + mockTeacher2.lastName.toUpperCase());
            cy.get('mat-card-content').get('span.ml1').eq(3).should('contain', 'July 6, 2024');
            cy.get('div.description').should('contain', mockSessionEdit.description);
        });
    });

    describe('Non admin user', () => {
        beforeEach(() => {
            cy.intercept('POST', '/api/auth/login', {
                statusCode: 200,
                body: mockNonAdminLogin
            }).as('postNonAdminLogin');

            cy.intercept('GET', '/api/session', {
                statusCode: 200,
                body: mockSessions
            }).as('getSessions');

            cy.intercept('GET', '/api/session/1', {
                statusCode: 200,
                body: mockSession
            }).as('getSession');

            cy.visit('/login');

            cy.get('input[formControlName=email]').type('john.wick@test.com');
            cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

            cy.url().should('contain', '/sessions');
        });

        it('should redirect to login page when trying to access create form', () => {
            cy.visit('/sessions/create');
            cy.url().should('contain', '/login');
        });

        it('should redirect to login page when trying to access update form', () => {
            cy.visit('/sessions/update/1');
            cy.url().should('contain', '/login');
        });
    });
});