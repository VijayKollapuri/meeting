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
    <div className="page">
      <h1 className="page-title">Room Management</h1>
      {error && <p className="text-error">{error}</p>}

      <button
        className="btn"
        onClick={() => {
          setShowForm(!showForm);
          setEditRoom(null);
          setFormData({ name: '', capacity: '', floorNumber: '' });
        }}
      >
        {showForm ? 'Cancel' : 'Add New Room'}
      </button>

      {showForm && (
        <div className="card">
          <h3>{editRoom ? 'Edit Room' : 'New Room'}</h3>
          <form onSubmit={handleSubmit} className="form-grid">
            <div className="form-row">
              <label>Room Name</label>
              <input type="text" value={formData.name} onChange={(e) => setFormData({ ...formData, name: e.target.value })} required />
            </div>
            <div className="form-row">
              <label>Capacity</label>
              <input type="number" value={formData.capacity} onChange={(e) => setFormData({ ...formData, capacity: e.target.value })} required min="1" />
            </div>
            <div className="form-row">
              <label>Floor Number</label>
              <input type="number" value={formData.floorNumber} onChange={(e) => setFormData({ ...formData, floorNumber: e.target.value })} required />
            </div>
            <button type="submit" className="btn">{editRoom ? 'Update' : 'Create'}</button>
          </form>
        </div>
      )}

      <table className="data-table">
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
                <div className="table-actions">
                  <button className="btn btn-secondary" onClick={() => handleEdit(room)}>Edit</button>
                  <button className="btn btn-danger" onClick={() => handleDelete(room.id)}>Delete</button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default RoomManagementPage;
