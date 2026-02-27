import React, { useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../api/bookingApi';

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isRegistering, setIsRegistering] = useState(false);
  const [email, setEmail] = useState('');
  const [isAdminRegister, setIsAdminRegister] = useState(false);

  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await login({ username, password });
      navigate('/bookings');
    } catch (err) {
      const message =
        err.response?.data?.message ||
        err.response?.data ||
        'Login failed. Check username/password or backend logs.';
      setError(message);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      await authApi.register({ username, password, email, admin: isAdminRegister });
      setIsRegistering(false);
      setError('Registration successful! Please login.');
    } catch (err) {
      const message =
        err.response?.data?.message ||
        err.response?.data ||
        'Registration failed. Check backend logs.';
      setError(message);
    }
  };

  return (
    <div className="auth-page">
      <div className="card auth-card">
        <h2 className="page-title">{isRegistering ? 'Register' : 'Login'}</h2>
        {error && <p className="text-error">{error}</p>}
        <form onSubmit={isRegistering ? handleRegister : handleLogin} className="form-grid">
          <div className="form-row">
            <label>Username</label>
            <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />
          </div>
          <div className="form-row">
            <label>Password</label>
            <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
          </div>
          {isRegistering && (
            <>
              <div className="form-row">
                <label>Email</label>
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
              </div>
              <div className="form-row">
                <label>Register as Admin</label>
                <input type="checkbox" checked={isAdminRegister} onChange={(e) => setIsAdminRegister(e.target.checked)} />
              </div>
            </>
          )}
          <button type="submit" className="btn">{isRegistering ? 'Register' : 'Login'}</button>
        </form>
        <button
          className="toggle-link"
          onClick={() => setIsRegistering(!isRegistering)}
        >
          {isRegistering ? 'Already have an account? Login' : 'Need an account? Register'}
        </button>
      </div>
    </div>
  );
};

export default LoginPage;
