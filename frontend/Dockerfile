FROM node:18 as build

WORKDIR /app

COPY package*.json ./
RUN npm install
COPY . .

RUN npm run build

# Nginx
FROM nginx:alpine

COPY default.conf /etc/nginx/conf.d/
COPY --from=build /app/build /usr/share/nginx/html

CMD ["nginx", "-g", "daemon off;"]