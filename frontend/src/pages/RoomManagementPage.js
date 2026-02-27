import React, { useState, useEffect } from 'react';
import { roomApi } from '../api/bookingApi';

const RoomManagementPage = () => {
  const [rooms, setRooms] = useState([]);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editRoom, setEditRoom] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    capacity: '',
    floorNumber: ''
  });

  useEffect(() => {
    fetchRooms();
  }, []);

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
    try {
      if (editRoom) {
        await roomApi.update(editRoom.id, formData);
      } else {
        await roomApi.create(formData);
      }
      setShowForm(false);
      setEditRoom(null);
      setFormData({ name: '', capacity: '', floorNumber: '' });
      fetchRooms();
    } catch (err) {
      setError('Failed to save room');
    }
  };

  const handleEdit = (room) => {
    setEditRoom(room);
    setFormData({
      name: room.name,
      capacity: room.capacity,
      floorNumber: room.floorNumber
    });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this room?')) {
      try {
        await roomApi.delete(id);
        fetchRooms();
      } catch (err) {
        setError('Failed to delete room');
      }
    }
  };

  return (
    <div>
      <h1>Room Management</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      
      <button onClick={() => {
        setShowForm(!showForm);
        setEditRoom(null);
        setFormData({ name: '', capacity: '', floorNumber: '' });
      }} style={{ marginBottom: '20px' }}>
        {showForm ? 'Cancel' : 'Add New Room'}
      </button>

      {showForm && (
        <div style={{ border: '1px solid #ccc', padding: '20px', marginBottom: '20px' }}>
          <h3>{editRoom ? 'Edit Room' : 'New Room'}</h3>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Room Name: </label>
              <input type="text" value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})} required />
            </div>
            <div>
              <label>Capacity: </label>
              <input type="number" value={formData.capacity} onChange={(e) => setFormData({...formData, capacity: e.target.value})} required min="1" />
            </div>
            <div>
              <label>Floor Number: </label>
              <input type="number" value={formData.floorNumber} onChange={(e) => setFormData({...formData, floorNumber: e.target.value})} required />
            </div>
            <button type="submit">{editRoom ? 'Update' : 'Create'}</button>
          </form>
        </div>
      )}

      <table border="1" style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>Name</th>
            <th>Capacity</th>
            <th>Floor</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {rooms.map(room => (
            <tr key={room.id}>
              <td>{room.name}</td>
              <td>{room.capacity}</td>
              <td>{room.floorNumber}</td>
              <td>
                <button onClick={() => handleEdit(room)}>Edit</button>
                <button onClick={() => handleDelete(room.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default RoomManagementPage;
