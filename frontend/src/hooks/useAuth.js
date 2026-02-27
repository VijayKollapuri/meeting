import React, { useContext } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';

import { AuthProvider, AuthContext } from './context/AuthContext';
import AppLayout from './components/AppLayout';
import ProtectedRoute from './components/ProtectedRoute';

import LoginPage from './pages/LoginPage';
import BookingPage from './pages/BookingPage';
import AdminPage from './pages/AdminPage';
import RoomManagementPage from './pages/RoomManagementPage';

function AppFrame() {
  const { user, logout, isAdmin } = useContext(AuthContext);

  return (
    <AppLayout user={user} isAdmin={isAdmin()} onLogout={logout}>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route
          path="/bookings"
          element={
            <ProtectedRoute>
              <BookingPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/admin"
          element={
            <ProtectedRoute adminOnly>
              <AdminPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/rooms"
          element={
            <ProtectedRoute adminOnly>
              <RoomManagementPage />
            </ProtectedRoute>
          }
        />

        <Route path="/" element={<Navigate to="/bookings" replace />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AppLayout>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <AppFrame />
      </BrowserRouter>
    </AuthProvider>
  );
}