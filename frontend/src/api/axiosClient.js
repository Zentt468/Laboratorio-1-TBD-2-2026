import axios from 'axios';

const axiosClient = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosClient.interceptors.request.use(async (config) => {
  if (keycloak && keycloak.authenticated) {
    try {
      // Renueva el token si le quedan menos de 30 segundos de vida
      await keycloak.updateToken(30);
      config.headers.Authorization = `Bearer ${keycloak.token}`;
    } catch (error) {
      console.error('Error al renovar token para axios:', error);
    }
  }
  return config;
});

export default axiosClient;