FROM node:lts-slim AS build
WORKDIR /src
RUN npm install -g @angular/cli

COPY package*.json ./
RUN npm ci

COPY . ./
RUN ng build --configuration=production

FROM nginx:stable AS final
EXPOSE 80

# Copy custom nginx config
COPY scripts/nginx.conf /etc/nginx/nginx.conf

# Copy built Angular app
COPY --from=build /src/dist/share-food-frontend/browser /usr/share/nginx/html
