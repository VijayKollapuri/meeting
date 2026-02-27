# Meeting Room Booking System Architecture

## Technology Stack
- **Backend:** Java 17, Spring Boot 3.2.3, Spring Security, Spring Data JPA
- **Database:** H2 (In-memory for development)
- **Security:** JWT (JSON Web Tokens)
- **Frontend:** React 18, React Router, Axios, Date-fns

## Key Features
- **Authentication & Authorization:** Secure login/registration with role-based access control (Admin/User).
- **Room Management (Admin):** CRUD operations for meeting rooms.
- **Booking Management (User):** 
  - Create booking with room, date, and time.
  - Update booking before approval.
  - Cancel approved or pending bookings.
  - Filter bookings by status.
- **Approval Workflow (Admin):** Review pending booking requests and approve or reject them.
- **Business Logic:**
  - Prevents overlapping bookings for the same room.
  - Minimum booking duration of 10 minutes.
  - Only allows bookings in the future.

## API Endpoints
- `/api/auth/login` - POST - User login
- `/api/auth/register` - POST - User registration
- `/api/rooms` - GET/POST/PUT/DELETE - Room management
- `/api/bookings/my` - GET - User's own bookings
- `/api/bookings` - POST/PUT - Create/Update booking
- `/api/bookings/{id}/cancel` - POST - Cancel booking
- `/api/admin/pending` - GET - View pending bookings
- `/api/admin/approve/{id}` - POST - Approve booking
- `/api/admin/reject/{id}` - POST - Reject booking
