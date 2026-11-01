import axiosClient from '../api/axiosClient';

export const auditoriaService = {
    getAll: async () => {
        const response = await axiosClient.get('/auditorias');
        return response.data;
    },
};