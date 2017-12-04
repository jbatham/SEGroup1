FROM node:alpine

# Create app directory
WORKDIR /app

# Install app dependencies
COPY package*.json ./

RUN npm install pm2 -g
RUN npm install

COPY . .

# Expose the port used by the app
EXPOSE 5000

# Start the app
CMD pm2-docker app.js --web
