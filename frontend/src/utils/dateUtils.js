import React from 'react';
import { Link } from 'react-router-dom';

const HomePage = () => {
  return (
    <div style={{ maxWidth: 900, margin: '0 auto', padding: 24 }}>
      <h1>Meeting Room Booking</h1>

      <p style={{ marginTop: 8 }}>
        Book rooms, manage your reservations, and (if you’re an admin) approve or reject requests.
      </p>

      <div style={{ marginTop: 16, display: 'flex', gap: 12, flexWrap: 'wrap' }}>
        <Link to="/login">
          <button type="button">Login / Register</button>
        </Link>

        <Link to="/bookings">
          <button type="button">My Bookings</button>
        </Link>

        <Link to="/rooms">
          <button type="button">Room Management</button>
        </Link>

        <Link to="/admin">
          <button type="button">Admin Approvals</button>
        </Link>
      </div>

      <hr style={{ margin: '24px 0' }} />

      <h3>Quick tips</h3>
      <ul>
        <li>Create and update bookings from “My Bookings”.</li>
        <li>Admins can approve/reject from “Admin Approvals”.</li>
        <li>Rooms can be created/edited in “Room Management”.</li>
      </ul>
    </div>
  );
};

export default HomePage;