import React, { useState, useEffect } from 'react';
import { bookingApi, roomApi } from '../api/bookingApi';

const BookingPage = () => {
  const [bookings, setBookings] = useState([]);
  const [rooms, setRooms] = useState([]);
  const [statusFilter, setStatusFilter] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editBooking, setEditBooking] = useState(null);
  const [formData, setFormData] = useState({
    roomId: '',
    startTime: '',
    endTime: '',
    agenda: ''
  });
  const [error, setError] = useState('');

  useEffect(() => {
    fetchBookings();
    fetchRooms();
  }, [statusFilter]);

  const fetchBookings = async () => {
    try {
      const response = await bookingApi.getMyBookings(statusFilter);
      setBookings(response.data);
    } catch (err) {
      setError('Failed to fetch bookings');
    }
  };

  const fetchRooms = async () => {
    try {
      const response = await roomApi.getAll();
      setRooms(response.data);
    } catch (err) {
      setError('Failed to fetch rooms');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Client-side validation
    const start = new Date(formData.startTime);
    const end = new Date(formData.endTime);
    const now = new Date();
    
    if (start < now) {
      setError('Start time cannot be in the past');
      return;
    }
    
    if (end <= start) {
      setError('End time must be after start time');
      return;
    }
    
    const durationMs = end - start;
    if (durationMs < 10 * 60 * 1000) {
      setError('Minimum booking duration is 10 minutes');
      return;
    }

    try {
      if (editBooking) {
        await bookingApi.update(editBooking.id, formData);
      } else {
        await bookingApi.create(formData);
      }
      setShowForm(false);
      setEditBooking(null);
      setFormData({ roomId: '', startTime: '', endTime: '', agenda: '' });
      fetchBookings();
    } catch (err) {
      setError(err.response?.data || 'Failed to save booking');
    }
  };

  const handleEdit = (booking) => {
    setEditBooking(booking);
    setFormData({
      roomId: rooms.find(r => r.name === booking.roomName)?.id || '',
      startTime: booking.startTime.substring(0, 16),
      endTime: booking.endTime.substring(0, 16),
      agenda: booking.agenda || ''
    });
    setShowForm(true);
  };

  const handleCancel = async (id) => {
    try {
      await bookingApi.cancel(id);
      fetchBookings();
    } catch (err) {
      setError(err.response?.data || 'Failed to cancel booking');
    }
  };

  return (
    <div>
      <h1>My Bookings</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      
      <div style={{ marginBottom: '20px' }}>
        <button onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : 'Create New Booking'}
        </button>
        <span style={{ marginLeft: '20px' }}>
          Filter by Status: 
          <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
            <option value="">All</option>
            <option value="PENDING">Pending</option>
            <option value="APPROVED">Approved</option>
            <option value="REJECTED">Rejected</option>
            <option value="CANCELLED">Cancelled</option>
          </select>
        </span>
      </div>

      {showForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px' }}>
          <h3>{editBooking ? 'Edit Booking' : 'New Booking'}</h3>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Room: </label>
              <select value={formData.roomId} onChange={(e) => setFormData({...formData, roomId: e.target.value})} required>
                <option value="">Select Room</option>
                {rooms.map(room => (
                  <option key={room.id} value={room.id}>{room.name} (Cap: {room.capacity}, Floor: {room.floorNumber})</option>
                ))}
              </select>
            </div>
            <div>
              <label>Start Time: </label>
              <input type="datetime-local" value={formData.startTime} onChange={(e) => setFormData({...formData, startTime: e.target.value})} required />
            </div>
            <div>
              <label>End Time: </label>
              <input type="datetime-local" value={formData.endTime} onChange={(e) => setFormData({...formData, endTime: e.target.value})} required />
            </div>
            <div>
              <label>Agenda (Optional): </label>
              <textarea value={formData.agenda} onChange={(e) => setFormData({...formData, agenda: e.target.value})} />
            </div>
            <button type="submit">{editBooking ? 'Update' : 'Create'}</button>
          </form>
        </div>
      )}

      <table border="1" style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>Room</th>
            <th>Floor</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Duration</th>
            <th>Agenda</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {bookings.map(booking => (
            <tr key={booking.id}>
              <td>{booking.roomName}</td>
              <td>{booking.floorNumber}</td>
              <td>{new Date(booking.startTime).toLocaleString()}</td>
              <td>{new Date(booking.endTime).toLocaleString()}</td>
              <td>{booking.duration}</td>
              <td>{booking.agenda}</td>
              <td>{booking.status}</td>
              <td>
                {booking.status === 'PENDING' && (
                  <>
                    <button onClick={() => handleEdit(booking)}>Edit</button>
                    <button onClick={() => handleCancel(booking.id)}>Cancel</button>
                  </>
                )}
                {booking.status === 'APPROVED' && (
                  <button onClick={() => handleCancel(booking.id)}>Cancel</button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default BookingPage;
