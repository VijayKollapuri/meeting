import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import LoginPage from '../pages/LoginPage';

const mockLogin = jest.fn();

const renderLoginPage = () => {
  return render(
    <AuthContext.Provider value={{ login: mockLogin }}>
      <BrowserRouter>
        <LoginPage />
      </BrowserRouter>
    </AuthContext.Provider>
  );
};

test('renders login page with username and password fields', () => {
  renderLoginPage();
  expect(screen.getByText(/Username:/i)).toBeInTheDocument();
  expect(screen.getByText(/Password:/i)).toBeInTheDocument();
  expect(screen.getByRole('button', { name: /Login/i })).toBeInTheDocument();
});

test('toggles between login and register modes', () => {
  renderLoginPage();
  const toggleButton = screen.getByText(/Need an account\? Register/i);
  fireEvent.click(toggleButton);
  
  expect(screen.getByRole('heading', { name: /Register/i })).toBeInTheDocument();
  expect(screen.getByText(/Email:/i)).toBeInTheDocument();
  expect(screen.getByText(/Already have an account\? Login/i)).toBeInTheDocument();
});
