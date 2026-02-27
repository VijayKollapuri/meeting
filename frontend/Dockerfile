# Use a newer Node.js version
FROM node:18-alpine

WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application
COPY . .

# Build the application
RUN npm run build

# Install a simple web server to serve the build
RUN npm install -g serve

# Start the server
CMD ["serve", "-s", "build", "-l", "3000"]

EXPOSE 3000
