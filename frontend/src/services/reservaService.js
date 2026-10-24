import axiosClient from '../api/axiosClient';

export const reservaService = {
  // obtener todas las reservas o por usuario
  getAll: async () => {
    const response = await axiosClient.get('/reservas');
    return response.data;
  },

  // crear una nueva reserva (TRIGGER)
  crearReserva: async (reservaData) => {
    const response = await axiosClient.post('/reservas', reservaData);
    return response.data;
  },

  // Cancelar reserva
  cancelar: async (id) => {
    const response = await axiosClient.put(`/reservas/${id}/cancelar`);
    return response.data;
  },
};