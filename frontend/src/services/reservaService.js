import axiosClient from '../api/axiosClient';

export const reservaService = {
  // obtener todas las reservas o por usuario
  getAll: async () => {
    const response = await axiosClient.get('/reservas');
    return response.data;
  },

  confirmarReserva: async (datosReserva) => {
    const response = await axiosClient.post('/reservas/confirmar', datosReserva);
    return response.data;
  },

  cancelarReserva: async (idReserva) => {
    const response = await axiosClient.post(`/reservas/${idReserva}/cancelar`);
    return response.data;
  },
};