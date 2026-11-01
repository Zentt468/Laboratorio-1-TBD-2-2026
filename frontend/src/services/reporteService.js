import axiosClient from '../api/axiosClient';

export const reporteService = {
    getOcupacion: async () => {
        const response = await axiosClient.get('/reportes/ocupacion');
        return response.data;
    },
};