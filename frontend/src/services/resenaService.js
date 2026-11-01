import axiosClient from '../api/axiosClient';

export const resenaService = {
    crearResena: async (datos) => {
        const response = await axiosClient.post('/resenas', datos);
        return response.data;
    },
    obtenerPorReserva: async (idReserva) => {
        const response = await axiosClient.get(`/resenas/reserva/${idReserva}`);
        return response.data; // Retorna null o vacío si es 204 No Content
    },
};