import React, { useState, useEffect } from 'react';
import { adminApi } from '../api/bookingApi';

const AdminPage = () => {
  const [pendingBookings, setPendingBookings] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchPendingBookings();
  }, []);

  const fetchPendingBookings = async () => {
    try {
      const response = await adminApi.getPending();
      setPendingBookings(response.data);
    } catch (err) {
      setError('Failed to fetch pending bookings');
    }
  };

  const handleApprove = async (id) => {
    try {
      await adminApi.approve(id);
      fetchPendingBookings();
    } catch (err) {
      setError(err.response?.data || 'Failed to approve booking');
    }
  };

  const handleReject = async (id) => {
    try {
      await adminApi.reject(id);
      fetchPendingBookings();
    } catch (err) {
      setError('Failed to reject booking');
    }
  };

  return (
    <div>
      <h1>Pending Approvals</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      
      {pendingBookings.length === 0 ? (
        <p>No pending booking requests.</p>
      ) : (
        <table border="1" style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr>
              <th>User</th>
              <th>Room</th>
              <th>Floor</th>
              <th>Start Time</th>
              <th>End Time</th>
              <th>Duration</th>
              <th>Agenda</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {pendingBookings.map(booking => (
              <tr key={booking.id}>
                <td>{booking.username}</td>
                <td>{booking.roomName}</td>
                <td>{booking.floorNumber}</td>
                <td>{new Date(booking.startTime).toLocaleString()}</td>
                <td>{new Date(booking.endTime).toLocaleString()}</td>
                <td>{booking.duration}</td>
                <td>{booking.agenda}</td>
                <td>
                  <button onClick={() => handleApprove(booking.id)} style={{ color: 'green' }}>Approve</button>
                  <button onClick={() => handleReject(booking.id)} style={{ color: 'red' }}>Reject</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default AdminPage;
