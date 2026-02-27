import React, { useContext } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, Link } from 'react-router-dom';
import { AuthProvider, AuthContext } from './context/AuthContext';
import LoginPage from './pages/LoginPage';
import BookingPage from './pages/BookingPage';
import AdminPage from './pages/AdminPage';
import RoomManagementPage from './pages/RoomManagementPage';

const Navigation = () => {
  const { user, logout, isAdmin } = useContext(AuthContext);

  if (!user) return null;

  return (
    <nav className="app-nav">
      <div className="app-nav__inner">
        <div className="app-nav__left">
          <Link to="/bookings" className="nav-link">My Bookings</Link>
          {isAdmin() && (
            <>
              <Link to="/admin" className="nav-link">Approve Bookings</Link>
              <Link to="/rooms" className="nav-link">Manage Rooms</Link>
            </>
          )}
        </div>
        <div className="app-nav__right">
          <span className="nav-welcome">Welcome, {user.username}</span>
          <button className="btn btn-ghost" onClick={logout}>Logout</button>
        </div>
      </div>
    </nav>
  );
};

const PrivateRoute = ({ children, adminOnly = false }) => {
  const { user, isAdmin, loading } = useContext(AuthContext);

  if (loading) return <div>Loading...</div>;
  if (!user) return <Navigate to="/login" />;
  if (adminOnly && !isAdmin()) return <Navigate to="/bookings" />;

  return children;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="app-shell">
          <Navigation />
          <main className="app-main">
            <Routes>
              <Route path="/login" element={<LoginPage />} />
              <Route path="/bookings" element={<PrivateRoute><BookingPage /></PrivateRoute>} />
              <Route path="/admin" element={<PrivateRoute adminOnly={true}><AdminPage /></PrivateRoute>} />
              <Route path="/rooms" element={<PrivateRoute adminOnly={true}><RoomManagementPage /></PrivateRoute>} />
              <Route path="/" element={<Navigate to="/bookings" />} />
            </Routes>
          </main>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
