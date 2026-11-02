import axiosClient from '../api/axiosClient';

export const alojamientoService = {
  //obtener todos los alojamientos
  getAll: async () => {
    const response = await axiosClient.get('/alojamientos');
    return response.data;
  },

  // buscar alojamientos disponibles 
  buscarDisponibles: async ({ ciudad, fechaInicio, fechaFin, huespedes }) => {
    const response = await axiosClient.get('/alojamientos/buscar', {
      params: { ciudad, fechaInicio, fechaFin, huespedes },
    });
    return response.data;
  },

  // obtener por ID
  getById: async (id) => {
    const response = await axiosClient.get(`/alojamientos/${id}`);
    return response.data;
  },

  // guardar un nuevo alojamiento
  save: async (alojamientoData) => {
    const response = await axiosClient.post('/alojamientos', alojamientoData);
    return response.data;
  },

   // actualizar un alojamiento (dispara el trigger de auditoría)
  update: async (alojamientoData) => {
    const response = await axiosClient.put('/alojamientos', alojamientoData);
    return response.data;
  },


};