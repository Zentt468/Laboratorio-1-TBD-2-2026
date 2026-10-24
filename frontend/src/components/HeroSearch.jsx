import React, { useState } from 'react';

export default function HeroSearch({ onBuscar }) {
  const [ciudad, setCiudad] = useState('');
  const [fechaInicio, setFechaInicio] = useState('');
  const [fechaFin, setFechaFin] = useState('');
  const [huespedes, setHuespedes] = useState(1);

  const handleSubmit = (e) => {
    e.preventDefault();
    onBuscar({ ciudad, fechaInicio, fechaFin, huespedes: Number(huespedes) });
  };

  return (
    <div className="position-relative mb-5">
      {/* 1. Contenedor de la Imagen del Banner */}
      <div 
        className="w-100 d-flex align-items-center justify-content-center"
        style={{
          height: '320px',
          backgroundImage: 'linear-gradient(rgba(0,0,0,0.25), rgba(0,0,0,0.25)), url("https://images.unsplash.com/photo-1540541338287-41700207dee6?auto=format&fit=crop&w=1600&q=80")',
          backgroundSize: 'cover',
          backgroundPosition: 'center',
        }}
      >
        <h1 
          className="text-white fw-bold display-5 text-center px-3" 
          style={{ textShadow: '0 2px 8px rgba(0,0,0,0.6)' }}
        >
          Entire place, just for you
        </h1>
      </div>

      {/* 2. Barra de Búsqueda Flotante (con position-relative y z-index explícito) */}
      <div 
        className="container position-relative" 
        style={{ marginTop: '-40px', zIndex: 10 }}
      >
        <form 
          onSubmit={handleSubmit}
          className="bg-white rounded-pill shadow-lg p-2 d-flex flex-wrap align-items-center border"
        >
          {/* Campo: Destino / Ciudad */}
          <div className="col-12 col-md-4 d-flex align-items-center px-3 py-1 border-end">
            <i className="bi bi-geo-alt fs-5 text-muted me-2"></i>
            <div className="flex-grow-1">
              <input
                type="text"
                className="form-control border-0 p-0 shadow-none"
                placeholder="Ciudad o destino (ej. Santiago, Pucón)"
                value={ciudad}
                onChange={(e) => setCiudad(e.target.value)}
              />
            </div>
          </div>

          {/* Campo: Fechas de Entrada y Salida */}
          <div className="col-12 col-md-4 d-flex align-items-center px-3 py-1 border-end">
            <i className="bi bi-calendar-event fs-5 text-muted me-2"></i>
            <div className="d-flex gap-2 flex-grow-1">
              <input
                type="date"
                className="form-control form-control-sm border-0 p-0 shadow-none text-muted"
                value={fechaInicio}
                onChange={(e) => setFechaInicio(e.target.value)}
                title="Fecha de llegada"
              />
              <span className="text-muted">-</span>
              <input
                type="date"
                className="form-control form-control-sm border-0 p-0 shadow-none text-muted"
                value={fechaFin}
                onChange={(e) => setFechaFin(e.target.value)}
                title="Fecha de salida"
              />
            </div>
          </div>

          {/* Campo: Huéspedes */}
          <div className="col-12 col-md-2 d-flex align-items-center px-3 py-1">
            <i className="bi bi-people fs-5 text-muted me-2"></i>
            <input
              type="number"
              min="1"
              max="20"
              className="form-control border-0 p-0 shadow-none"
              placeholder="Huéspedes"
              value={huespedes}
              onChange={(e) => setHuespedes(e.target.value)}
            />
          </div>

          {/* Botón Buscar */}
          <div className="col-12 col-md-2 text-end pe-1">
            <button 
              type="submit" 
              className="btn btn-primary rounded-pill w-100 py-2 fw-semibold d-flex justify-content-center align-items-center gap-2"
              style={{ backgroundColor: '#1668e3' }}
            >
              <i className="bi bi-search"></i>
              <span>Search</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}