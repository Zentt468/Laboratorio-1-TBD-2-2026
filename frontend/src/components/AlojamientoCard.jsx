// src/components/AlojamientoCard.jsx
import React from 'react';

export default function AlojamientoCard({ alojamiento, onReservarClick, onEditarClick, puedeEditar }) {
  // Imagen de respaldo por si url_imagen viene vacía o nula
  const imagenFallback = 'https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?auto=format&fit=crop&w=600&q=80';
  const imagenFinal = alojamiento.imagenUrl?.trim() ? alojamiento.imagenUrl : imagenFallback;

  return (
    <div className="card h-100 border-0 shadow-sm rounded-4 overflow-hidden d-flex flex-column">
      {/* 1. Imagen con badges informativos */}
      <div className="position-relative" style={{ height: '200px' }}>
        <img
          src={imagenFinal}
          alt={alojamiento.titulo}
          className="w-100 h-100"
          style={{ objectFit: 'cover' }}
        />
        {/* Badge de ciudad */}
        <span className="position-absolute top-0 start-0 m-2 badge bg-dark bg-opacity-75 rounded-pill px-3 py-2 fw-normal">
          <i className="bi bi-geo-alt me-1"></i>
          {alojamiento.ciudad}
        </span>
        {/* Badge de tipo de alojamiento */}
        <span className="position-absolute top-0 end-0 m-2 badge bg-light text-dark shadow-sm rounded-pill px-2 py-1 small">
          {alojamiento.tipo_alojamiento}
        </span>
      </div>

      {/* 2. Cuerpo con los datos del alojamiento */}
      <div className="card-body d-flex flex-column p-3">
        {/* Título de la propiedad */}
        <h6 className="card-title fw-bold text-truncate mb-1" title={alojamiento.titulo}>
          {alojamiento.titulo}
        </h6>

        {/* Dirección */}
        <p className="text-muted small text-truncate mb-2" title={alojamiento.direccion}>
          {alojamiento.direccion}
        </p>

        {/* Capacidad y Estadía Mínima */}
        <div className="d-flex align-items-center gap-2 mb-2 text-secondary small">
          <span><i className="bi bi-people me-1"></i>Máx. {alojamiento.capacidad} pers.</span>
          <span>•</span>
           <span>Mín. {alojamiento.estadia_min} noche(s)</span>
        </div>

        {/* Política de cancelación */}
        <div className="mb-3">
          <span className="badge bg-light text-primary border border-primary-subtle fw-medium" style={{ fontSize: '0.75rem' }}>
            <i className="bi bi-shield-check me-1"></i>
            Canc. {alojamiento.politica_cancel}
          </span>
        </div>

        {/* 3. Precios y Botón de Acción */}
        <div className="mt-auto pt-2 border-top d-flex justify-content-between align-items-center">
          <div>
            <span className="fs-5 fw-bold text-dark">
              ${Number(alojamiento.precio_noche).toLocaleString('es-CL')}
            </span>
            <span className="text-muted small"> / noche</span>
          </div>
                    <div className="d-flex">
            {puedeEditar && (
              <button
                className="btn btn-outline-secondary btn-sm rounded-pill px-3 me-2"
                onClick={() => onEditarClick(alojamiento)}
              >
                Editar
              </button>
            )}
            <button
              className="btn btn-outline-primary btn-sm rounded-pill px-3 fw-semibold"
              onClick={() => onReservarClick(alojamiento)}
            >
              Reservar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}