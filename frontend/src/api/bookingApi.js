import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
});

// Add a request interceptor to include the JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const authApi = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
};

export const roomApi = {
  getAll: () => api.get('/rooms'),
  create: (roomData) => api.post('/rooms', roomData),
  update: (id, roomData) => api.put(`/rooms/${id}`, roomData),
  delete: (id) => api.delete(`/rooms/${id}`),
};

export const bookingApi = {
  getMyBookings: (status) => {
    const params = status ? { status } : {};
    return api.get('/bookings/my', { params });
  },
  create: (bookingData) => api.post('/bookings', bookingData),
  update: (id, bookingData) => api.put(`/bookings/${id}`, bookingData),
  cancel: (id) => api.post(`/bookings/${id}/cancel`),
};

export const adminApi = {
  getPending: () => api.get('/admin/pending'),
  approve: (id) => api.post(`/admin/approve/${id}`),
  reject: (id) => api.post(`/admin/reject/${id}`),
};

export default api;
