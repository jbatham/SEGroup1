FROM node:carbon

# Create app directory
WORKDIR /app

# Install app dependencies
COPY package*.json ./

RUN npm install

COPY . .

# Expose the port used by the app
EXPOSE 5000

# Start the app
CMD [ "npm", "start" ]
