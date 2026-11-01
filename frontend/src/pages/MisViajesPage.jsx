import React from 'react';

export default function MisViajesPage({ reservas, onCancelarReserva, onVolverHome }) {
  // Función auxiliar para formatear fechas
  const formatearFecha = (fechaStr) => {
    if (!fechaStr) return '';
    const fecha = new Date(fechaStr + 'T00:00:00');
    return fecha.toLocaleDateString('es-CL', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  // Color de badge según el estado de la reserva
  const getBadgeClass = (estado) => {
    switch (estado) {
      case 'CONFIRMADA':
        return 'bg-success';
      case 'PENDIENTE':
        return 'bg-warning text-dark';
      case 'CANCELADA':
        return 'bg-danger';
      case 'COMPLETADA':
        return 'bg-secondary';
      default:
        return 'bg-info text-dark';
    }
  };

  return (
    <div className="container py-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2 className="fw-bold mb-1">Mis viajes y reservas</h2>
          <p className="text-muted small mb-0">Revisa el estado de tus estadías programadas</p>
        </div>
        <button className="btn btn-outline-secondary rounded-pill btn-sm px-3" onClick={onVolverHome}>
          <i className="bi bi-arrow-left me-1"></i> Explorar más alojamientos
        </button>
      </div>

      {reservas.length === 0 ? (
        <div className="text-center py-5 border rounded-4 bg-light">
          <i className="bi bi-calendar-x fs-1 text-muted mb-3 d-block"></i>
          <h5 className="fw-bold text-dark">No tienes reservas activas</h5>
          <p className="text-muted small mb-3">Encuentra tu próximo destino en la página principal.</p>
          <button
            className="btn btn-primary rounded-pill px-4"
            style={{ backgroundColor: '#1668e3' }}
            onClick={onVolverHome}
          >
            Buscar alojamientos
          </button>
        </div>
      ) : (
        <div className="row g-4">
          {reservas.map((reserva) => (
            <div key={reserva.id_reserva} className="col-12 col-md-6">
              <div className="card border-0 shadow-sm rounded-4 overflow-hidden h-100">
                <div className="row g-0 h-100">
                  {/* Foto miniatura del alojamiento */}
                  <div className="col-4 position-relative">
                    <img
                      src={reserva.imagenUrl}
                      alt={reserva.titulo}
                      className="w-100 h-100"
                      style={{ objectFit: 'cover', minHeight: '160px' }}
                    />
                  </div>

                  {/* Detalle de la reserva */}
                  <div className="col-8">
                    <div className="card-body d-flex flex-column h-100 p-3">
                      <div className="d-flex justify-content-between align-items-start mb-2">
                        <h6 className="card-title fw-bold text-truncate mb-0" style={{ maxWidth: '70%' }}>
                          {reserva.titulo}
                        </h6>
                        <span className={`badge rounded-pill ${getBadgeClass(reserva.estado_reserva)}`}>
                          {reserva.estado_reserva}
                        </span>
                      </div>

                      <p className="text-muted small mb-2">
                        <i className="bi bi-geo-alt me-1"></i>
                        {reserva.ciudad}
                      </p>

                      <div className="bg-light p-2 rounded-3 small mb-3">
                        <div className="text-secondary">
                          <i className="bi bi-calendar3 me-1"></i>
                          {formatearFecha(reserva.fecha_inicio)} al {formatearFecha(reserva.fecha_fin)}
                        </div>
                        <div className="text-secondary mt-1">
                          <i className="bi bi-people me-1"></i>
                          {reserva.n_huespedes} huésped(es)
                        </div>
                      </div>

                      <div className="mt-auto d-flex justify-content-between align-items-center pt-2 border-top">
                        <div>
                          <span className="text-muted small d-block">Total pagado / por pagar</span>
                          <span className="fw-bold fs-6 text-dark">
                            ${Number(reserva.precio_total).toLocaleString('es-CL')}
                          </span>
                        </div>

                        {reserva.estado_reserva === 'CONFIRMADA' && (
                          <button
                            className="btn btn-outline-danger btn-sm rounded-pill px-3"
                            onClick={() => onCancelarReserva(reserva.id_reserva)}
                          >
                            Cancelar
                          </button>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}