import React, { useState, useMemo } from 'react';

export default function ReservaModal({ alojamiento, onClose, onConfirmarReserva, idUsuarioActual }) {
  const [fechaInicio, setFechaInicio] = useState('');
  const [fechaFin, setFechaFin] = useState('');
  const [huespedes, setHuespedes] = useState(1);
  const [error, setError] = useState('');
  const [cargando, setCargando] = useState(false);
  const [tipoPago, setTipoPago] = useState('DEBITO');

  // Cálculo de noches y total
  const calculoEstadia = useMemo(() => {
    if (!fechaInicio || !fechaFin) return { noches: 0, total: 0 };

    const inicio = new Date(fechaInicio);
    const fin = new Date(fechaFin);
    const diferenciaMs = fin - inicio;
    const noches = Math.ceil(diferenciaMs / (1000 * 60 * 60 * 24));

    if (noches <= 0) return { noches: 0, total: 0 };

    const total = noches * Number(alojamiento.precio_noche || alojamiento.precioNoche || 0);
    return { noches, total };
  }, [fechaInicio, fechaFin, alojamiento]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!fechaInicio || !fechaFin) {
      setError('Debes seleccionar ambas fechas.');
      return;
    }

    if (calculoEstadia.noches <= 0) {
      setError('La fecha de salida debe ser posterior a la de entrada.');
      return;
    }

    const datosReserva = {
      idUsuario: idUsuarioActual,
      idAlojamiento: alojamiento.id_alojamiento,
      fechaInicio: fechaInicio,
      fechaFin: fechaFin,
      cantidadHuespedes: Number(huespedes),
      tipoPago: tipoPago,
    };

    try {
      setCargando(true);
      await onConfirmarReserva(datosReserva);
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Error al procesar la reserva';
      setError(typeof msg === 'string' ? msg : JSON.stringify(msg));
    } finally {
      setCargando(false);
    }
  };

  return (
      <div
          className="modal show d-block"
          tabIndex="-1"
          style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1050 }}
      >
        <div className="modal-dialog modal-dialog-centered">
          <div className="modal-content rounded-4 border-0 shadow">
            {/* Cabecera del modal */}
            <div className="modal-header border-bottom-0 pb-0">
              <h5 className="modal-title fw-bold">Confirmar reserva</h5>
              <button
                  type="button"
                  className="btn-close shadow-none"
                  onClick={onClose}
                  disabled={cargando}
              ></button>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                {/* Resumen del alojamiento seleccionado */}
                <div className="d-flex gap-3 align-items-center p-2 mb-3 bg-light rounded-3">
                  {alojamiento.url_imagen && (
                      <img
                          src={alojamiento.url_imagen}
                          alt={alojamiento.titulo}
                          className="rounded-3"
                          style={{ width: '70px', height: '70px', objectFit: 'cover' }}
                      />
                  )}
                  <div className="overflow-hidden">
                    <h6 className="fw-bold mb-0 text-truncate">{alojamiento.titulo}</h6>
                    <p className="text-muted small mb-0">{alojamiento.ciudad} • {alojamiento.tipo_alojamiento || 'Alojamiento'}</p>
                    <span className="small text-primary fw-semibold">
                    ${Number(alojamiento.precio_noche || alojamiento.precioNoche || 0).toLocaleString('es-CL')} / noche
                  </span>
                  </div>
                </div>

                {error && (
                    <div className="alert alert-danger py-2 small" role="alert">
                      <i className="bi bi-exclamation-triangle-fill me-2"></i>
                      {error}
                    </div>
                )}

                {/* Campos de fechas */}
                <div className="row g-2 mb-3">
                  <div className="col-6">
                    <label className="form-label small fw-semibold text-muted">Llegada</label>
                    <input
                        type="date"
                        className="form-control"
                        value={fechaInicio}
                        onChange={(e) => setFechaInicio(e.target.value)}
                        required
                    />
                  </div>
                  <div className="col-6">
                    <label className="form-label small fw-semibold text-muted">Salida</label>
                    <input
                        type="date"
                        className="form-control"
                        value={fechaFin}
                        onChange={(e) => setFechaFin(e.target.value)}
                        required
                    />
                  </div>
                </div>

                {/* Huéspedes */}
                <div className="mb-3">
                  <label className="form-label small fw-semibold text-muted">Huéspedes</label>
                  <input
                      type="number"
                      min="1"
                      max={alojamiento.capacidad || alojamiento.capacidad_maxima || 10}
                      className="form-control"
                      value={huespedes}
                      onChange={(e) => setHuespedes(e.target.value)}
                      required
                  />
                  <span className="text-muted small">
                  Capacidad máxima: {alojamiento.capacidad || alojamiento.capacidad_maxima || 'N/A'} personas.
                </span>
                </div>

                <div className="mb-3">
                  <label className="form-label small fw-semibold text-muted">Metodo de pago</label>
                  <select
                      className="form-select"
                      value={tipoPago}
                      onChange={(e) => setTipoPago(e.target.value)}
                      required
                  >
                    <option value="DEBITO">Tarjeta de Débito</option>
                    <option value="CREDITO">Tarjeta de Crédito</option>
                    <option value="TRANSFERENCIA">Transferencia Bancaria</option>
                  </select>
                </div>

                {/* Desglose de precios en tiempo real */}
                {calculoEstadia.noches > 0 && (
                    <div className="border-top pt-3 mt-3">
                      <div className="d-flex justify-content-between text-muted small mb-1">
                        <span>${Number(alojamiento.precio_noche || alojamiento.precioNoche || 0).toLocaleString('es-CL')} x {calculoEstadia.noches} noche(s)</span>
                        <span>${calculoEstadia.total.toLocaleString('es-CL')}</span>
                      </div>
                      <div className="d-flex justify-content-between fw-bold fs-5 text-dark mt-2">
                        <span>Total</span>
                        <span>${calculoEstadia.total.toLocaleString('es-CL')}</span>
                      </div>
                    </div>
                )}
              </div>

              {/* Pie con botones */}
              <div className="modal-footer border-top-0 pt-0">
                <button
                    type="button"
                    className="btn btn-light rounded-pill px-4"
                    onClick={onClose}
                    disabled={cargando}
                >
                  Cancelar
                </button>
                <button
                    type="submit"
                    className="btn btn-primary rounded-pill px-4"
                    style={{ backgroundColor: '#1668e3' }}
                    disabled={calculoEstadia.noches <= 0 || cargando}
                >
                  {cargando ? 'Reservando...' : 'Reservar ahora'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
  );
}