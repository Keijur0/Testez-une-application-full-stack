import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  const mockUser: SessionInformation = {
    token: '123',
    type: 'user',
    id: 1,
    username: 'johnwick',
    firstName: 'John',
    lastName: 'Wick',
    admin: false
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should log in a user', () => {
    service.logIn(mockUser);

    expect(service.isLogged).toBe(true) ;
    expect(service.sessionInformation).toEqual(mockUser);
  });

  it('should log out a user', () => {
    service.logOut();

    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('it should show the correct session state', (done) => {
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      done();
    });
    service.logIn(mockUser);
  });

});
