// src/pages/PublicarAlojamientoPage.jsx
import React, { useState } from 'react';

export default function PublicarAlojamientoPage({ onPublicarExitoso, onCancelar }) {
  const [formData, setFormData] = useState({
    titulo: '',
    tipo_alojamiento: 'Departamento',
    ciudad: '',
    direccion: '',
    capacidad: 2,
    estadia_min: 1,
    precio_noche: '',
    politica_cancel: 'flexible',
    imagenUrl: '',
  });

  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');

    if (!formData.titulo.trim() || !formData.ciudad.trim() || !formData.direccion.trim()) {
      setError('Por favor completa todos los campos de texto requeridos.');
      return;
    }

    if (Number(formData.precio_noche) <= 0) {
      setError('El precio por noche debe ser mayor a 0.');
      return;
    }

    if (Number(formData.capacidad) <= 0 || Number(formData.estadia_min) <= 0) {
      setError('La capacidad y la estadía mínima deben ser al menos 1.');
      return;
    }

    // Estructura exacta que espera la tabla 'alojamientos' en PostgreSQL
  const nuevoAlojamiento = {
      titulo: formData.titulo.trim(),
      tipo_alojamiento: formData.tipo_alojamiento,
      ciudad: formData.ciudad.trim(),
      direccion: formData.direccion.trim(),
      capacidad: Number(formData.capacidad),
      estadia_min: Number(formData.estadia_min),
      precio_noche: Number(formData.precio_noche),
      politica_cancel: formData.politica_cancel,
      imagenUrl: formData.imagenUrl.trim() || 'https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?auto=format&fit=crop&w=600&q=80',
    };

    onPublicarExitoso(nuevoAlojamiento);
  };

  return (
    <div className="container py-5" style={{ maxWidth: '780px' }}>
      <div className="card border-0 shadow-sm rounded-4 p-4 p-md-5">
        <div className="mb-4">
          <h2 className="fw-bold mb-1">Publica tu propiedad</h2>
          <p className="text-muted small">
            Ingresa los datos del alojamiento para ponerlo a disposición de los huéspedes.
          </p>
        </div>

        {error && (
          <div className="alert alert-danger py-2 small" role="alert">
            <i className="bi bi-exclamation-triangle-fill me-2"></i>
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          {/* Título de la publicación */}
          <div className="mb-3">
            <label className="form-label fw-semibold small text-muted">Título del alojamiento</label>
             <input
              type="text"
              name="titulo"
              maxLength={100}
              className="form-control"
              placeholder="Ej. Cabaña frente al lago con tinaja"
              value={formData.titulo}
              onChange={handleChange}
              required
            />
          </div>

          {/* Tipo de alojamiento y Ciudad */}
          <div className="row g-3 mb-3">
            <div className="col-md-6">
              <label className="form-label fw-semibold small text-muted">Tipo de alojamiento</label>
              <select
                name="tipo_alojamiento"
                className="form-select"
                value={formData.tipo_alojamiento}
                onChange={handleChange}
              >
                <option value="Departamento">Departamento</option>
                <option value="Casa">Casa</option>
                <option value="Cabaña">Cabaña</option>
                <option value="Chalet">Chalet</option>
                <option value="Loft">Loft</option>
                <option value="Habitación">Habitación</option>
              </select>
            </div>

            <div className="col-md-6">
              <label className="form-label fw-semibold small text-muted">Ciudad / Comuna</label>
              <input
                type="text"
                name="ciudad"
                className="form-control"
                placeholder="Ej. Santiago, Pucón, Viña del Mar"
                value={formData.ciudad}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          {/* Dirección */}
          <div className="mb-3">
            <label className="form-label fw-semibold small text-muted">Dirección exacta</label>
            <input
              type="text"
              name="direccion"
              className="form-control"
              placeholder="Ej. Av. Providencia 1234"
              value={formData.direccion}
              onChange={handleChange}
              required
            />
          </div>

          {/* Capacidad, Estadía mínima y Precio por noche */}
          <div className="row g-3 mb-3">
            <div className="col-md-4">
              <label className="form-label fw-semibold small text-muted">Capacidad máxima</label>
              <input
                type="number"
                name="capacidad"
                min="1"
                max="30"
                className="form-control"
                value={formData.capacidad}
                onChange={handleChange}
                required
              />
            </div>

            <div className="col-md-4">
              <label className="form-label fw-semibold small text-muted">Estadía mínima (noches)</label>
              <input
                type="number"
                name="estadia_min"
                min="1"
                max="30"
                className="form-control"
                value={formData.estadia_min}
                onChange={handleChange}
                required
              />
            </div>

            <div className="col-md-4">
              <label className="form-label fw-semibold small text-muted">Precio por noche ($CLP)</label>
              <input
                type="number"
                name="precio_noche"
                min="1000"
                className="form-control"
                placeholder="Ej. 65000"
                value={formData.precio_noche}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          {/* Política de cancelación y URL de imagen */}
          <div className="row g-3 mb-4">
            <div className="col-md-6">
              <label className="form-label fw-semibold small text-muted">Política de cancelación</label>
              <select
                name="politica_cancel"
                className="form-select"
                value={formData.politica_cancel}
                onChange={handleChange}
              >
                <option value="flexible">Flexible (reembolso completo hasta 24h antes)</option>
                <option value="moderada">Moderada (reembolso completo hasta 5 días antes)</option>
                <option value="estricta">Estricta (reembolso completo hasta 14 dias antes)</option>
              </select>
            </div>

            <div className="col-md-6">
              <label className="form-label fw-semibold small text-muted">URL de la imagen</label>
              <input
                type="url"
                name="imagenUrl"
                className="form-control"
                placeholder="https://images.unsplash.com/..."
                value={formData.imagenUrl}
                onChange={handleChange}
              />
              <span className="text-muted" style={{ fontSize: '0.75rem' }}>
                Opcional: Si lo dejas vacío, se usará una imagen referencial.
              </span>
            </div>
          </div>

          {/* Botones de acción */}
          <div className="d-flex justify-content-end gap-2 pt-3 border-top">
            <button
              type="button"
              className="btn btn-light rounded-pill px-4"
              onClick={onCancelar}
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="btn btn-primary rounded-pill px-4 fw-semibold"
              style={{ backgroundColor: '#1668e3' }}
            >
              Publicar propiedad
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}