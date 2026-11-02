import axiosClient from '../api/axiosClient';

export const reporteService = {
    getOcupacion: async () => {
        const response = await axiosClient.get('/reportes/ocupacion');
        return response.data;
    },

    refrescar: async () => {
        const response = await axiosClient.post('/reportes/ocupacion/refrescar');
        return response.data;
    },
};