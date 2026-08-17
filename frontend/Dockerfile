# ---------- Stage 1: Build ----------
FROM node:24 AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build


# ---------- Stage 2: Serve ----------
FROM nginx:alpine

COPY --from=build /app/dist/task-management-frontend/browser /usr/share/nginx/html

EXPOSE 80