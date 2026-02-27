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
    <nav style={{ padding: '10px', backgroundColor: '#f0f0f0', marginBottom: '20px' }}>
      <Link to="/bookings" style={{ marginRight: '10px' }}>My Bookings</Link>
      {isAdmin() && (
        <>
          <Link to="/admin" style={{ marginRight: '10px' }}>Approve Bookings</Link>
          <Link to="/rooms" style={{ marginRight: '10px' }}>Manage Rooms</Link>
        </>
      )}
      <span style={{ marginLeft: 'auto' }}>Welcome, {user.username} </span>
      <button onClick={logout}>Logout</button>
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
        <div className="App">
          <Navigation />
          <div style={{ padding: '20px' }}>
            <Routes>
              <Route path="/login" element={<LoginPage />} />
              <Route path="/bookings" element={<PrivateRoute><BookingPage /></PrivateRoute>} />
              <Route path="/admin" element={<PrivateRoute adminOnly={true}><AdminPage /></PrivateRoute>} />
              <Route path="/rooms" element={<PrivateRoute adminOnly={true}><RoomManagementPage /></PrivateRoute>} />
              <Route path="/" element={<Navigate to="/bookings" />} />
            </Routes>
          </div>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
