# frontend/Dockerfile
FROM node:22-slim

WORKDIR /app

# Copiar archivos de dependencias
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar el resto del código
COPY . .

# Exponer el puerto por defecto de Vite
EXPOSE 5173

# Correr Vite exponiendo el host para que sea accesible fuera del contenedor
CMD ["npm", "run", "dev", "--", "--host"]