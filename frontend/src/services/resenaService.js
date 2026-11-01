import axiosClient from '../api/axiosClient';

export const resenaService = {
    crearResena: async (datos) => {
        // datos = { idReserva, tituloResena, comentario, calificacion }
        const response = await axiosClient.post('/resenas', datos);
        return response.data;
    },
};